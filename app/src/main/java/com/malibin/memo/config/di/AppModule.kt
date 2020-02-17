package com.malibin.memo.config.di

import com.malibin.memo.util.AsyncExecutor
import com.malibin.memo.util.ViewModelFactory
import org.koin.dsl.module

val asyncExecutorModule = module {
    single { AsyncExecutor() }
}

val viewModelFactory = module {
    single { ViewModelFactory(get(), get()) }
}

val diModules = listOf(
    asyncExecutorModule,
    appDataBaseModule,
    localDataSourceModule,
    repositoryModule,
    viewModelFactory
)