package com.example.photomemo.ViewModel

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.photomemo.Model.Photo
import com.example.photomemo.Model.PhotoRepository
import com.example.photomemo.Model.PhotoRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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

    fun getAllThumbs(photos: List<Photo>) : List<Pair<Photo, Bitmap?>> {
        return repository.getAllThumbnails(
            photos, getApplication<Application>().contentResolver
        ).toList()
    }

    fun getPhotoInfo(uri: Uri, contentResolver: ContentResolver) : Map<String, String> {
        val info = mutableMapOf<String, String>()
        var cursor: Cursor?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            cursor = contentResolver.query(uri, null, null, null)
        } else {
            cursor = MediaStore.Images.Media.query(
                contentResolver,
                uri,
                null
            )
        }
        cursor?.let {
            if (it.count != 0) {
                it.moveToFirst()
                info["FileName"] =
                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                val size: Long =
                    it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                info["FileSize"] =
                    String.format(Locale.JAPAN, "%.1fMB", size / 1024.0 / 1024.0)
                it.close()
            }
        }
        return info.toMap()
    }
}