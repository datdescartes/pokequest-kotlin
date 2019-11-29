package com.arya21.pokequest.domain

/**
 * Entry point for accessing Monster data
 *
 * Created by dat.
 */

interface PokeRepository {
    /**
     * fetch monster list
     * @return Observable of list of monsters
     */
    suspend fun getMonsters(offset: Int, limit: Int): Page<Monster>

    /**
     * search monsters by keywords
     * @param searchText text to search
     * @return Observable of list of monsters
     */
    suspend fun searchMonsters(searchText: String, offset: Int, limit: Int): Page<Monster>

    /**
     * get suggest keywords
     * @param searchText text to search
     * @return Observable of list of keywords
     */
    suspend fun getSuggestKeywords(searchText: String): List<String>

    /**
     * Get detail information of a Monster
     * @param monsterId id of a monster
     * @return detail info of the monster
     */
    suspend fun getMonsterDetail(monsterId: Int): MonsterDetail
}
