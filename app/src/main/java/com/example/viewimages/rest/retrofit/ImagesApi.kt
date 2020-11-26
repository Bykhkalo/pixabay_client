package com.example.viewimages.rest.retrofit

import com.example.viewimages.model.ImagePage
import com.example.viewimages.rest.retrofit.API.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("/api/")
    fun getImages(
            @Query("q") query: String,
            @Query("image_type") type: String = "all",
            @Query("orientation") orientation: String,
            @Query("category") category: String,
            @Query("min_width") minWidth: Int,
            @Query("min_height") minHeight: Int,
            @Query("safesearch") safeSearch: Boolean,
            @Query("order") order: String,
            @Query("page") pageNum: Int,
            @Query("per_page") pageSize: Int,
            @Query("key") apiKey: String = API_KEY
    ): Call<ImagePage>


    //Paging API
    @GET("api")
    fun getAll(@Query("page") pageNum: Long,
               @Query("per_page") pageSize: Int,
               @Query("key") apiKey: String = API_KEY): Call<ImagePage?>?

    @GET("api")
    fun findImages(@Query("q") query: String?,
                   @Query("image_type") type: String?,
                   @Query("orientation") orientation: String?,
                   @Query("category") category: String?,
                   @Query("min_width") minWidth: Int,
                   @Query("min_height") minHeight: Int,
                   @Query("safesearch") safeSearch: Boolean,
                   @Query("order") order: String?,
                   @Query("page") pageNum: Long,
                   @Query("per_page") pageSize: Int,
    @Query("key") apiKey: String = API_KEY): Call<ImagePage?>?


}