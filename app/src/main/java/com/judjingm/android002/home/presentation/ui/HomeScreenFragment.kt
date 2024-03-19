package com.judjingm.android002.home.presentation.ui

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentHomeScreenBinding
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.home.presentation.models.StringVO
import com.judjingm.android002.home.presentation.models.state.HomeScreenEvents
import com.judjingm.android002.home.presentation.models.state.HomeScreenUiState
import com.judjingm.android002.home.presentation.models.state.UiErrorsState
import com.judjingm.android002.home.presentation.recycleView.RecycleViewPopularContentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment :
    BaseFragment<FragmentHomeScreenBinding, HomeScreenViewModel>(FragmentHomeScreenBinding::inflate) {
    override val viewModel: HomeScreenViewModel by viewModels()
    private var adapter: RecycleViewPopularContentAdapter? = null

    override fun onConfigureViews() {
        viewModel.handleEvent(HomeScreenEvents.InitializeViewModel)
        viewModel.handleEvent(HomeScreenEvents.GetPopularContent)
        recycleViewInit()
        setOnScrollForRecycleView(binding.contentRecyclerView, adapter, viewModel)
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

                override fun handleLoading(isPagination: Boolean) {
                    showLoading(isPagination)
                }

                override fun handleSuccess(content: List<PopularContentUi>) {
                    logDebugMessage("content: $content")
                    showContent(content)
                }

                override fun handleError(error: UiErrorsState) {
                    showError(StringVO.Plain("Something went wrong"))
                }
            }
        )
    }

    private fun recycleViewInit() {
        adapter = RecycleViewPopularContentAdapter { content ->

        }

        binding.contentRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.contentRecyclerView.adapter = adapter

    }

    private fun setOnScrollForRecycleView(
        recyclerView: RecyclerView,
        adapter: RecycleViewPopularContentAdapter?,
        viewModel: HomeScreenViewModel
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter?.itemCount
                    if (itemsCount != null && pos >= (itemsCount - 1)) {
                        viewModel.handleEvent(HomeScreenEvents.GetPopularContent)
                    }
                }
            }
        })
    }

    private fun showContent(content: List<PopularContentUi>) {
        showEmpty()
        adapter?.items = content
    }

    private fun showLoading(isPagination: Boolean) {
        if (isPagination) {
            binding.progressBarPagination.isVisible = true
        } else {
            showEmpty()
            binding.progressBar.isVisible = true
        }
    }

    private fun showError(text: StringVO) {
        showEmpty()
        adapter?.items = emptyList()
        binding.placeholderImage.isVisible = true
        binding.errorTextTextView.isVisible = true
        binding.errorTextTextView.text = text.value(requireContext())
    }


    private fun showEmpty() {
        binding.placeholderImage.isVisible = false
        binding.errorTextTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarPagination.isVisible = false
    }
}