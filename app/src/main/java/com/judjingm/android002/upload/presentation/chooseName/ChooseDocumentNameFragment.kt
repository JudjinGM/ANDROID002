package com.judjingm.android002.upload.presentation.chooseName

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentChooseNameBinding
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameEvent
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameSideEffects
import com.judjingm.android002.upload.presentation.models.chooseDocument.ChooseDocumentNameUiScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseDocumentNameFragment :
    BaseFragment<FragmentChooseNameBinding, ChooseDocumentNameViewModel>(
        FragmentChooseNameBinding::inflate
    ) {
    override val viewModel: ChooseDocumentNameViewModel by viewModels()
    override fun onConfigureViews() {
        binding.proceedNextButton.setOnClickListener {
            viewModel.handleEvent(ChooseDocumentNameEvent.ProceedNextClicked)
        }
        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                binding.nameEditText.setText(text)
                viewModel.handleEvent(ChooseDocumentNameEvent.OnDocumentNameChanged(text.toString()))
            }
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

    private fun updateUi(uiState: ChooseDocumentNameUiScreenState) {
        when (uiState) {
            ChooseDocumentNameUiScreenState.Loading -> {

            }

            is ChooseDocumentNameUiScreenState.Success -> {
                binding.nameEditText.setText(uiState.name)
            }
        }
    }

    private fun handleSideEffect(effect: ChooseDocumentNameSideEffects) {
        when (effect) {
            ChooseDocumentNameSideEffects.NavigateToNextScreen -> {

            }
        }
    }
}