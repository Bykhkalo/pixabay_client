package com.example.viewimages.view.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.viewimages.R
import com.example.viewimages.model.ImageItem

class ImageItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private var context: Context = view.context

    private var image: ImageView = view.findViewById(R.id.loaded_image)
    private var views: TextView = view.findViewById(R.id.views_textview)
    private var likes: TextView = view.findViewById(R.id.likes_textView)
    private var downloads: TextView = view.findViewById(R.id.downloads_textview)


    companion object {
        fun create(parent: ViewGroup): ImageItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)


            return ImageItemViewHolder(view)
        }
    }

    fun bind(item: ImageItem?) {

        if (item != null) {
            Glide
                .with(context)
                .load(item.largeImageURL)
                .into(image)

            views.text = item.views.toString()
            likes.text = item.likes.toString()
            downloads.text = item.downloads.toString()


        }



    }


}