package com.example.photomemo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.photomemo.Model.Photo
import com.example.photomemo.Model.PhotoRepository
import com.example.photomemo.Model.PhotoRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository
    init {
        val photoDao = PhotoRoomDatabase.getPhotoDatabase(
            application
        ).photoDao()
        repository = PhotoRepository(photoDao)
    }
    fun insert(photo: Photo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(photo)
    }

    // これはどこに置く？
    fun delete(photo: Photo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(photo)
    }
}