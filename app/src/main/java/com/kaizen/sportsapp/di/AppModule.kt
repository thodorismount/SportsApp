package com.kaizen.sportsapp.di

import androidx.room.Room
import com.kaizen.sportsapp.data.repository.SportsRepositoryImpl
import com.kaizen.sportsapp.domain.repository.SportsRepository
import com.kaizen.sportsapp.domain.usecase.GetSportsWithFavoritesUC
import com.kaizen.sportsapp.domain.usecase.GetSportsWithFavoritesUCImpl
import com.kaizen.sportsapp.domain.usecase.ToggleFavoriteUC
import com.kaizen.sportsapp.domain.usecase.ToggleFavoriteUCImpl
import com.kaizen.sportsapp.local.AppDatabase
import com.kaizen.sportsapp.local.LocalFavoritesSource
import com.kaizen.sportsapp.local.LocalFavoritesSourceImpl
import com.kaizen.sportsapp.presentation.SportsViewModel
import com.kaizen.sportsapp.remote.SportsApiSource
import com.kaizen.sportsapp.remote.SportsApiSourceImpl
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
