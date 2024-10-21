package com.adedom.data.datasource.remote

import com.adedom.data.datasource.providers.HttpClientProvider
import com.adedom.data.models.response.FearAndGreedIndexResponse
import io.ktor.client.call.*
import io.ktor.client.request.*

interface FearAndGreedRemoteDataSource {
    suspend fun fetchFearAndGreedIndex(): FearAndGreedIndexResponse
}

class FearAndGreedRemoteDataSourceImpl(
    private val httpClientProvider: HttpClientProvider,
) : FearAndGreedRemoteDataSource {
    override suspend fun fetchFearAndGreedIndex(): FearAndGreedIndexResponse {
        return httpClientProvider.client
            .get("https://fear-and-greed-index.p.rapidapi.com/v1/fgi") {
                header("x-rapidapi-host", "fear-and-greed-index.p.rapidapi.com")
                header("x-rapidapi-key", "2a61cb8507mshe1dbae8eecc38c2p179a95jsn2281e796ce67")
            }
            .body()
    }
}
