package com.nullit.newpeople.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.nullit.newpeople.service.VideoUploader
import com.nullit.newpeople.ui.main.MainActivity
import com.nullit.newpeople.util.getHasVideo
import com.nullit.newpeople.util.putFlagFromBroadcast

class QueueBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val BROADCAST_VIDEO_ACTION = "com.newpeople.uploadingactionbroadcast"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // i have start service here
        intent?.let {
            if (intent.getHasVideo()) {
                context?.let { mainActivity ->
                    // main activity
                    Log.e("QueueBroadcastReceiver", "Есть еще видосы, продолжаю грузить!")
                    (mainActivity as MainActivity).startService(
                        Intent(
                            mainActivity,
                            VideoUploader::class.java
                        ).apply {
                            putFlagFromBroadcast(true)
                        })
                }
            }
        }
    }
}