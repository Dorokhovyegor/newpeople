package com.nullit.newpeople.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.esafirm.imagepicker.features.ImagePicker
import com.nullit.newpeople.R
import com.nullit.newpeople.ui.base.BaseActivity
import com.nullit.newpeople.ui.main.photo.ActivityResultHandlerPhoto
import com.nullit.newpeople.ui.main.photo.AttachListenerPhoto
import com.nullit.newpeople.ui.main.video.ActivityResultHandlerVideo
import com.nullit.newpeople.ui.main.video.AttachListenerVideo

class MainActivity : BaseActivity(), AttachListenerVideo, AttachListenerPhoto {

    var activityResultHandlerVideo: ActivityResultHandlerVideo? = null
    var activityResultHandlerPhoto: ActivityResultHandlerPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        handleOnActivityResult(data, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleOnActivityResult(intent: Intent?, resultCode: Int) {
        intent?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (intent.hasExtra("selectedImages")) {
                    // значит тут фотки
                    val images = ImagePicker.getImages(intent)
                    activityResultHandlerPhoto?.onPhotosResult(images)
                } else {
                    // значит видео
                    val videoUri: Uri = intent.data!!
                    activityResultHandlerVideo?.onVideoResult(videoUri)
                }
            }
        }
    }

    override fun onAttach(sendFragment: ActivityResultHandlerVideo) {
        activityResultHandlerVideo = sendFragment
    }

    override fun onDetach() {
        activityResultHandlerVideo = null
    }

    override fun onDetachPhoto() {
        activityResultHandlerPhoto = null
    }

    override fun onAttach(sendFragment: ActivityResultHandlerPhoto) {
        Log.e("MainActivity", sendFragment.toString())
        activityResultHandlerPhoto = sendFragment
    }
}