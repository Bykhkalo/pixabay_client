package com.example.viewimages.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.viewimages.model.ImageSearchRequest
import com.example.viewimages.repository.MainRepository
import com.example.viewimages.utils.DebugUtils.TAG


class ImagesViewModel(private val repository: MainRepository) : ViewModel() {

    private val request = MutableLiveData<ImageSearchRequest>()
    private val repoResult = Transformations.map(request) {
            Log.d(TAG, "paging: q: ${it.query}")
        repository.getImages(it)
    }

    val images = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }


    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }


    fun begin(imageSearchRequest: ImageSearchRequest){
        request.value = imageSearchRequest
    }

}