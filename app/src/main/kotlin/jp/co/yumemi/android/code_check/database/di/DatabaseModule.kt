package jp.co.yumemi.android.code_check.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.data.FavoriteRepository
import jp.co.yumemi.android.code_check.data.FavoriteRepositoryImpl
import jp.co.yumemi.android.code_check.database.dao.FavoriteDatabase
import jp.co.yumemi.android.code_check.database.dao.FavoriteRepositoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_database").build()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: FavoriteDatabase) = db.favoriteRepositoryDao()

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        favoriteDao: FavoriteRepositoryDao
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteDao)
    }
}