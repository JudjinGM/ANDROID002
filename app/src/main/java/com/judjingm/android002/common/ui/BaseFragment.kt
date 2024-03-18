package com.judjingm.android002.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Base Fragment class with View Binding support, works with BaseViewModel.
 *
 * While implementing this class, you need to override: configureViews() and subscribe().
 *
 * Also need to implement viewModel property by override viewmodel field:
 *
 * val viewModel by viewModels&lt;SomeViewModel&gt;()
 *
 */
abstract class BaseFragment<VB: ViewBinding, VM: BaseViewModel> (
    private val inflate: Inflate<VB>
): Fragment() {

        private var _binding: VB? = null
        protected val binding: VB get() = _binding!!

        abstract val viewModel: VM

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: android.os.Bundle?
        ): android.view.View? {
            _binding = inflate.invoke(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            onConfigureViews()
            onSubscribe()
        }

        abstract fun onConfigureViews()

        abstract fun onSubscribe()

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}