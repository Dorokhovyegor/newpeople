package com.nullit.newpeople.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nullit.newpeople.R
import com.nullit.newpeople.ui.base.BaseActivity
import com.nullit.newpeople.ui.main.MainActivity
import com.nullit.newpeople.util.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        authViewModel = ViewModelProvider(this, viewModelProvider)[AuthViewModel::class.java]
        authViewModel.requestUserAuthStatus()
        subscribeObservers()
        setContentView(R.layout.activity_auth)
        initListeners()
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            login()
        }
        passwordEditText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    login()
                    true
                }
                else -> false
            }
        }
    }

    private fun login() {
        authViewModel.login(
            loginEditText.text.toString().trim(),
            passwordEditText.text.toString().trim()
        )
    }

    private fun subscribeObservers() {
        authViewModel.snackBar.observe(this, Observer {
            Snackbar.make(loginEditText.rootView, it.toString(), Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
        })
        authViewModel.progressBar.observe(this, Observer {
            if (it) {
                backgroundProgress.visibility = View.VISIBLE
                circularProgress.visibility = View.VISIBLE
            } else {
                backgroundProgress.visibility = View.GONE
                circularProgress.visibility = View.GONE
            }
        })
        authViewModel.successLogin.observe(this, Observer { isAuthenticated ->
            if (isAuthenticated) {
                loginEditText.setText("")
                loginEditText.isEnabled = false
                passwordEditText.setText("")
                passwordEditText.isEnabled = false
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}