package com.malibin.memo.config

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.malibin.memo.R
import com.malibin.memo.config.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MemoApplication)
            modules(diModules)
        }

        MobileAds.initialize(this, getString(R.string.admobSdkId))
    }

}
