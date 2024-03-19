package com.judjingm.android002.common.domain

data class PagedList<T>(
    val currentPage: Int = 0,
    val content: List<T> = emptyList(),
    val totalPages: Int = 0,
    val totalElements: Int = 0
) {
    operator fun plus(newPage: PagedList<T>) = PagedList(
        content = content + newPage.content,
        currentPage = newPage.currentPage,
        totalElements = totalElements,
        totalPages = totalPages
    )

}