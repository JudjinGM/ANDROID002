package com.judjingm.android002.home.presentation.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentHomeScreenBinding
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.home.presentation.models.state.HomeScreenEvents
import com.judjingm.android002.home.presentation.models.state.HomeScreenUiState
import com.judjingm.android002.home.presentation.models.state.UiErrorsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment :
    BaseFragment<FragmentHomeScreenBinding, HomeScreenViewModel>(FragmentHomeScreenBinding::inflate) {
    override val viewModel: HomeScreenViewModel by viewModels()
    override fun onConfigureViews() {
        viewModel.handleEvent(HomeScreenEvents.InitializeViewModel)
        viewModel.handleEvent(HomeScreenEvents.GetPopularContent)
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
                viewModel.sideEffect.collect {
                    //     handleSideEffects(it)
                }
            }
        }

        viewModel.handleEvent(HomeScreenEvents.InitializeViewModel)
    }

    private fun updateUi(uiState: HomeScreenUiState) {
        uiState.state.handleState(
            handler = object : HomeScreenUiState.StateHandler {
                override fun handleLoading() {
                    logDebugMessage("LOADING")
                }

                override fun handleSuccess(content: List<PopularContentUi>) {
                    logDebugMessage("content: $content")
                }

                override fun handleError(error: UiErrorsState) {
                    when (error) {
                        UiErrorsState.CouldNotFetchData -> logDebugMessage("error: CouldNotFetchData")
                        UiErrorsState.NoConnection -> logDebugMessage("error: NoConnection")
                        UiErrorsState.NothingFound -> logDebugMessage("error: NothingFound")
                        UiErrorsState.UnknownError -> logDebugMessage("error: UnknownError")
                    }
                }
            }
        )
    }

}