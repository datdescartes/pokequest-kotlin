package com.arya21.pokequest.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseFragment: Fragment() {
    inline fun <T> LiveData<T>.observeViewLifecycle(crossinline block: (T)->Unit) {
        observe(viewLifecycleOwner, Observer {
            block(it)
        })
    }
}