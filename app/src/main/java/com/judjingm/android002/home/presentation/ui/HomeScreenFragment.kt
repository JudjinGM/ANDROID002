package com.judjingm.android002.home.presentation.ui

import androidx.fragment.app.viewModels
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentHomeScreenBinding

class HomeScreenFragment :
    BaseFragment<FragmentHomeScreenBinding, HomeScreenViewModel>(FragmentHomeScreenBinding::inflate) {
    override val viewModel: HomeScreenViewModel by viewModels()
    override fun onConfigureViews() {

    }

    override fun onSubscribe() {

    }
}