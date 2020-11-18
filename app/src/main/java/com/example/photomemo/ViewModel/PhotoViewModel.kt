package com.example.photomemo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.photomemo.Model.Photo
import com.example.photomemo.Model.PhotoRepository
import com.example.photomemo.Model.PhotoRoomDatabase

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository
    val allPhotos: LiveData<List<Photo>>
    init {
        val photoDao = PhotoRoomDatabase.getPhotoDatabase(
            application
        ).photoDao()
        repository = PhotoRepository(photoDao)
        allPhotos = repository.allPhotos
    }
}