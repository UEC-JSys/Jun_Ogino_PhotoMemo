package com.example.photomemo.View

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.photomemo.ViewModel.AddPhotoViewModel
import com.example.photomemo.Model.Photo
import com.example.photomemo.R

class AddPhotoActivity : AppCompatActivity() {
    private val pickPhotoRequestCode = 2
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        val openButton: Button = findViewById(R.id.addPhotoMemoOpenButton)
        openButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                setType("image/jpeg")
            }
            startActivityForResult(intent, pickPhotoRequestCode)
        }

        val saveButton: Button = findViewById(R.id.addPhotoMemoSaveButton)
        saveButton.setOnClickListener {
            val editText = findViewById<EditText>(R.id.addPhotoMemoEditText)
            val replyIntent = Intent()
            if (imageUri == null || TextUtils.isEmpty(editText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val photo = Photo(
                    imageUri.toString(),
                    editText.text.toString()
                )
                val viewModel = ViewModelProvider(this).get(AddPhotoViewModel::class.java)
                viewModel.insert(photo)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickPhotoRequestCode && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
//                if (Build.VERSION.SDK_INT >=
//                    Build.VERSION_CODES.)
//                    contentResolver.takePersistableUriPermission(
//                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val imageView = findViewById<ImageView>(R.id.addPhotoMemoImageView)
                imageView.setImageURI(it)
                imageUri = it
            }
        }
    }
}
