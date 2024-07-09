package jp.co.yumemi.android.code_check.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.yumemi.android.code_check.database.entity.Favorite

@Dao
interface FavoriteRepositoryDao {
    @Query("SELECT * FROM favorite_table")
    fun getFavorites(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)
}