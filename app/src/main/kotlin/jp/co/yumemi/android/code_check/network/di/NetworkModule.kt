package jp.co.yumemi.android.code_check.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.data.GithubRepository
import jp.co.yumemi.android.code_check.data.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.network.service.GithubClient
import jp.co.yumemi.android.code_check.network.service.GithubService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubService(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubClient(githubService: GithubService): GithubClient {
        return GithubClient(githubService)
    }

    @Provides
    @Singleton
    fun provideGithubRepository(githubClient: GithubClient): GithubRepository {
        return GithubRepositoryImpl(githubClient)
    }
}