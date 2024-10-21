package com.adedom.domain.usecases

import com.adedom.data.datasource.remote.CompaniesMarketCapRemoteDataSource
import com.adedom.data.models.response.CompaniesMarketCapResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetCompaniesMarketCapUseCase(
    private val companiesMarketCapRemoteDataSource: CompaniesMarketCapRemoteDataSource,
) {
    suspend fun execute(pageCount: Int): List<String> = coroutineScope {
        return@coroutineScope (1..pageCount)
            .map {
                async {
                    companiesMarketCapRemoteDataSource.fetchCompaniesMarketCap(it)
                }
            }
            .awaitAll()
            .flatten()
            .map(CompaniesMarketCapResponse::symbol)
            .mapNotNull { it }
    }
}
