package com.arya21.pokequest.data

import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.domain.MonsterDetail
import com.arya21.pokequest.domain.Page
import com.arya21.pokequest.domain.PokeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokeRepositoryImpl @Inject constructor(private val api: PokeApi) : PokeRepository {
    override suspend fun getMonsters(offset: Int, limit: Int): Page<Monster> =
        api.getMonsters(offset, limit)

    override suspend fun searchMonsters(
        searchText: String,
        offset: Int,
        limit: Int
    ): Page<Monster> = api.searchMonsters(searchText, offset, limit)

    override suspend fun getSuggestKeywords(searchText: String): List<String> =
        api.getSuggestKeywords(searchText)

    override suspend fun getMonsterDetail(monsterId: Int): MonsterDetail =
        api.getMonsterDetail(monsterId)
}