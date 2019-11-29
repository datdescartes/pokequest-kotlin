package com.arya21.pokequest.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMonstersUsecase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): Page<Monster> =
        repository.getMonsters(offset, limit)
}