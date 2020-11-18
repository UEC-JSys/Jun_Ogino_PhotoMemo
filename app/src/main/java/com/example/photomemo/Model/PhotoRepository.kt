package com.example.photomemo.Model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import androidx.lifecycle.LiveData

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()
    private var allThumbs: MutableMap<Photo, Bitmap?> = mutableMapOf()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    fun getAllThumbnails(photos: List<Photo>, contentResolver: ContentResolver)
        : Map<Photo, Bitmap?> {
        photos.forEach {
            if (!allThumbs.containsKey(it)) {
                allThumbs[it] = getThumbnail(Uri.parse(it.uri), contentResolver)
            }
        }
        return allThumbs.toMap()
    }

    fun getThumbnail(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            return contentResolver.loadThumbnail(uri, Size(100, 100), null)
        } else {

        }
    }
}