package com.adedom.domain.usecases

import com.adedom.data.datasource.remote.FearAndGreedRemoteDataSource
import com.adedom.data.models.response.Now

class GetFearAndGreedIndexUseCase(
    private val fearAndGreedRemoteDataSource: FearAndGreedRemoteDataSource,
) {
    suspend fun execute(): Now? {
        return fearAndGreedRemoteDataSource.fetchFearAndGreedIndex().fgi?.now
    }
}