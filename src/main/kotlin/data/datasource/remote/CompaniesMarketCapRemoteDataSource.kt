package com.adedom.data.datasource.remote

import com.adedom.data.datasource.providers.HttpClientProvider
import com.adedom.data.models.response.CompaniesMarketCapResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

interface CompaniesMarketCapRemoteDataSource {
    suspend fun fetchCompaniesMarketCap(page: Int): List<CompaniesMarketCapResponse>
}

class CompaniesMarketCapRemoteDataSourceImpl(
    private val httpClientProvider: HttpClientProvider,
) : CompaniesMarketCapRemoteDataSource {
    override suspend fun fetchCompaniesMarketCap(page: Int): List<CompaniesMarketCapResponse> {
        val url = "https://companiesmarketcap.com/page/$page"
        val html = httpClientProvider.client.get(url).bodyAsText()
        val doc: Document = Parser.parse(html, url)
        val master = doc.getElementsByClass("default-table table marketcap-table dataTable").firstOrNull()
        val body = master?.getElementsByTag("tbody")?.firstOrNull()
        val tr = body?.getElementsByTag("tr")
        return tr
            ?.mapNotNull { element ->
                val adsSize = element.getElementsByClass("ad-tr no-sort").size
                if (adsSize == 0) element else null
            }
            ?.map {
                val td = it.getElementsByTag("td")
                val companyName = it.getElementsByClass("company-name").firstOrNull()
                val companyCode = it.getElementsByClass("company-code").firstOrNull()
                CompaniesMarketCapResponse(
                    rank = td[1].text().toIntOrNull(),
                    company = companyName?.text()?.trim(),
                    symbol = companyCode?.text()?.trim(),
                    marketCap = td[3].text().trim(),
                    country = td[7].text().trim(),
                )
            } ?: emptyList()
    }
}
