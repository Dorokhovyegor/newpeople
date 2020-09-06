package com.nullit.newpeople.ui.main.photo.adapter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoPresentationModel(
    val path: String,
    val name: String
) : Parcelable