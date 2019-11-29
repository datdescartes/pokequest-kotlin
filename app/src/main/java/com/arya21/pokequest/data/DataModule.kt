package com.arya21.pokequest.data

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import com.arya21.pokequest.domain.PokeRepository
import dagger.Binds
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [DataModule.BindsModule::class])
class DataModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, moshi: Moshi,
        @Named("baseUrl") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providePokeApi(retrofit: Retrofit): PokeApi {
        return retrofit.create(PokeApi::class.java)
    }

    @Module
    internal interface BindsModule {
        @Binds
        fun bindPokeDataSource(repo: PokeRepositoryImpl): PokeRepository
    }
}