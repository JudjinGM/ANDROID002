package com.judjingm.android002.upload.presentation.chooseName

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentChooseNameBinding
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentNameEvent
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentNameSideEffects
import com.judjingm.android002.upload.presentation.models.state.ChooseDocumentNameUiScreenState
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
            if (!text.isNullOrEmpty()) {
                viewModel.handleEvent(ChooseDocumentNameEvent.OnDocumentNameChanged(text.toString()))
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.handleEvent(ChooseDocumentNameEvent.BackClicked)
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
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.sideEffect.collect {
                    handleSideEffect(it)
                }
            }
        }
    }

    private fun updateUi(uiState: ChooseDocumentNameUiScreenState) {
        when (uiState) {
            ChooseDocumentNameUiScreenState.Loading -> {}
            ChooseDocumentNameUiScreenState.Success -> {}
        }
    }

    private fun handleSideEffect(effect: ChooseDocumentNameSideEffects) {
        when (effect) {
            ChooseDocumentNameSideEffects.NavigateToNextScreen -> {
                findNavController().navigate(R.id.action_chooseDocumentNameFragment_to_confirmUploadFragment)
            }

            is ChooseDocumentNameSideEffects.SetDocumentName -> {
                binding.nameEditText.setText(effect.name)
            }

            ChooseDocumentNameSideEffects.NavigateToPreviousScreen -> {
                findNavController().popBackStack()
            }
        }
    }
}