package com.nullit.newpeople.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.nullit.newpeople.R
import com.nullit.newpeople.ui.auth.AuthActivity
import com.nullit.newpeople.ui.base.BaseMainFragment
import com.nullit.newpeople.util.ViewModelProviderFactory
import kotlinx.android.synthetic.main.main_fragment.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class MainFragment : BaseMainFragment(), EasyPermissions.PermissionCallbacks {

    val REQUEST_VIDEO_CAPTURE = 112

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var mainFragmentViewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainFragmentViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MainFragmentViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        EasyPermissions.requestPermissions(
            requireActivity(),
            "Для нормальной работы приложения, дайте доступ к камере",
            102,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
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
                102,
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
            checkPermsAndOpenCamera()
            findNavController().navigate(R.id.action_mainFragment_to_sendVideoFragment)
        }
        logoutButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(null, "Подтверждение")
                    .message(null, "Вы действительно хотите выйти?")
                positiveButton {
                    mainFragmentViewModel.logout()
                }
                    .negativeButton {
                        dismiss()
                    }
            }
        }
        sendPhotos.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sendFormFragment)
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
        // checkPermsAndOpenCamera()
    }

    override fun subscribeObservers() {
        // subscribe on view model here
        mainFragmentViewModel.logOutSuccess.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        })
    }

    override fun saveState(outState: Bundle) {

    }

    override fun restoreState(savedInstanceState: Bundle?) {

    }
}