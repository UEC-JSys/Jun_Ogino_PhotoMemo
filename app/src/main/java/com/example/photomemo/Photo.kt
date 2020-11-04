package com.example.photomemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "photo_table")
data class Photo(
    @PrimaryKey @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "memo") val memo: String
) {
}

@Dao
interface PhotoDao {
    @Query("SELECT * from photo_table")
    fun getPhotos(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)
}

@Database(entities = [Photo::class], version = 1, exportSchema = false)
public abstract class PhotoRoomDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: PhotoRoomDatabase? = null
        fun getPhotoDatabase(context: Context): PhotoRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRoomDatabase::class.java,
                    "photo_database"

                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }
}

class AddPhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository
    init {
        val photoDao = PhotoRoomDatabase.getPhotoDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)
    }
    fun insert(photo: Photo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(photo)
    }
}