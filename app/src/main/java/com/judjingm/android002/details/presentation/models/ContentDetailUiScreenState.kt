package com.judjingm.android002.details.presentation.models

sealed class ContentDetailUiScreenState {
    data object Loading : ContentDetailUiScreenState()
    data class Success(val contentDetailUi: ContentDetailsUi) : ContentDetailUiScreenState()
    data class Error(val errorState: ContentDetailErrorState) : ContentDetailUiScreenState()
}