package com.judjingm.android002.common.domain

data class PagedList<T> (
    val currentPage: Int?,
    val content: List<T>,
    val totalPages: Int?,
    val totalElements: Int?
){
    operator fun plus(newPage: PagedList<T>) = PagedList(
        content = content + newPage.content,
        currentPage = newPage.currentPage,
        totalElements = totalElements,
        totalPages = totalPages
    )

}