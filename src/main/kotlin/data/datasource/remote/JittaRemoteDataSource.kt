package com.adedom.data.datasource.remote

import com.adedom.data.datasource.providers.HttpClientProvider
import com.adedom.data.models.constans.MarketType
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

interface JittaRemoteDataSource {
    suspend fun fetchStock(marketType: MarketType, symbol: String): String
}

class JittaRemoteDataSourceImpl(
    private val httpClientProvider: HttpClientProvider,
) : JittaRemoteDataSource {
    override suspend fun fetchStock(marketType: MarketType, symbol: String): String {
        val url = "https://www.jitta.com/stock/${marketType.value}:${symbol.lowercase()}"
        val html = httpClientProvider.client.get(url).bodyAsText()
        val doc: Document = Parser.parse(html, url)
        return doc.text()
    }
}
