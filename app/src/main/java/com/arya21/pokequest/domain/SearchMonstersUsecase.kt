package com.arya21.pokequest.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMonstersUsecase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(query: String, offset: Int, limit: Int): Page<Monster> =
        repository.searchMonsters(query, offset, limit)
}