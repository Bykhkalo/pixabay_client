package com.example.viewimages.repository

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Room
import com.example.viewimages.model.ImageItem
import com.example.viewimages.model.ImagePage
import com.example.viewimages.model.ImageSearchRequest
import com.example.viewimages.rest.retrofit.API
import com.example.viewimages.rest.retrofit.ImagesApi
import com.example.viewimages.room.ImageDao
import com.example.viewimages.room.UserDb
import com.example.viewimages.utils.DebugUtils.TAG
import com.memebattle.pwc.util.NetworkState
import com.memebattle.pwc.util.PwcListing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class MainRepository(context: Context, var pageSize: Int, var prefetchDistance: Int = pageSize) : PixabayImagesRepository {

    var db = Room.databaseBuilder(
            context,
            UserDb::class.java, "database"
    ).build()


    private var imagesApi: ImagesApi = API().getUsersApi()
    private var dao: ImageDao

    init {
        dao = db.images()
    }

    val ioExecutor = Executors.newSingleThreadExecutor()

    override fun getImages(imageSearchRequest: ImageSearchRequest): PwcListing<ImageItem> {

        if (imageSearchRequest.isNewRequest) clearDb()

        Log.d(TAG, "getImages: $pageSize")
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ImageBoundaryCallback(
                webservice = imagesApi,
                handleResponse = this::insertResultIntoDb,
                ioExecutor = ioExecutor,
                networkPageSize = pageSize,
                imageSearchRequest = imageSearchRequest,
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(imageSearchRequest)
        }
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setMaxSize(pageSize + 2 * prefetchDistance)
                .setPrefetchDistance(prefetchDistance)
                .setPageSize(pageSize)
                .build()

        val livePagedList = LivePagedListBuilder(db.images().getImages(), config)
                .setBoundaryCallback(boundaryCallback)
                .build()


        return PwcListing(
                pagedList = livePagedList,
                networkState = boundaryCallback.networkState,
                retry = {
                    boundaryCallback.helper.retryAllFailed()
                },
                refresh = {
                    refreshTrigger.value = null
                },
                refreshState = refreshState
        )
    }

    private fun clearDb() {
        ioExecutor.execute {
            db.runInTransaction {
                dao.deleteAll()
            }
        }
    }

    private fun insertResultIntoDb(body: ImagePage?) {
        body!!.imageItems.let { users ->
            val start = dao.getNextIndex()
            val items = users.mapIndexed { index, imageItem ->
                imageItem.indexInResponse = start + index
                imageItem
                /*Даний метод визначення позиції елементів
                  у списку де він буде відображатися.

                  У кожного user є поле індекса.

                  Це поле визначає положення елемента у своїй группі
                  Наприклад, група постів Василя чи постів Віктора

                  Типу пам'ять:
                  [пости Віктора(індекси з 11 по 20), пости Василя(індекси з 0 по 10), пости Віктора(індекси з 0 по 10)]

                */

            }

            db.images().insert(items)
        }
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     * <p>
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    @MainThread
    private fun refresh(imageSearchRequest: ImageSearchRequest): LiveData<NetworkState> {
        Log.d(TAG, "repository.refresh(): ")
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        imagesApi.getImages(imageSearchRequest.query,
        imageSearchRequest.imageType,
        imageSearchRequest.orientation,
        imageSearchRequest.imageCategory,
        imageSearchRequest.minWidth,
        imageSearchRequest.minHeight,
        imageSearchRequest.isSafeSearchEnabled,
        imageSearchRequest.imageOrder,
        1, pageSize).enqueue(
                object : Callback<ImagePage> {
                    override fun onFailure(call: Call<ImagePage>, t: Throwable) {
                        // retrofit calls this on main thread so safe to call set value
                        networkState.value = NetworkState.error(t.message)
                    }

                    override fun onResponse(call: Call<ImagePage>, response: Response<ImagePage>) {
                        Log.d(TAG, "MainRepository.refresh(): onResponse: pageNum: 1")
                        ioExecutor.execute {
                            db.runInTransaction {
                                dao.deleteAll()
                                insertResultIntoDb(response.body())
                            }
                            // since we are in bg thread now, post the result.
                            networkState.postValue(NetworkState.LOADED)
                        }
                    }
                }
        )
        return networkState
    }

}