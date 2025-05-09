package com.app.composepagination.presentation.intent

sealed class ItemIntent {
    data object LoadItems : ItemIntent()
}
