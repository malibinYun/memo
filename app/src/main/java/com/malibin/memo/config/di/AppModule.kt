package com.malibin.memo.config.di

import androidx.room.Room
import com.malibin.memo.db.AppDatabase
import com.malibin.memo.db.source.local.CategoryLocalDataSource
import com.malibin.memo.util.AsyncExecutor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val asyncExecutorModule = module {
    single { AsyncExecutor() }
}

val appDataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database").build()
    }
}

val localDataSourceModule = module {
    single {
        CategoryLocalDataSource.getInstance(get(), get<AppDatabase>().categoryDao())
    }
}

val diModules = listOf(
    asyncExecutorModule,
    appDataBaseModule,
    localDataSourceModule
)