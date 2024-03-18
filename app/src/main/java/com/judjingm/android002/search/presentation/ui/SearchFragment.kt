package com.judjingm.android002.search.presentation.ui

import androidx.fragment.app.viewModels
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentSearchBinding

class SearchFragment() : BaseFragment<FragmentSearchBinding, SearchViewModel >(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()
    override fun onConfigureViews() {
    }

    override fun onSubscribe() {
    }

}