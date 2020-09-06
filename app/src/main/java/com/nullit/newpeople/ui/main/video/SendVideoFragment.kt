package com.nullit.newpeople.ui.main.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.nullit.newpeople.R
import com.nullit.newpeople.service.VideoUploader
import com.nullit.newpeople.ui.base.BaseMainFragment
import com.nullit.newpeople.util.ViewModelProviderFactory
import com.nullit.newpeople.util.getRealPathFromURI
import kotlinx.android.synthetic.main.send_video_fragment.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendVideoFragment : BaseMainFragment(), ActivityResultHandlerVideo {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager
    lateinit var sendVideoViewModel: SendVideoViewModel
    private var lastPath: String? = null
    private var lastId: Int? = null
    private var lastCheckedTypeId: Int? = null

    var attachListener: AttachListenerVideo? = null

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
        sendVideoViewModel.getViolationList()
    }

    override fun onVideoResult(uri: Uri) {
        sendVideoViewModel.videoUri.value = uri
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerVideo) {
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
        lastCheckedTypeId?.let {
            outState.putInt("lastTypeId", it)
        }
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        lastPath = savedInstanceState?.getString("pathVideo", "")
        lastId = savedInstanceState?.getInt("violationId")
        lastCheckedTypeId = savedInstanceState?.getInt("lastTypeId")
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
        cancelButton.setOnClickListener {
            stopUploading()
            findNavController().popBackStack()
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

        sendVideoViewModel.violationList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                categories.removeAllViews()
                list.forEach { entity ->
                    categories.addView(RadioButton(context).apply {
                        text = entity.name
                        tag = entity.violationId.toString()
                        setOnCheckedChangeListener { buttonView, isChecked ->
                            lastCheckedTypeId = buttonView.tag.toString().toInt()
                        }
                    })
                }
                lastCheckedTypeId?.let { id ->
                    categories?.children?.forEach { radio ->
                        if (radio is RadioButton) {
                            if (radio.tag.toString().toInt() == id) {
                                radio.isChecked = true
                                return@forEach
                            }
                        }
                    }
                }
            }
        })

        sendVideoViewModel.lastViolationId.observe(viewLifecycleOwner, Observer { lastId ->
            titleReportId.text = "Заявка №${lastId}"
            this.lastId = lastId
        })
    }

    private fun startUploading(lastPath: String, lastId: Int) {
        lifecycleScope.launch {
            sendVideoViewModel.addNewVideoIntoQueue(lastPath, lastId)
            if (lastCheckedTypeId != null) {
                val addTypeResult = async {
                    sendVideoViewModel.addCategory(lastId, lastCheckedTypeId!!)
                }
                addTypeResult.await()
                val intent = Intent(requireActivity(), VideoUploader::class.java)
                requireActivity().startService(intent)
                Toast.makeText(
                    requireContext(),
                    "Отправлено в очередь на отправку",
                    Toast.LENGTH_SHORT
                )
                    .show()
                findNavController().popBackStack()
            } else {
                val intent = Intent(requireActivity(), VideoUploader::class.java)
                requireActivity().startService(intent)
                Toast.makeText(
                    requireContext(),
                    "Отправлено в очередь на отправку",
                    Toast.LENGTH_SHORT
                )
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    private fun stopUploading() {
        val intentService = Intent(requireActivity(), VideoUploader::class.java)
        requireActivity().stopService(intentService)
    }
}

interface ActivityResultHandlerVideo {
    fun onVideoResult(uri: Uri)
}

interface AttachListenerVideo {
    fun onAttach(sendFragment: ActivityResultHandlerVideo)
    fun onDetach()
}