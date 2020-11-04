package com.example.photomemo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotoListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var photos = emptyList<Photo>()

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoItemView: ImageView = itemView.findViewById(R.id.imageView)
        val memoItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = photos[position]
        holder.memoItemView.text = current.memo
        holder.photoItemView.setImageURI(Uri.parse(current.uri))
    }

    internal fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }
    override fun getItemCount() = photos.size
}