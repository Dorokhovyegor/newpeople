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