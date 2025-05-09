package com.app.composepagination.presentation.state

data class ItemUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)