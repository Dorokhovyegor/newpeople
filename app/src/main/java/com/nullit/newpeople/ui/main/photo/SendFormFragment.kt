package com.nullit.newpeople.ui.main.photo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.nullit.newpeople.R
import com.nullit.newpeople.service.PhotoUploader
import com.nullit.newpeople.ui.base.BaseMainFragment
import com.nullit.newpeople.ui.main.photo.adapter.AddPhotoListener
import com.nullit.newpeople.ui.main.photo.adapter.PhotoListener
import com.nullit.newpeople.ui.main.photo.adapter.PhotoPresentationModel
import com.nullit.newpeople.ui.main.photo.adapter.PhotoRVAdapter
import com.nullit.newpeople.util.ViewModelProviderFactory
import com.nullit.newpeople.util.putIdViolation
import com.nullit.newpeople.util.putPhotos
import kotlinx.android.synthetic.main.send_photo_fragment.*
import kotlinx.android.synthetic.main.send_photo_fragment.cancelButton
import kotlinx.android.synthetic.main.send_photo_fragment.sendButton
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendFormFragment : BaseMainFragment(), AddPhotoListener, PhotoListener,
    ActivityResultHandlerPhoto {

    lateinit var adapter: PhotoRVAdapter
    @Inject
    lateinit var requestManager: RequestManager
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var sendPhotoViewModel: SendPhotoViewModel
    var photos: ArrayList<PhotoPresentationModel> = ArrayList()
    var lastViolationId: Int? = null
    var attachListener: AttachListenerPhoto? = null
    private var lastCheckedTypeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.send_photo_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sendPhotoViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[SendPhotoViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            initRecyclerView()
        }
        sendPhotoViewModel.getViolationList()
    }

    private fun initRecyclerView() {
        adapter = PhotoRVAdapter(requestManager, this@SendFormFragment, this@SendFormFragment)
        photosRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = this@SendFormFragment.adapter
        }
    }

    override fun saveState(outState: Bundle) {
        outState.putParcelableArrayList("photos", photos)
        lastCheckedTypeId?.let {
            outState.putInt("lastTypeId", it)
        }
    }

    private fun startUploading() {
        val job = lifecycleScope.launch {
            val violationResult = async {
                sendPhotoViewModel.createNewViolation()
            }
            val vioId = violationResult.await()
            if (lastCheckedTypeId != null) {
                val addTypeResult = async {
                    sendPhotoViewModel.addCategory(lastViolationId!!, lastCheckedTypeId!!)
                }
                addTypeResult.await()
                if (photos.isNotEmpty() && vioId != -1) {
                    val intent = Intent(requireActivity(), PhotoUploader::class.java)
                    intent.putPhotos(photos)
                    intent.putIdViolation(lastViolationId!!)
                    requireActivity().startService(intent)
                } else {
                    Toast.makeText(requireContext(), "Попробуйте еще раз", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (photos.isNotEmpty() && vioId != -1) {
                    val intent = Intent(requireActivity(), PhotoUploader::class.java)
                    intent.putPhotos(photos)
                    intent.putIdViolation(lastViolationId!!)
                    requireActivity().startService(intent)
                } else {
                    Toast.makeText(requireContext(), "Попробуйте еще раз", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        job.invokeOnCompletion {
            findNavController().popBackStack()
        }
    }


    override fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("photos")) {
                initRecyclerView()
                photos =
                    savedInstanceState.getParcelableArrayList<PhotoPresentationModel>("photos") as ArrayList<PhotoPresentationModel>
                adapter.submitList(photos)
            }
            lastCheckedTypeId = savedInstanceState?.getInt("lastTypeId")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttachListenerPhoto) {
            attachListener = context
            attachListener?.onAttach(this)
        }
    }

    override fun onDetach() {
        attachListener?.onDetachPhoto()
        attachListener = null
        super.onDetach()
    }

    override fun initListeners() {
        sendButton.setOnClickListener {
            startUploading()
        }
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun subscribeObservers() {
        sendPhotoViewModel.lastViolationId.observe(viewLifecycleOwner, Observer {
            lastViolationId = it
        })

        sendPhotoViewModel.violationList.observe(viewLifecycleOwner, Observer { list ->
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
    }

    override fun addPhoto() {
        ImagePicker.create(this)
            .toolbarImageTitle("Выберите изображение")
            .toolbarArrowColor(Color.WHITE)
            .includeVideo(false)
            .multi()
            .limit(10)
            .showCamera(true)
            .start(111)
    }

    override fun deletePhoto(photoItem: PhotoPresentationModel) {
        if (adapter.deletePhoto(photoItem) == 1) {
            photos.remove(photoItem)
        }
    }

    override fun openPhoto(photoItem: PhotoPresentationModel) {
        // not applicable
    }

    fun mapImages(images: List<Image>): List<PhotoPresentationModel> {
        val localImages = ArrayList<PhotoPresentationModel>()
        images.forEach {
            localImages.add(PhotoPresentationModel(it.path, it.name))
        }
        this.photos = localImages
        return localImages
    }

    override fun onPhotosResult(list: List<Image>) {
        val photos = mapImages(list)
        adapter.submitList(photos)
    }
}

interface ActivityResultHandlerPhoto {
    fun onPhotosResult(list: List<Image>)
}

interface AttachListenerPhoto {
    fun onAttach(sendFragment: ActivityResultHandlerPhoto)
    fun onDetachPhoto()
}