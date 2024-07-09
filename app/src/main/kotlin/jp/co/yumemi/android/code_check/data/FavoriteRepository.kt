package jp.co.yumemi.android.code_check.data

import jp.co.yumemi.android.code_check.database.dao.FavoriteRepositoryDao
import jp.co.yumemi.android.code_check.database.entity.Favorite
import javax.inject.Inject

interface FavoriteRepository {

    fun getAllFavorites(): List<Favorite>

    suspend fun insertFavorites(favorite: Favorite)

    suspend fun deleteFavorites(favorite: Favorite)
}

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteRepositoryDao: FavoriteRepositoryDao
): FavoriteRepository {
    override fun getAllFavorites(): List<Favorite> = favoriteRepositoryDao.getFavorites()

    override suspend fun insertFavorites(favorite: Favorite) {
        favoriteRepositoryDao.insert(favorite)
    }

    override suspend fun deleteFavorites(favorite: Favorite) {
        favoriteRepositoryDao.delete(favorite)
    }
}