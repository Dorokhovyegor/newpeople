package com.nullit.newpeople.ui.base

import android.os.Bundle
import android.view.View
import com.nullit.newpeople.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseMainFragment: DaggerFragment() {

    abstract fun saveState(outState: Bundle)
    abstract fun restoreState(savedInstanceState: Bundle?)
    abstract fun initListeners()
    abstract fun subscribeObservers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreState(savedInstanceState)
        initListeners()
        subscribeObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }
}