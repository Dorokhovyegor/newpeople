package com.nullit.newpeople.ui.main.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.nullit.newpeople.R
import com.nullit.newpeople.service.VideoUploader
import com.nullit.newpeople.ui.base.BaseMainFragment
import com.nullit.newpeople.util.ViewModelProviderFactory
import com.nullit.newpeople.util.getRealPathFromURI
import com.nullit.newpeople.util.putIdViolation
import com.nullit.newpeople.util.putVideoPath
import kotlinx.android.synthetic.main.send_video_fragment.*
import javax.inject.Inject

class SendVideoFragment : BaseMainFragment(), ActivityResultHandler {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var sendVideoViewModel: SendVideoViewModel
    private var lastPath: String? = null
    private var lastId: Int? = null
    var attachListener: AttachListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.send_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sendVideoViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[SendVideoViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        sendVideoViewModel.createNewViolation()
    }

    override fun onVideoResult(uri: Uri) {
        sendVideoViewModel.videoUri.value = uri
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListener) {
            attachListener = context
            attachListener?.onAttach(this)
        }
    }

    override fun onDetach() {
        attachListener?.onDetach()
        attachListener = null
        super.onDetach()
    }

    override fun saveState(outState: Bundle) {
        outState.putString("pathVideo", lastPath)
        lastId?.let {
            outState.putInt("violationId", it)
        }
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        lastPath = savedInstanceState?.getString("pathVideo", "")
        lastId = savedInstanceState?.getInt("violationId")
    }

    override fun initListeners() {
        sendButton.setOnClickListener {
            if (lastPath != null && lastId != null) {
                startUploading(lastPath!!, lastId!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Не получается отправить видео",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun subscribeObservers() {
        sendVideoViewModel.videoUri.observe(viewLifecycleOwner, Observer { newVideoUri ->
            requestManager
                .asBitmap()
                .load(newVideoUri)
                .centerCrop()
                .into(previewImageView)
            lastPath = newVideoUri.getRealPathFromURI(requireContext())
        })

        sendVideoViewModel.lastViolationId.observe(viewLifecycleOwner, Observer { lastId ->
            view?.let {
                Snackbar.make(it, "Новый айди получен", Snackbar.LENGTH_SHORT).show()
            }
            this.lastId = lastId
        })
    }

    private fun startUploading(lastPath: String, lastId: Int) {
        val intent = Intent(requireActivity(), VideoUploader::class.java)
        intent.putVideoPath(lastPath)
        intent.putIdViolation(lastId)
        requireActivity().startService(intent)
    }
}

interface ActivityResultHandler {
    fun onVideoResult(uri: Uri)
}

interface AttachListener {
    fun onAttach(sendFragment: ActivityResultHandler)
    fun onDetach()
}