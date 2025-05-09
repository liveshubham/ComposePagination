package com.app.composepagination.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.app.composepagination.domain.repository.ItemRepository
import com.app.composepagination.presentation.intent.ItemIntent
import com.app.composepagination.presentation.state.ItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ItemUiState())
    val state: StateFlow<ItemUiState> = _state

    private val _intent = MutableSharedFlow<ItemIntent>()

    val pagingItems = repository.getItemsPager().cachedIn(viewModelScope)

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            _intent.collect { intent ->
                when (intent) {
                    is ItemIntent.LoadItems -> {
                        _state.update { it.copy(isLoading = true) }
                        delay(500)
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun sendIntent(intent: ItemIntent) {
        viewModelScope.launch { _intent.emit(intent) }
    }
}