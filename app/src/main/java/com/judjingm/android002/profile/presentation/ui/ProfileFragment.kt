package com.judjingm.android002.profile.presentation.ui

import androidx.fragment.app.viewModels
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentProfileBinding

class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {
    override val viewModel: ProfileViewModel by viewModels()
    override fun onConfigureViews() {
    }

    override fun onSubscribe() {
    }

}