package com.judjingm.android002.upload.presentation.chooseDocument

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.judjingm.android002.databinding.FragmentChooseDocumentBinding
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentEvent
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentSideEffects
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentUiScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class ChooseDocumentFragment : BaseFragment<FragmentChooseDocumentBinding, ChooseDocumentViewModel>(
    FragmentChooseDocumentBinding::inflate
) {
    override val viewModel: ChooseDocumentViewModel by viewModels()

    private val pickDocument =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val returnCursor =
                    requireContext().contentResolver.query(uri, null, null, null, null)
                val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor?.moveToFirst()
                val name: String = nameIndex?.let { returnCursor.getString(it) } ?: BLANC_STRING
                viewModel.handleEvent(ChooseDocumentEvent.DocumentSelected(uri = uri, name = name))
            }
        }

    override fun onConfigureViews() {
        binding.chooseDocumentButton.setOnClickListener {
            viewModel.handleEvent(ChooseDocumentEvent.ChooseDocumentClicked)
        }

        binding.proceedNextButton.setOnClickListener {
            viewModel.handleEvent(ChooseDocumentEvent.ProceedNextClicked)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect { sideEffect ->
                    handleSideEffect(sideEffect)
                }
            }
        }

    }

    private fun updateUi(uiState: ChooseDocumentUiScreenState) {
        when (uiState) {
            ChooseDocumentUiScreenState.Initial -> {
                initialScreen()
            }

            is ChooseDocumentUiScreenState.Loading -> {
                loadingScreen()
            }

            is ChooseDocumentUiScreenState.Success -> {
                showSuccessScreen(uiState.name, uiState.uri)
            }
        }
    }

    private fun handleSideEffect(sideEffect: ChooseDocumentSideEffects) {
        when (sideEffect) {
            ChooseDocumentSideEffects.OpenDocumentPicker -> {
                pickDocument.launch("application/pdf")
            }

            is ChooseDocumentSideEffects.ShowMessage -> {
                showToast(sideEffect.message.value(requireContext()))
            }

            ChooseDocumentSideEffects.NavigateToNextScreen -> {
                findNavController().navigate(R.id.action_chooseDocumentFragment_to_chooseDocumentNameFragment)
            }
        }
    }

    private fun initialScreen() {
        emptyScreen()
    }

    private fun loadingScreen() {
        emptyScreen()
        binding.progressBar.isVisible = true
    }

    private fun emptyScreen() {
        binding.documentNameTextView.isVisible = false
        binding.documentImageView.isVisible = false
        binding.proceedNextButton.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showSuccessScreen(name: String, uri: Uri) {
        emptyScreen()
        if (name.isNotBlank()) {
            binding.documentNameTextView.isVisible = true
            binding.documentNameTextView.text = name
        }
        getPdfThumbnail(uri, binding.documentImageView)
        binding.proceedNextButton.isVisible = true
    }

    private fun getPdfThumbnail(uri: Uri, imageView: ImageView) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val file = uri.toFile()
                imageView.isVisible = true
                val bitmap = getBitmapFromPdf(file, 320)
                Glide.with(requireContext()).load(bitmap).into(imageView)
            } catch (e: Exception) {
                emptyScreen()
                e.printStackTrace()
                logDebugMessage("getPdfThumbnail error ${e.localizedMessage}")
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val BLANC_STRING = ""
    }

}