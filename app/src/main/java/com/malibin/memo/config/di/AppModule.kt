package com.malibin.memo.config.di

import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.util.AsyncExecutor
import com.malibin.memo.util.ViewModelFactory
import org.koin.dsl.module

val asyncExecutorModule = module {
    single { AsyncExecutor() }
}

val viewModelFactoryModule = module {
    single<ViewModelProvider.Factory> { ViewModelFactory(get(), get()) }
}

val diModules = listOf(
    asyncExecutorModule,
    appDataBaseModule,
    localDataSourceModule,
    repositoryModule,
    viewModelFactoryModule
)