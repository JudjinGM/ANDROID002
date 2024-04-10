package com.judjingm.android002.upload.presentation.choseDocument

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentUploadBinding
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentEvent
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentSideEffects
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentUiScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseDocumentFragment : BaseFragment<FragmentUploadBinding, ChooseDocumentViewModel>(FragmentUploadBinding::inflate) {
    override val viewModel: ChooseDocumentViewModel by viewModels()

    private val pickDocument =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.handleEvent(ChooseDocumentEvent.DocumentSelected(uri))
            }
        }

    override fun onConfigureViews() {
        binding.chooseDocumentButton.setOnClickListener {
            viewModel.handleEvent(ChooseDocumentEvent.ChooseDocumentClicked)
        }

        binding.uploadDocumentButton.setOnClickListener {
            viewModel.handleEvent(ChooseDocumentEvent.ChooseDocumentDocumentClicked)
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

            }

            is ChooseDocumentUiScreenState.Loading -> {

            }

            is ChooseDocumentUiScreenState.Success.Chosen -> {

            }

            is ChooseDocumentUiScreenState.Success.Uploaded -> {

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
        }
    }

    private fun initialScreen() {
        emptyScreen()
        binding.chooseDocumentButton.isVisible = true
    }

    private fun emptyScreen() {
        binding.chooseDocumentButton.isVisible = false
        binding.documentNameTextView.isVisible = false
        binding.documentImageView.isVisible = false
        binding.uploadDocumentButton.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}