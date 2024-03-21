package com.judjingm.android002.content.presentation.ui

import androidx.fragment.app.viewModels
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentContentDetailBinding

class ContentDetailFragment :
    BaseFragment<FragmentContentDetailBinding, ContentDetailViewModel>(FragmentContentDetailBinding::inflate) {

    override val viewModel: ContentDetailViewModel by viewModels()
    override fun onConfigureViews() {
        TODO("Not yet implemented")
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }

}