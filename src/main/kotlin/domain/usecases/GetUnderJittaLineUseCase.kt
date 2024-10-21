package com.adedom.domain.usecases

import com.adedom.data.datasource.local.JittaLocalDataSource
import com.adedom.data.datasource.remote.JittaRemoteDataSource
import com.adedom.data.models.constans.MarketType

class GetUnderJittaLineUseCase(
    private val jittaLocalDataSource: JittaLocalDataSource,
    private val jittaRemoteDataSource: JittaRemoteDataSource,
) {
    suspend fun execute(symbol: String): Boolean {
        val entity = jittaLocalDataSource.selectAllWhereToday().firstOrNull { it.symbol == symbol }
        if (entity != null) {
            return entity.isUnderJittaLine
        }

        return try {
            val html = jittaRemoteDataSource.fetchStock(MarketType.NASDAQ, symbol)

            val result = when {
                html.contains("Under Jitta Line") -> {
                    true
                }

                html.contains("Over Jitta Line") -> {
                    false
                }

                html.contains("Page not found!") -> {
                    jittaRemoteDataSource.fetchStock(MarketType.NYSE, symbol)
                        .contains("Under Jitta Line")
                }

                else -> {
                    false
                }
            }
            jittaLocalDataSource.insert(symbol, result)
            return result
        } catch (e: Throwable) {
            println("$symbol - ${e.message}")
            false
        }
    }
}
