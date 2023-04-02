package com.gourav.competrace.app_core.data.di

import android.content.Context
import com.gourav.competrace.contests.data.AlarmScheduler
import com.gourav.competrace.contests.data.AndroidAlarmScheduler
import com.gourav.competrace.app_core.data.UserPreferences
import com.gourav.competrace.app_core.data.network.CodeforcesApiService
import com.gourav.competrace.app_core.data.network.KontestsApiService
import com.gourav.competrace.contests.data.repository.AlarmRepository
import com.gourav.competrace.app_core.util.ConnectivityObserver
import com.gourav.competrace.app_core.util.NetworkConnectivityObserver
import com.gourav.competrace.progress.user.data.repository.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences =
        UserPreferences(context)

    @Provides
    @Singleton
    fun provideAlarmRepository(@ApplicationContext context: Context): AlarmRepository =
        AlarmRepository(context)

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler =
        AndroidAlarmScheduler(context)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        NetworkConnectivityObserver(context)

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository =
        UserRepository(context)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideCodeforcesApiService(moshi: Moshi): CodeforcesApiService = Retrofit.Builder()
        .baseUrl(CodeforcesApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CodeforcesApiService::class.java)

    @Provides
    @Singleton
    fun provideKontestsApiService(moshi: Moshi): KontestsApiService = Retrofit.Builder()
        .baseUrl(KontestsApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KontestsApiService::class.java)
}