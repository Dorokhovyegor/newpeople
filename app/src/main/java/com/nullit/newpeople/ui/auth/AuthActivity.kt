package com.nullit.newpeople.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
            authViewModel.login(
                loginEditText.text.toString().trim(),
                passwordEditText.text.toString().trim()
            )
        }
    }

    private fun subscribeObservers() {
        authViewModel.successLogin.observe(this, Observer { isAuthenticated ->
            if (isAuthenticated) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}