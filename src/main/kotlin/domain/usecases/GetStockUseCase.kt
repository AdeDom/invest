package com.adedom.domain.usecases

import com.adedom.data.datasource.remote.SlickChartsRemoteDataSource
import com.adedom.data.models.response.SlickChartsResponse
import com.adedom.domain.models.StockSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetStockUseCase(
    private val slickChartsRemoteDataSource: SlickChartsRemoteDataSource,
    private val getCompaniesMarketCapUseCase: GetCompaniesMarketCapUseCase,
    private val getUnderJittaLineUseCase: GetUnderJittaLineUseCase,
) {
    suspend fun execute(stockSources: List<StockSource>, isJitta: Boolean): List<String> = coroutineScope {
        val source = stockSources
            .map {
                async {
                    when (it) {
                        is StockSource.CompaniesMarketCap -> {
                            getCompaniesMarketCapUseCase.execute(it.pageCount)
                        }

                        StockSource.Sp500 -> {
                            slickChartsRemoteDataSource.fetchSp500()
                                .map(SlickChartsResponse::symbol)
                                .mapNotNull { it }
                        }

                        StockSource.Nasdaq -> {
                            slickChartsRemoteDataSource.fetchNasdaq100()
                                .map(SlickChartsResponse::symbol)
                                .mapNotNull { it }
                        }
                    }
                }
            }
            .awaitAll()

        var symbols = listOf<String>()
        List(source.size) { index ->
            symbols = if (index == 0) {
                source[0]
            } else {
                symbols.intersect(source[index].toSet()).toList()
            }
        }

        if (isJitta) {
            symbols
                .map {
                    async {
                        Pair(it, getUnderJittaLineUseCase.execute(it))
                    }
                }
                .awaitAll()
                .filter { it.second }
                .map { it.first }
        } else {
            symbols
        }
    }
}
