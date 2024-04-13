package com.judjingm.android002.upload.presentation.confirmUpload

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentConfirmUploadBinding
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadEvent
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadSideEffects
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadUiErrorState
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadUiScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class ConfirmUploadFragment : BaseFragment<FragmentConfirmUploadBinding, ConfirmUploadViewModel>(
    FragmentConfirmUploadBinding::inflate
) {
    override val viewModel: ConfirmUploadViewModel by viewModels()
    override fun onConfigureViews() {
        binding.confirmButton.setOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.ConfirmButtonClicked)
        }
        binding.toolbar.setNavigationOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.BackButtonClicked)
        }
        binding.toHomeScreenButton.setOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.CancelButtonClicked)
        }
        binding.retryButton.setOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.RetryButtonClicked)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it)
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

    private fun updateUi(uiState: ConfirmUploadUiScreenState) {
        when (uiState) {
            ConfirmUploadUiScreenState.Loading -> showLoading()
            is ConfirmUploadUiScreenState.ReadyToUplLoad -> showContent(uiState.name, uiState.uri)
            ConfirmUploadUiScreenState.Uploading -> showUploading()
            is ConfirmUploadUiScreenState.Success -> showSuccess()
            is ConfirmUploadUiScreenState.Error -> showError(uiState.errorState)
        }
    }

    private fun handleSideEffect(sideEffects: ConfirmUploadSideEffects) {
        when (sideEffects) {
            ConfirmUploadSideEffects.NavigateToHome -> {
                findNavController().navigate(R.id.action_confirmUploadFragment_to_profileFragment)
            }

            ConfirmUploadSideEffects.NavigateBack -> {
                findNavController().popBackStack()
            }

            is ConfirmUploadSideEffects.ShowMessage -> {
                showText(sideEffects.message.value(requireContext()))
            }
        }
    }

    private fun showLoading() {
        emptyScreen()
        binding.progressBar.isVisible = true
    }

    private fun showContent(name: String, uri: Uri) {
        emptyScreen()
        binding.documentNameTextView.text = name
        binding.documentNameTextView.isVisible = true
        setPdfImage(uri, binding.documentImageView)
        binding.confirmButton.isVisible = true
        binding.toHomeScreenButton.isVisible = true
        setNavigationIconIsVisible(true)
        requireActivity().onBackPressedDispatcher
            .addCallback {
                findNavController().popBackStack()
            }

    }

    private fun showUploading() {
        emptyScreen()
        binding.statusTextView.isVisible = true
        binding.statusTextView.text = getString(R.string.uploading_pdf_to_server)
        binding.documentNameTextView.isVisible = true
        binding.documentImageView.isVisible = true
        binding.progressBar.isVisible = true
        binding.toHomeScreenButton.isVisible = true
        binding.toolbar.navigationIcon = null
        requireActivity().onBackPressedDispatcher.addCallback { }
    }

    private fun showSuccess() {
        emptyScreen()
        binding.documentNameTextView.isVisible = true
        binding.documentImageView.isVisible = true
        binding.statusTextView.isVisible = true
        binding.statusTextView.text = getString(R.string.upload_success)
        binding.toHomeScreenButton.isVisible = true
        requireActivity().onBackPressedDispatcher.addCallback { }

    }

    private fun showError(errorState: ConfirmUploadUiErrorState) {
        when (errorState) {
            is ConfirmUploadUiErrorState.FileLoadError -> {
                emptyScreen()
                binding.errorTextView.text = errorState.message.value(requireContext())
                binding.errorTextView.isVisible = true
                binding.placeholderImage.isVisible = true
                setNavigationIconIsVisible(true)
                binding.toHomeScreenButton.isVisible = true
            }

            is ConfirmUploadUiErrorState.FileUploadError -> {
                emptyScreen()
                binding.documentNameTextView.isVisible = true
                binding.documentImageView.isVisible = true
                binding.statusTextView.isVisible = true
                binding.statusTextView.text = errorState.message.value(requireContext())
                binding.retryButton.isVisible = true
                binding.toHomeScreenButton.isVisible = true
            }
        }
    }

    private fun emptyScreen() {
        with(binding) {
            placeholderImage.isVisible = false
            errorTextView.isVisible = false
            documentNameTextView.isVisible = false
            documentImageView.isVisible = false
            confirmButton.isVisible = false
            progressBar.isVisible = false
            statusTextView.isVisible = false
            retryButton.isVisible = false
            setNavigationIconIsVisible(false)
            toHomeScreenButton.isVisible = false
        }
    }

    private fun showText(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setPdfImage(uri: Uri, imageView: ImageView) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val file = uri.toFile()
                imageView.isVisible = true
                val bitmap = getBitmapFromPdf(file, 320)
                Glide.with(requireContext()).load(bitmap).into(imageView)
            } catch (e: Exception) {
                viewModel.handleEvent(ConfirmUploadEvent.ErrorShowingPDf)
            }
        }
    }

    private suspend fun getBitmapFromPdf(file: File, width: Int): Bitmap {
        var bitmap: Bitmap
        withContext(Dispatchers.IO) {
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                .use { pfd -> PdfRenderer(pfd).openPage(0) }
                .use { page ->
                    bitmap = Bitmap.createBitmap(
                        width,
                        (width.toFloat() / page.width * page.height).toInt(),
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                }
        }
        return bitmap
    }

    fun setNavigationIconIsVisible(isVisible: Boolean) {
        if (isVisible) binding.toolbar.navigationIcon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_left)
        else binding.toolbar.navigationIcon = null
    }
}