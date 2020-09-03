package com.nullit.newpeople.util

import android.util.Log
import androidx.work.Data
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBuilderManager
@Inject constructor(
    private val dataBuilder: Data.Builder
) {
    val hashSet: HashSet<Pair<String, String>> = HashSet()

    fun putStringPhotoWithIndex(photoPath: String): DataBuilderResult {
        try {
            hashSet.add(Pair("photo", photoPath))
            return DataBuilderResult.Success
        } catch (e: Exception) {
            e.printStackTrace()
            return DataBuilderResult.Error
        }
    }

    fun putVideo(videoPath: String): DataBuilderResult {
        try {
            hashSet.add(Pair("video", videoPath))
            return DataBuilderResult.Error
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return DataBuilderResult.Error
        }
    }

    fun prepareDataAndBuild(): Data {
        val setIterator = hashSet.iterator()
        while (setIterator.hasNext()) {
            val item = setIterator.next()
            dataBuilder.putString(item.first, item.second)
        }
        Log.e("DataBuilderManager", dataBuilder.build().keyValueMap.toString())
        return dataBuilder.build()
    }
}

sealed class DataBuilderResult {
    object Success : DataBuilderResult()
    object Error : DataBuilderResult()
}
