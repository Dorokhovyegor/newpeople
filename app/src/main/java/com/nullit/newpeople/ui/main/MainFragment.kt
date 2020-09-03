package com.nullit.newpeople.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nullit.newpeople.R
import com.nullit.newpeople.ui.base.BaseMainFragment
import com.nullit.newpeople.util.ViewModelProviderFactory
import kotlinx.android.synthetic.main.main_fragment.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class MainFragment : BaseMainFragment(), EasyPermissions.PermissionCallbacks {

    val REQUEST_VIDEO_CAPTURE = 112

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun checkPermsAndOpenCamera() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            EasyPermissions.requestPermissions(
                requireActivity(),
                "Для нормальной работы приложения, дайте доступ к камере",
                100,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            openCameraViaIntent()
        }
    }

    override fun initListeners() {
        // init click listeners
        sosButton.setOnClickListener {
            // do request and open video camera
            findNavController().navigate(R.id.action_mainFragment_to_sendVideoFragment)
            checkPermsAndOpenCamera()
        }
        logoutButton.setOnClickListener {
            // clear db and logout
        }
        sendPhotos.setOnClickListener {

        }
    }


    private fun openCameraViaIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // handle denied perms
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        checkPermsAndOpenCamera()
    }

    override fun subscribeObservers() {
        // subscribe on view model here
    }

    override fun saveState(outState: Bundle) {

    }

    override fun restoreState(savedInstanceState: Bundle?) {

    }
}