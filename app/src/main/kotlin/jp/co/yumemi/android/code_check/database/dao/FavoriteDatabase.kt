package jp.co.yumemi.android.code_check.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.co.yumemi.android.code_check.database.dao.FavoriteRepositoryDao
import jp.co.yumemi.android.code_check.database.entity.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteRepositoryDao(): FavoriteRepositoryDao
}