package com.judjingm.android002.content.presentation.ui

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.content.presentation.models.ContentDetailErrorState
import com.judjingm.android002.content.presentation.models.ContentDetailEvent
import com.judjingm.android002.content.presentation.models.ContentDetailSideEffect
import com.judjingm.android002.content.presentation.models.ContentDetailUiScreenState
import com.judjingm.android002.content.presentation.models.ContentDetailsUi
import com.judjingm.android002.databinding.FragmentContentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentDetailFragment :
    BaseFragment<FragmentContentDetailBinding, ContentDetailViewModel>(FragmentContentDetailBinding::inflate) {

    override val viewModel: ContentDetailViewModel by viewModels()
    override fun onConfigureViews() {
        binding.detailToolbar.setNavigationOnClickListener {
            viewModel.handleEvent(ContentDetailEvent.OnBackClicked)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUI(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect {
                    handleSideEffect(it)
                }
            }
        }
    }

    private fun updateUI(uiState: ContentDetailUiScreenState) {
        when (uiState) {
            is ContentDetailUiScreenState.Success -> showSuccess(uiState.contentDetailUi)

            is ContentDetailUiScreenState.Error -> showError(uiState.errorState)
            ContentDetailUiScreenState.Loading -> showLoading()
        }
    }

    private fun handleSideEffect(effect: ContentDetailSideEffect) {
        when (effect) {
            ContentDetailSideEffect.NavigateBack -> findNavController().popBackStack()
        }
    }


    private fun showEmpty() {
        binding.detailProgressBar.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.errorTextView.isVisible = false
        binding.contentScrollView.isVisible = false
    }

    private fun showSuccess(contentDetailUi: ContentDetailsUi) {
        showEmpty()
        binding.contentScrollView.isVisible = true
        binding.detailToolbar.title = contentDetailUi.title
        Glide
            .with(requireContext())
            .load(IMAGE_BASE_URL + contentDetailUi.posterPath)
            .placeholder(R.drawable.content_cover_rv)
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.corner_radius
                    )
                )
            )
            .into(binding.posterImage)

        if (contentDetailUi.releaseDate.isNotBlank()) {
            binding.releaseYearTextView.text = contentDetailUi.releaseDate
        } else {
            binding.releaseYearTextView.isVisible = false
            binding.releaseYearTitleTextView.isVisible = false
        }

        if (contentDetailUi.runtime.isNotBlank()) {
            binding.runtimeYearTextView.text = contentDetailUi.runtime
        } else {
            binding.runtimeYearTextView.isVisible = false
            binding.runtimeYearTitleTextView.isVisible = false
        }
        if (contentDetailUi.genres.isNotBlank()) {
            binding.genresTextView.text = contentDetailUi.genres
        } else {
            binding.genresTextView.isVisible = false
            binding.genresTitleTextView.isVisible = false
        }

        if (contentDetailUi.cast.isNotBlank()) {
            binding.castTextView.text = contentDetailUi.cast
        } else {
            binding.castTextView.isVisible = false
            binding.castTitleTextView.isVisible = false
        }

        if (contentDetailUi.overview.isNotBlank()) {
            binding.overviewTextView.text = contentDetailUi.overview
        } else {
            binding.overviewTextView.isVisible = false
            binding.overviewTitleTextView.isVisible = false
        }
    }

    private fun showError(errorState: ContentDetailErrorState) {
        showEmpty()
        when (errorState) {
            is ContentDetailErrorState.NoConnection -> {
                binding.placeholderImage.setImageResource(R.drawable.no_signal_no_background)
                binding.errorTextView.text = errorState.message.value(requireContext())
            }

            is ContentDetailErrorState.ServerError -> {
                binding.placeholderImage.setImageResource(R.drawable.error)
                binding.errorTextView.text = errorState.message.value(requireContext())
            }

            is ContentDetailErrorState.UnknownError -> {
                binding.placeholderImage.setImageResource(R.drawable.error)
                binding.errorTextView.text = errorState.message.value(requireContext())
            }
        }
        binding.placeholderImage.isVisible = true
        binding.errorTextView.isVisible = true
    }

    private fun showLoading() {
        showEmpty()
        binding.detailProgressBar.isVisible = true
    }

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }

}