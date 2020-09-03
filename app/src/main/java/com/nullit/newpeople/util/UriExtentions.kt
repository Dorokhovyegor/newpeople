package com.nullit.newpeople.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log


fun Uri.getRealPathFromURI(context: Context): String? {
    if ("content".equals(this.scheme, true)) {
        val path = this.getDataColumn(context, null, null)
        return path
    }
    return null
}


fun Uri.getDataColumn(
    context: Context?, selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = context?.contentResolver?.query(
            this, projection, selection, selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}

fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == this.authority
}

fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == this.authority
}

fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == this.authority
}

fun Uri.isGooglePhotosUri(): Boolean {
    return "com.google.android.apps.photos.content" == this.authority
}