package com.app.composepagination.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.composepagination.data.model.Item
import com.app.composepagination.data.paging.ItemPagingSource
import com.app.composepagination.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor(private val api: ApiService) {
    fun getItemsPager(): Flow<PagingData<Item>> {
        return Pager(PagingConfig(pageSize = 10)) {
            ItemPagingSource(api)
        }.flow
    }
}