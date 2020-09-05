package com.nullit.newpeople.util

import android.content.Intent

fun Intent.putIdViolation(violationId: Int) {
    this.putExtra("violationId", violationId)
}

fun Intent.putVideoPath(videoPath: String) {
    this.putExtra("videoPath", videoPath)
}

fun Intent.getViolationId(): Int {
    return this.getIntExtra("violationId", -1)
}

fun Intent.getVideoPath(): String {
    return this.getStringExtra("videoPath") ?: ""
}

fun Intent.putHasVideo(hasVideos: Boolean) {
    this.putExtra("has_videos", hasVideos)
}

fun Intent.getHasVideo(): Boolean {
    return this.getBooleanExtra("has_videos", false)
}

fun Intent.putFlagFromBroadcast(value: Boolean) {
    this.putExtra("startFromBroadcast", value)
}

fun Intent.getFlagFromBroadcast(): Boolean {
    return this.getBooleanExtra("startFromBroadcast", false)
}

fun Intent.putPhotos(list: ArrayList<String>) {
    this.putStringArrayListExtra("photos", list)
}

fun Intent.getPhotos(): ArrayList<String>? {
    return this.getStringArrayListExtra("photos")
}