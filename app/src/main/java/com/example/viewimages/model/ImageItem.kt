package com.example.viewimages.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
data class ImageItem(

        @PrimaryKey
        @SerializedName("id") val id: Int,
        @SerializedName("pageURL") val pageURL: String,
        @SerializedName("type") val type: String,

        @SerializedName("tags") val tags: String,
        @SerializedName("previewURL") val previewURL: String,
        @SerializedName("previewWidth") val previewWidth: Int,
        @SerializedName("previewHeight") val previewHeight: Int,

        @SerializedName("webformatURL") val webformatURL: String,
        @SerializedName("webformatWidth") val webformatWidth: Int,
        @SerializedName("webformatHeight") val webformatHeight: Int,
        @SerializedName("largeImageURL") val largeImageURL: String,
        @SerializedName("imageWidth") val imageWidth: Int,
        @SerializedName("imageHeight") val imageHeight: Int,
        @SerializedName("imageSize") val imageSize: Int,

        @SerializedName("views") val views: Int,
        @SerializedName("downloads") val downloads: Int,
        @SerializedName("favorites") val favorites: Int,

        @SerializedName("likes") val likes: Int,
        @SerializedName("comments") val comments: Int,

        @SerializedName("user_id") val user_id: Int,
        @SerializedName("user") val user: String,
        @SerializedName("userImageURL") val userImageURL: String){

        override fun toString(): String {
                return "com.example.viewimages.model.ImageItem{" +
                        "id=" + id +
                        ", type='" + type + '\'' +
                        ", largeImageURL='" + largeImageURL + '\'' +
                        '}'
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as ImageItem

                if (id != other.id) return false
                if (pageURL != other.pageURL) return false
                if (type != other.type) return false
                if (tags != other.tags) return false
                if (previewURL != other.previewURL) return false
                if (previewWidth != other.previewWidth) return false
                if (previewHeight != other.previewHeight) return false
                if (webformatURL != other.webformatURL) return false
                if (webformatWidth != other.webformatWidth) return false
                if (webformatHeight != other.webformatHeight) return false
                if (largeImageURL != other.largeImageURL) return false
                if (imageWidth != other.imageWidth) return false
                if (imageHeight != other.imageHeight) return false
                if (imageSize != other.imageSize) return false
                if (views != other.views) return false
                if (downloads != other.downloads) return false
                if (favorites != other.favorites) return false
                if (likes != other.likes) return false
                if (comments != other.comments) return false
                if (user_id != other.user_id) return false
                if (user != other.user) return false
                if (userImageURL != other.userImageURL) return false
                if (indexInResponse != other.indexInResponse) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id
                result = 31 * result + pageURL.hashCode()
                result = 31 * result + type.hashCode()
                result = 31 * result + tags.hashCode()
                result = 31 * result + previewURL.hashCode()
                result = 31 * result + previewWidth
                result = 31 * result + previewHeight
                result = 31 * result + webformatURL.hashCode()
                result = 31 * result + webformatWidth
                result = 31 * result + webformatHeight
                result = 31 * result + largeImageURL.hashCode()
                result = 31 * result + imageWidth
                result = 31 * result + imageHeight
                result = 31 * result + imageSize
                result = 31 * result + views
                result = 31 * result + downloads
                result = 31 * result + favorites
                result = 31 * result + likes
                result = 31 * result + comments
                result = 31 * result + user_id
                result = 31 * result + user.hashCode()
                result = 31 * result + userImageURL.hashCode()
                result = 31 * result + indexInResponse
                return result
        }

        var indexInResponse : Int = -1

}