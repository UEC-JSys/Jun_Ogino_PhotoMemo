package com.example.photomemo.View

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photomemo.Model.Photo
import com.example.photomemo.R

class PhotoListAdapter internal constructor(context: Context, listener: Listener)
    : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var photos = emptyList<Pair<Photo, Bitmap?>>()
    private val clickListener: Listener = listener

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoItemView: ImageView = itemView.findViewById(R.id.imageView)
        val memoItemView: TextView = itemView.findViewById(R.id.textView)
    }

    interface Listener {
        fun onItemClicked(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = photos[position]
        holder.memoItemView.text = current.first.memo
        if (current.second !== null) {
            holder.photoItemView.setImageBitmap(current.second)
        } else {
            holder.photoItemView.setImageURI(Uri.parse(current.first.uri))
        }
        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(holder.adapterPosition)
        }
    }

    internal fun setPhotos(photos: List<Pair<Photo, Bitmap>>) {
        this.photos = photos
        notifyDataSetChanged()
    }
    override fun getItemCount() = photos.size
}