package com.arya21.pokequest.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSuggestMonstersUsecase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(searchText: String): List<String> =
        repository.getSuggestKeywords(searchText)
}