package com.judjingm.android002.home.presentation.ui

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentHomeScreenBinding
import com.judjingm.android002.home.presentation.models.PopularContentUi
import com.judjingm.android002.home.presentation.models.state.HomeScreenEvent
import com.judjingm.android002.home.presentation.models.state.HomeScreenSideEffects
import com.judjingm.android002.home.presentation.models.state.HomeScreenUiState
import com.judjingm.android002.home.presentation.models.state.PopularsErrorUiState
import com.judjingm.android002.home.presentation.recycleView.RecycleViewPopularContentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment :
    BaseFragment<FragmentHomeScreenBinding, HomeScreenViewModel>(FragmentHomeScreenBinding::inflate) {
    override val viewModel: HomeScreenViewModel by viewModels()
    private var adapter: RecycleViewPopularContentAdapter? = null

    override fun onConfigureViews() {
        recycleViewInit()
        setOnScrollForRecycleView(binding.contentRecyclerView, adapter, viewModel)
        binding.refreshButton.setOnClickListener {
            viewModel.handleEvent(HomeScreenEvent.RefreshButtonClicked)
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
                viewModel.sideEffect.collect {
                    handleSideEffects(it)
                }
            }
        }
    }

    private fun updateUi(uiState: HomeScreenUiState) {
        uiState.handleState(
            handler = object : HomeScreenUiState.StateHandler {

                override fun handleLoading(isPagination: Boolean) {
                    showLoading(isPagination)
                }

                override fun handleSuccess(content: List<PopularContentUi>) {
                    showContent(content)
                }

                override fun handleError(error: PopularsErrorUiState) {
                    when (error) {
                        is PopularsErrorUiState.CouldNotFetchData -> showError(
                            error.message.value(requireContext())
                        )

                        is PopularsErrorUiState.NoConnection -> showErrorNoConnection(
                            error.message.value(requireContext())
                        )

                        is PopularsErrorUiState.NothingFound -> showError(
                            error.message.value(requireContext())
                        )

                        is PopularsErrorUiState.UnknownError -> showError(
                            error.message.value(requireContext())
                        )
                    }

                }
            }
        )
    }

    private fun handleSideEffects(sideEffect: HomeScreenSideEffects) {
        when (sideEffect) {
            is HomeScreenSideEffects.ShowMessage -> {
                showToast(sideEffect.message.value(requireContext()))
                binding.progressBarPagination.isVisible = false
            }

            is HomeScreenSideEffects.NavigateToDetails -> {
                val direction =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToContentDetailFragment(
                        contentId = sideEffect.id,
                        contentTypeName = sideEffect.contentTypeName
                    )
                findNavController().navigate(direction)
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun recycleViewInit() {
        adapter = RecycleViewPopularContentAdapter { content ->
            viewModel.handleEvent(HomeScreenEvent.OnContentClicked(content))
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
                        viewModel.handleEvent(HomeScreenEvent.PaginationTriggered)
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

    private fun showError(text: String) {
        showEmpty()
        adapter?.items = emptyList()
        binding.placeholderImage.isVisible = true
        binding.errorTextView.isVisible = true
        binding.errorTextView.text = text
        binding.refreshButton.isVisible = true
    }

    private fun showErrorNoConnection(text: String) {
        showEmpty()
        adapter?.items = emptyList()
        binding.placeholderImage.setImageResource(com.judjingm.android002.R.drawable.no_signal_no_background)
        binding.placeholderImage.isVisible = true
        binding.errorTextView.isVisible = true
        binding.errorTextView.text = text
        binding.refreshButton.isVisible = true
    }


    private fun showEmpty() {
        binding.placeholderImage.isVisible = false
        binding.errorTextView.isVisible = false
        binding.progressBar.isVisible = false
        binding.progressBarPagination.isVisible = false
        binding.refreshButton.isVisible = false
    }
}