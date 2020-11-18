package com.example.photomemo.View

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photomemo.Model.Photo
import com.example.photomemo.ViewModel.PhotoViewModel
import com.example.photomemo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), PhotoListAdapter.Listener {
    private val addActivityRequestCode = 1
    private val requestExternalStorage = 2
    // Changed
    private lateinit var viewModel: PhotoViewModel
    val adapter = PhotoListAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PhotoListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        viewModel.allPhotos.observe(this, Observer { photos ->
            photos?.let {
                adapter.setPhotos(viewModel.getAllThumbs(it) as List<Pair<Photo, Bitmap>>)
                val info = viewModel.getPhotoInfo(it[dataIndex])
                findViewById<ImageView>(R.id.detailInfoTextView).text =
                    String.format(Locale.JAPAN, "Photo Info: \n\tFile Name: %s\n", "\tFile Size: %s", info["FileName"], info["FileSize"])
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPhotoActivity::class.java)
            startActivityForResult(intent, addActivityRequestCode)
        }

        // パーミッションの状態を取得
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                // ユーザーに問い合わせる
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestExternalStorage
            )
        }
    }

    override fun onItemClicked(index: Int) {
        // Key:Valueの形で何番目の写真かを呼び出し先に渡す。
        val intent = Intent(this@MainActivity, PhotoDetailActivity::class.java)
        intent.putExtra(PhotoDetailActivity.EXTRA_DATA_INDEX, index)
        startActivity(intent)
    }

    // ユーザーへの問い合わせの結果が来る
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestExternalStorage) {
            // grantResultsに結果が格納されている 許可されなかった場合はToastでメッセージを表示し、アプリを終了する
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Must have permission to access external storage.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addActivityRequestCode) {
            if(resultCode != Activity.RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Photo additions have been cancelled.",
                Toast.LENGTH_LONG

                ).show()
            }
        }
    }
}
