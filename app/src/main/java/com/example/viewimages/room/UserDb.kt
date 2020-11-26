package com.example.viewimages.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.viewimages.model.ImageItem

@Database(
    entities = [ImageItem::class],
    version = 1,
    exportSchema = false
)
abstract class UserDb : RoomDatabase(){
    abstract fun images(): ImageDao
}