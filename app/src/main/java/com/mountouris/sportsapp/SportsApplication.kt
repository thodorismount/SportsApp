package com.mountouris.sportsapp

import android.app.Application
import com.mountouris.sportsapp.di.dataModule
import com.mountouris.sportsapp.di.databaseModule
import com.mountouris.sportsapp.di.domainModule
import com.mountouris.sportsapp.di.networkModule
import com.mountouris.sportsapp.di.viewModelModule
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
