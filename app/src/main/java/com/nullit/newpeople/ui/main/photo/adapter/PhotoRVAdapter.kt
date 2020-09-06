package com.nullit.newpeople.ui.main.photo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.nullit.newpeople.R
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotoRVAdapter(
    val requestManager: RequestManager,
    val addPhotoListener: AddPhotoListener?,
    val photoListener: PhotoListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var photos = ArrayList<PhotoPresentationModel>().apply {
        add(PhotoPresentationModel("add_photo", "no_name"))
    }

    val ADD_PHOTO_ITEM = 1
    val PHOTO_ITEM = 2

    fun submitList(arrayList: List<PhotoPresentationModel>) {
        photos.clear()
        Log.e("PhotoAdapter", arrayList.toString())
        val localList = ArrayList<PhotoPresentationModel>()
        localList.add(PhotoPresentationModel("add_photo", "no_name"))
        if (arrayList.size > 10) {
            localList.addAll(arrayList.subList(0, 9))
        } else {
            localList.addAll(arrayList)
        }
        photos = localList
        notifyDataSetChanged()
    }

    fun deletePhoto(item: PhotoPresentationModel): Int {
        if (photos.remove(item)) {
            notifyDataSetChanged()
            return 1
        } else {
            return -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ADD_PHOTO_ITEM -> {
                return AddPhotoViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.add_new_photo_item, parent, false), addPhotoListener
                )
            }
            PHOTO_ITEM -> {
                return PhotoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false),
                    photoListener
                )
            }
            else -> {
                return PhotoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false),
                    photoListener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun getItemViewType(position: Int): Int {
        if (photos.get(position).path == "add_photo") {
            return ADD_PHOTO_ITEM
        } else {
            return PHOTO_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddPhotoViewHolder -> {
                holder.bind()
            }
            is PhotoViewHolder -> {
                holder.bind(photoItem = photos[position])
            }
        }
    }

    inner class AddPhotoViewHolder(
        view: View,
        val addPhotoListener: AddPhotoListener?
    ) : RecyclerView.ViewHolder(view) {

        fun bind() = itemView.setOnClickListener {
            addPhotoListener?.addPhoto()
        }
    }

    inner class PhotoViewHolder(
        view: View,
        val photoListener: PhotoListener?
    ) : RecyclerView.ViewHolder(view) {

        fun bind(photoItem: PhotoPresentationModel) = with(itemView) {
            // path and that is all
            itemView.rootView.setOnClickListener {
                photoListener?.openPhoto(photoItem)
            }
            requestManager.load(photoItem.path).centerCrop().into(photo)
            itemView.deleteButton.setOnClickListener {
                photoListener?.deletePhoto(photoItem)
            }
        }
    }
}

interface AddPhotoListener {
    fun addPhoto()
}

interface PhotoListener {
    fun deletePhoto(photoItem: PhotoPresentationModel)
    fun openPhoto(photoItem: PhotoPresentationModel)
}
