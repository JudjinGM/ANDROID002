package com.judjingm.android002.content.presentation.ui

import com.judjingm.android002.common.ui.BaseViewModel
import com.judjingm.android002.home.presentation.models.ContentType

class ContentDetailViewModel(
    private val contentId: Int,
    private val contentType: ContentType
) : BaseViewModel() {

    fun getContent() {
        if (contentType == ContentType.MOVIE) {
        } else {
        }
    }
}