package com.nullit.newpeople.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nullit.newpeople.R
import com.nullit.newpeople.ui.base.BaseActivity
import com.nullit.newpeople.ui.main.video.ActivityResultHandler
import com.nullit.newpeople.ui.main.video.AttachListener
import com.nullit.newpeople.ui.main.video.SendVideoFragment

class MainActivity : BaseActivity(), AttachListener {

    var activityResultHandler: ActivityResultHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val videoUri: Uri = intent.data!!
                Log.e("MainActivityDebug", "$videoUri")
                activityResultHandler?.onVideoResult(videoUri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(sendFragment: ActivityResultHandler) {
        activityResultHandler = sendFragment
    }

    override fun onDetach() {
        activityResultHandler = null
    }
}