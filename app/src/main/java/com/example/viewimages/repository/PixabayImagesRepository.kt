package com.example.viewimages.repository

import com.example.viewimages.model.ImageItem
import com.example.viewimages.model.ImageSearchRequest
import com.memebattle.pwc.util.PwcListing


interface PixabayImagesRepository {
    fun getImages(imageSearchRequest: ImageSearchRequest) : PwcListing<ImageItem>
}