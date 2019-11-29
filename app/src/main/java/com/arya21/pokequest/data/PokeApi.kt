package com.arya21.pokequest.data

import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.domain.MonsterDetail
import com.arya21.pokequest.domain.Page
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @Headers("Content-Type: application/json")
    @GET("api/pokemons/")
    suspend fun getMonsters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Page<Monster>

    @Headers("Content-Type: application/json")
    @GET("api/search/")
    suspend fun searchMonsters(
        @Query("search") searchText: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Page<Monster>

    @Headers("Content-Type: application/json")
    @GET("api/suggest/")
    suspend fun getSuggestKeywords(@Query("search") searchText: String): List<String>

    @Headers("Content-Type: application/json")
    @GET("api/pokemons/{id}/")
    suspend fun getMonsterDetail(@Path("id") id: Int): MonsterDetail
}