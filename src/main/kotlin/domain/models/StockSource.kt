package com.adedom.domain.models

sealed interface StockSource {
    data class CompaniesMarketCap(
        val pageCount: Int,
    ) : StockSource

    data object Sp500 : StockSource

    data object Nasdaq : StockSource
}
