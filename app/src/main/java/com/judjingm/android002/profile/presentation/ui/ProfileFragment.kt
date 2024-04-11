package com.judjingm.android002.profile.presentation.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseFragment
import com.judjingm.android002.databinding.FragmentProfileBinding
import com.judjingm.android002.profile.domain.models.ProfileDetails
import com.judjingm.android002.profile.presentation.models.state.ProfileEvent
import com.judjingm.android002.profile.presentation.models.state.ProfileSideEffects
import com.judjingm.android002.profile.presentation.models.state.ProfileUiScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {
    override val viewModel: ProfileViewModel by viewModels()
    override fun onConfigureViews() {
        binding.loginButton.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.OnLoginTaped)
        }

        binding.logoutButton.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.OnLogoutTaped)
        }

        binding.closeWebViewImageView.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.CloseButtonClicked)
        }
        binding.toUploadFileButton.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.OnUploadPdfButtonClicked)
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

    private fun updateUi(uiState: ProfileUiScreenState) {
        when (uiState) {
            is ProfileUiScreenState.AuthenticateInProgress -> {
                showAuthenticationWebView(uiState.url)
            }

            is ProfileUiScreenState.Initial -> {
                showInitialScreen(uiState.message.value(requireContext()))
            }

            ProfileUiScreenState.Loading -> {
                showLoadingScreen()
            }

            is ProfileUiScreenState.Success -> {
                showSuccessScreen(uiState.profileDetails)
            }
        }
    }

    private fun handleSideEffect(sideEffect: ProfileSideEffects) {
        when (sideEffect) {
            is ProfileSideEffects.ShowMessage -> {
                showToast(sideEffect.message.value(requireContext()))
            }

            ProfileSideEffects.NavigateToUploadPdf -> {
                findNavController().navigate(R.id.action_profileFragment_to_chooseDocumentFragment)
            }
        }
    }

    private fun showAuthenticationWebView(url: String) {
        emptyScreen()
        binding.closeWebViewButton.isVisible = true
        binding.webView.isVisible = true
        binding.webView.loadUrl(url)
        binding.webView.canGoForward()
        binding.webView.canGoBack()
        binding.webView.getSettings().javaScriptEnabled = true

        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (url.contains(ALLOW)) {
                    viewModel.handleEvent(ProfileEvent.RequestTokenConfirmed)
                    binding.webView.isVisible = false
                }

                if (url.contains(DENY)) {
                    viewModel.handleEvent(ProfileEvent.RequestTokenDenied)
                    binding.webView.isVisible = false
                }
                super.onPageFinished(view, url)
            }
        })
    }

    private fun showInitialScreen(message: String) {
        emptyScreen()
        binding.loginButton.isVisible = true
        binding.messageTextView.isVisible = true
        binding.messageTextView.text = message
        binding.toUploadFileButton.isVisible = true
    }

    private fun showLoadingScreen() {
        emptyScreen()
        binding.progressBar.isVisible = true
    }

    private fun showSuccessScreen(profileDetails: ProfileDetails) {
        emptyScreen()
        binding.userNameTextView.text = profileDetails.userName
        binding.userIdTextView.text = profileDetails.id.toString()
        binding.userNameTitleTextView.isVisible = true
        binding.userNameTextView.isVisible = true
        binding.userIdTitleTextView.isVisible = true
        binding.userIdTextView.isVisible = true
        binding.logoutButton.isVisible = true
        binding.toUploadFileButton.isVisible = true

    }

    private fun emptyScreen() {
        binding.userNameTitleTextView.isVisible = false
        binding.userNameTextView.isVisible = false
        binding.userIdTitleTextView.isVisible = false
        binding.userIdTextView.isVisible = false
        binding.loginButton.isVisible = false
        binding.logoutButton.isVisible = false
        binding.webView.isVisible = false
        binding.progressBar.isVisible = false
        binding.messageTextView.isVisible = false
        binding.closeWebViewButton.isVisible = false
        binding.toUploadFileButton.isVisible = false
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ALLOW = "/allow"
        const val DENY = "/deny"
    }
}