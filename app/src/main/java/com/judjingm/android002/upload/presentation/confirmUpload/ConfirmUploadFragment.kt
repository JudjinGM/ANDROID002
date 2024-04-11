package com.judjingm.android002.upload.presentation.confirmUpload

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.bumptech.glide.Glide
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentConfirmUploadBinding
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadEvent
import com.judjingm.android002.upload.presentation.models.state.ConfirmUploadSideEffects
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
        binding.backButton.setOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.BackButtonClicked)
        }
        binding.cancelButton.setOnClickListener {
            viewModel.handleEvent(ConfirmUploadEvent.CancelButtonClicked)
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
            is ConfirmUploadUiScreenState.Success -> showContent(uiState.name, uiState.uri)
            is ConfirmUploadUiScreenState.Error -> showError(uiState.message.value(requireContext()))
        }
    }

    private fun handleSideEffect(sideEffects: ConfirmUploadSideEffects) {
        when (sideEffects) {
            ConfirmUploadSideEffects.CancelUploadConfirmation -> {
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
        binding.backButton.isVisible = true
        binding.cancelButton.isVisible = true
    }

    private fun showError(message: String) {
        emptyScreen()
        binding.errorTextView.text = message
        binding.errorTextView.isVisible = true
        binding.placeholderImage.isVisible = true
        binding.backButton.isVisible = true
        binding.cancelButton.isVisible = true
    }

    private fun emptyScreen() {
        with(binding) {
            progressBar.isVisible = false
            placeholderImage.isVisible = false
            errorTextView.isVisible = false
            documentNameTextView.isVisible = false
            documentImageView.isVisible = false
            confirmButton.isVisible = false
            backButton.isVisible = false
            cancelButton.isVisible = false
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
                logDebugMessage("setPdfImage ${e.message}")
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

}