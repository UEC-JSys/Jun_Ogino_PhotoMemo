package com.example.photomemo.Model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PhotoDao {
    @Query("SELECT * from photo_table")
    fun getPhotos(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}