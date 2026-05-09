package com.kaizen.sportsapp.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun favoriteDao(): FavoriteDao
}
