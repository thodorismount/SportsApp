package com.mountouris.sportsapp.di

import androidx.room.Room
import com.mountouris.sportsapp.data.repository.SportsRepositoryImpl
import com.mountouris.sportsapp.domain.repository.SportsRepository
import com.mountouris.sportsapp.domain.usecase.GetSportsWithFavoritesUC
import com.mountouris.sportsapp.domain.usecase.GetSportsWithFavoritesUCImpl
import com.mountouris.sportsapp.domain.usecase.ToggleFavoriteUC
import com.mountouris.sportsapp.domain.usecase.ToggleFavoriteUCImpl
import com.mountouris.sportsapp.local.AppDatabase
import com.mountouris.sportsapp.local.LocalFavoritesSource
import com.mountouris.sportsapp.local.LocalFavoritesSourceImpl
import com.mountouris.sportsapp.presentation.SportsViewModel
import com.mountouris.sportsapp.remote.SportsApiSource
import com.mountouris.sportsapp.remote.SportsApiSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
    single<SportsApiSource> { SportsApiSourceImpl(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "sports.db"
        ).build()
    }
    single<LocalFavoritesSource> { LocalFavoritesSourceImpl(get()) }
}

val dataModule = module {
    single<SportsRepository> { SportsRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory<GetSportsWithFavoritesUC> { GetSportsWithFavoritesUCImpl(get()) }
    factory<ToggleFavoriteUC> { ToggleFavoriteUCImpl(get()) }
}

val viewModelModule = module {
    viewModel { SportsViewModel(get(), get()) }
}
