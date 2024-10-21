package com.adedom

import com.adedom.data.database.table.JittaSqliteTable
import com.adedom.di.appModule
import com.adedom.domain.models.StockSource
import com.adedom.domain.usecases.GetFearAndGreedIndexUseCase
import com.adedom.domain.usecases.GetStockUseCase
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

fun main() = runBlocking {
    startKoin {
        modules(appModule)
    }

    Database.connect("jdbc:sqlite:./data/invest.db", driver = "org.sqlite.JDBC")
    newSuspendedTransaction {
        SchemaUtils.create(JittaSqliteTable)
    }

    println("AdeDom :: BEGIN")
    val getFearAndGreedIndexUseCase: GetFearAndGreedIndexUseCase by inject(GetFearAndGreedIndexUseCase::class.java)
    val getStockUseCase: GetStockUseCase by inject(GetStockUseCase::class.java)

    // Fear & Greed Index
    val fearAndGreedIndex = getFearAndGreedIndexUseCase.execute()
    println("AdeDom :: Fear & Greed Index - ${fearAndGreedIndex?.valueText}(${fearAndGreedIndex?.value})")

    // Stock
    val stockSources = listOf(
        StockSource.CompaniesMarketCap(1),
        StockSource.Sp500,
        StockSource.Nasdaq,
    )
    val result = getStockUseCase.execute(stockSources, true)
    result.forEachIndexed { index, s ->
        println("AdeDom :: ${index.plus(1)} $s")
    }

    println("AdeDom :: END")
}
