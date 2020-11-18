package com.example.photomemo.Model

import androidx.lifecycle.LiveData

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }
}