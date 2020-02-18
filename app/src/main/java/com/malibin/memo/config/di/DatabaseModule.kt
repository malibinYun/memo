package com.malibin.memo.config.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.malibin.memo.db.AppDatabase
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.source.CategoryDataSource
import com.malibin.memo.db.source.MemoDataSource
import com.malibin.memo.db.source.local.CategoryLocalDataSource
import com.malibin.memo.db.source.local.MemoLocalDataSource
import com.malibin.memo.util.AsyncExecutor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appDataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    get<AsyncExecutor>().ioThread.execute {
                        get<AppDatabase>().categoryDao().insertCategory(
                            Category("unique", "내 메모")
                        )
                    }
                }
            })
            .build()
    }
}


val localDataSourceModule = module {
    single<CategoryDataSource> {
        CategoryLocalDataSource.getInstance(get(), get<AppDatabase>().categoryDao())
    }
    single<MemoDataSource> {
        MemoLocalDataSource.getInstance(get(), get<AppDatabase>().memoDao())
    }
}

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { MemoRepository(get()) }
}