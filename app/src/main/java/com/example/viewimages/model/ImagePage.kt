package com.example.viewimages.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImagePage {
    @SerializedName("total")
    @Expose
    val total: Int? = null

    @SerializedName("totalHits")
    @Expose
    val totalHits: Int? = null

    @SerializedName("hits")
    @Expose
    lateinit var imageItems: List<ImageItem>


    override fun toString(): String {
        val imagePage = "ImagePage{" +
                "total=" + total +
                ", totalHits=" + totalHits +
                '}'
        val items = StringBuilder()
        for (item in imageItems) items.append("\n").append(item)
        return "$imagePage $items"
    }
}