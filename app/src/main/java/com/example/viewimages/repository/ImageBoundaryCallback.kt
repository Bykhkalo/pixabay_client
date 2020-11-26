package com.example.viewimages.repository

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.viewimages.model.ImageItem
import com.example.viewimages.model.ImagePage
import com.example.viewimages.model.ImageSearchRequest
import com.example.viewimages.rest.retrofit.ImagesApi
import com.example.viewimages.utils.DebugUtils.TAG
import com.memebattle.pagingwithrepository.domain.repository.network.createStatusLiveData
import com.memebattle.pwc.helper.PwcPagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class ImageBoundaryCallback(
        private val webservice: ImagesApi,
        private val handleResponse: (ImagePage?) -> Unit,
        private val ioExecutor: Executor,
        private val networkPageSize: Int,
        private val imageSearchRequest: ImageSearchRequest,
) : PagedList.BoundaryCallback<ImageItem>() {

    val helper = PwcPagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    companion object {
        //Данна змінна потрібна мені лише для логів.
        //Для роботи программи її можна прибрати
        //Зробивши локальною в onItemAtEndLoaded
        var pageNum: Int = 0
    }


    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        Log.d(TAG, "BoundaryCallback: onZeroItemsLoaded: ")
        helper.runIfNotRunning(PwcPagingRequestHelper.RequestType.BEFORE) {
            webservice.getImages(imageSearchRequest.query,
                    imageSearchRequest.imageType,
                    imageSearchRequest.orientation,
                    imageSearchRequest.imageCategory,
                    imageSearchRequest.minWidth,
                    imageSearchRequest.minHeight,
                    imageSearchRequest.isSafeSearchEnabled,
                    imageSearchRequest.imageOrder,
                    1, networkPageSize)
                .enqueue(createWebserviceCallback(it))
        }
    }

    private fun createWebserviceCallback(it: PwcPagingRequestHelper.Request.Callback)
            : Callback<ImagePage> {
        return object : Callback<ImagePage> {
            override fun onFailure(call: Call<ImagePage>, t: Throwable) {
                Log.d(TAG, "BoundaryCallback.onFailure")
                it.recordFailure(t)
            }

            override fun onResponse(call: Call<ImagePage>, response: Response<ImagePage>) {
                Log.d(TAG, "BoundaryCallback.onResponse: loaded: $response")
                insertItemsIntoDb(response, it)
            }

        }
    }

    private fun insertItemsIntoDb(
        response: Response<ImagePage>,
        it: PwcPagingRequestHelper.Request.Callback
    ) {
        Log.d(TAG, "BoundaryCallback.insertItemsIntoDb")
        ioExecutor.execute {
            handleResponse(response.body())
            it.recordSuccess()
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: ImageItem) {
        Log.d(TAG, "BoundaryCallback.onItemAtEndLoaded")
        helper.runIfNotRunning(PwcPagingRequestHelper.RequestType.AFTER) {

            val currentPage = (itemAtEnd.indexInResponse + 1) / networkPageSize
            pageNum = currentPage + 1

            Log.d(TAG, "onItemAtEndLoaded: itemIndex: ${itemAtEnd.indexInResponse} pageNum: $pageNum")

            webservice.getImages(imageSearchRequest.query,
                    imageSearchRequest.imageType,
                    imageSearchRequest.orientation,
                    imageSearchRequest.imageCategory,
                    imageSearchRequest.minWidth,
                    imageSearchRequest.minHeight,
                    imageSearchRequest.isSafeSearchEnabled,
                    imageSearchRequest.imageOrder,
                    pageNum, networkPageSize)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: ImageItem) {
        // ignored, since we only ever append to what's in the DB
    }
}