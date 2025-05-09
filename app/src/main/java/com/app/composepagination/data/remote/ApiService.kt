package com.app.composepagination.data.remote

import com.app.composepagination.data.model.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("items")
    suspend fun getItems(@Query("page") page: Int): PagedResponse
}