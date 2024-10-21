package com.adedom.data.datasource.remote

import com.adedom.data.datasource.providers.HttpClientProvider
import com.adedom.data.models.response.SlickChartsResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

interface SlickChartsRemoteDataSource {
    suspend fun fetchSp500(): List<SlickChartsResponse>
    suspend fun fetchNasdaq100(): List<SlickChartsResponse>
}

class SlickChartsRemoteDataSourceImpl(
    private val httpClientProvider: HttpClientProvider,
) : SlickChartsRemoteDataSource {
    override suspend fun fetchSp500(): List<SlickChartsResponse> {
        val url = "https://www.slickcharts.com/sp500"
        val html = httpClientProvider.client.get(url).bodyAsText()
        val doc: Document = Parser.parse(html, url)
        val master = doc.getElementsByClass("table table-hover table-borderless table-sm").firstOrNull()
        val body = master?.getElementsByTag("tbody")?.firstOrNull()
        val tr = body?.getElementsByTag("tr")
        return tr?.map {
            val td = it.getElementsByTag("td")
            SlickChartsResponse(
                id = td[0].text().toIntOrNull(),
                company = td[1].text().trim(),
                symbol = td[2].text().trim(),
            )
        } ?: emptyList()
    }

    override suspend fun fetchNasdaq100(): List<SlickChartsResponse> {
        val url = "https://www.slickcharts.com/nasdaq100"
        val html = httpClientProvider.client.get(url).bodyAsText()
        val doc: Document = Parser.parse(html, url)
        val body = doc.getElementById("companyListComponent")
        val tr = body?.getElementsByTag("tr")
        return tr
            ?.mapIndexedNotNull { index, element ->
                if (index % 2 == 0) element else null
            }
            ?.map {
                val td = it.getElementsByTag("td")
                SlickChartsResponse(
                    id = td[0].text().toIntOrNull(),
                    company = td[1].text().trim(),
                    symbol = td[2].text().trim(),
                )
            } ?: emptyList()
    }
}
