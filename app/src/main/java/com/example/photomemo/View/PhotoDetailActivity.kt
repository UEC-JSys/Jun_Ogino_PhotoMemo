package com.example.photomemo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photomemo.R

class PhotoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        val dataIndex = getIntent().getIntExtra(EXTRA_DATA_INDEX, -1)
    }

    companion object {
        const val EXTRA_DATA_INDEX = "com.example.photomemo.detail.DATA_INDEX"
    }
}
