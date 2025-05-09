package com.app.composepagination.data.model

data class PagedResponse(
    val data: List<Item>,
    val total_pages: Int,
    val page: Int
)
