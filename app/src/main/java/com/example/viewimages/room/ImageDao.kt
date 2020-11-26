package com.example.viewimages.room

import androidx.paging.DataSource
import androidx.room.*
import com.example.viewimages.model.ImageItem

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images : List<ImageItem>)

    @Query("SELECT * FROM images ORDER BY indexInResponse ASC")
    fun getImages() : DataSource.Factory<Int, ImageItem>

    @Query("DELETE FROM images WHERE images.id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM images")
    fun deleteAll()

    @Delete
    fun deleteMany( users: List<ImageItem>)

    @Query("SELECT MAX(indexInResponse) + 1 FROM images")
    fun getNextIndex() : Int
}