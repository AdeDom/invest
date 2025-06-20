package com.adedom.di

import com.adedom.data.datasource.local.JittaLocalDataSource
import com.adedom.data.datasource.local.JittaLocalDataSourceImpl
import com.adedom.data.datasource.providers.HttpClientProvider
import com.adedom.data.datasource.providers.HttpClientProviderImpl
import com.adedom.data.datasource.remote.CompaniesMarketCapRemoteDataSource
import com.adedom.data.datasource.remote.CompaniesMarketCapRemoteDataSourceImpl
import com.adedom.data.datasource.remote.FearAndGreedRemoteDataSource
import com.adedom.data.datasource.remote.FearAndGreedRemoteDataSourceImpl
import com.adedom.data.datasource.remote.JittaRemoteDataSource
import com.adedom.data.datasource.remote.JittaRemoteDataSourceImpl
import com.adedom.data.datasource.remote.SlickChartsRemoteDataSource
import com.adedom.data.datasource.remote.SlickChartsRemoteDataSourceImpl
import com.adedom.domain.usecases.GetCompaniesMarketCapUseCase
import com.adedom.domain.usecases.GetFearAndGreedIndexUseCase
import com.adedom.domain.usecases.GetStockUseCase
import com.adedom.domain.usecases.GetUnderJittaLineUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule =
    module {
        // provider
        singleOf(::HttpClientProviderImpl) { bind<HttpClientProvider>() }

        // local data source
        singleOf(::JittaLocalDataSourceImpl) { bind<JittaLocalDataSource>() }

        // remote data source
        singleOf(::FearAndGreedRemoteDataSourceImpl) { bind<FearAndGreedRemoteDataSource>() }
        singleOf(::CompaniesMarketCapRemoteDataSourceImpl) { bind<CompaniesMarketCapRemoteDataSource>() }
        singleOf(::SlickChartsRemoteDataSourceImpl) { bind<SlickChartsRemoteDataSource>() }
        singleOf(::JittaRemoteDataSourceImpl) { bind<JittaRemoteDataSource>() }

        // domain
        factoryOf(::GetFearAndGreedIndexUseCase)
        factoryOf(::GetUnderJittaLineUseCase)
        factoryOf(::GetStockUseCase)
        factoryOf(::GetCompaniesMarketCapUseCase)
    }
