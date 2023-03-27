package com.example.movies.model.di

import com.example.movies.model.remote.MovieApi
import com.example.movies.model.remote.MovieResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope((SupervisorJob()))
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope


}