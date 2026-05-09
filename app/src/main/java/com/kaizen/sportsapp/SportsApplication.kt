package com.kaizen.sportsapp

import android.app.Application
import com.kaizen.sportsapp.di.dataModule
import com.kaizen.sportsapp.di.databaseModule
import com.kaizen.sportsapp.di.domainModule
import com.kaizen.sportsapp.di.networkModule
import com.kaizen.sportsapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SportsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SportsApplication)
            modules(
                networkModule,
                databaseModule,
                dataModule,
                domainModule,
                viewModelModule
            )
        }
    }
}
