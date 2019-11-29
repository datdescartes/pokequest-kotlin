package com.arya21.pokequest.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMonsterDetailUsecase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(id: Int): MonsterDetail =
        repository.getMonsterDetail(id)
}