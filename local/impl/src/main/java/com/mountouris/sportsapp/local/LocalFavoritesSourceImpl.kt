package com.mountouris.sportsapp.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalFavoritesSourceImpl(private val db: AppDatabase) : LocalFavoritesSource {

    override fun observeFavoriteIds(): Flow<Set<String>> =
        db.favoriteDao().getFavoriteIds().map { it.toSet() }

    override suspend fun toggleFavorite(eventId: String) {
        val dao = db.favoriteDao()
        if (eventId in dao.getFavoriteIds().first()) {
            dao.removeFavorite(eventId)
        } else {
            dao.addFavorite(FavoriteEntity(eventId))
        }
    }
}
