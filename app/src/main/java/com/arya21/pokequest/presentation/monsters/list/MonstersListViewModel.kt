package com.arya21.pokequest.presentation.monsters.list

import androidx.lifecycle.*
import com.arya21.pokequest.core.SingleLiveEvent
import com.arya21.pokequest.domain.*
import com.arya21.pokequest.presentation.common.BaseViewModel
import com.arya21.pokequest.presentation.common.LoadingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MonstersListViewModel @Inject constructor(
    private val getMonstersList: GetMonstersUsecase,
    private val searchMonsters: SearchMonstersUsecase,
    private val getSuggestMonsters: GetSuggestMonstersUsecase
) : BaseViewModel() {
    private val monstersLiveData = MutableLiveData<Pair<List<Monster>, Boolean>>()
    private val loadingStateLiveData = MutableLiveData<LoadingState>()
    private val networkErrorLiveData = SingleLiveEvent<Exception?>()

    private val query = MutableLiveData<String>()

    val suggestMonsters: LiveData<List<String>> = MediatorLiveData<List<String>>().apply {
        addSource(query, object : Observer<String> {
            var job: Job? = null
            override fun onChanged(text: String) {
                job?.cancel()
                if (text.length < 2) {
                    value = listOf()
                } else job = launch {
                    try {
                        val res = runIO { getSuggestMonsters(text) }
                        value = res
                    } catch (e: Exception) {
                        when (e) {
                            is IOException -> { // Network error
                                Timber.d(
                                    e,
                                    "Network Error when trying to get suggestions text=$text"
                                )
                            }
                            is HttpException -> { // Server error
                                Timber.w(e, "Http Error when trying to get suggestions text=$text")
                            }
                            else -> { // Client error
                                Timber.e(e, "Error when trying to get suggestions text=$text")
                            }
                        }
                    }
                    job = null
                }
            }
        })
    }

    val currentQueryText: String?
        get() = query.value
    val monsters: LiveData<Pair<List<Monster>, Boolean>> = monstersLiveData
    val loadingState: LiveData<LoadingState> = loadingStateLiveData
    val networkError: LiveData<Exception?> = networkErrorLiveData
    var hasMore = false
        private set

    /**
     * Load monsters list
     */
    fun loadMonsters(isLoadmore: Boolean = false) {
        launch {
            loadingStateLiveData.value =
                if (isLoadmore) LoadingState.LOADING_MORE else LoadingState.LOADING
            val offset = if (isLoadmore) monsters.value?.first?.size ?: 0 else 0
            val limit = 30
            val searchText = query.value
            try {
                val monstersPage = if (searchText.isNullOrBlank()) {
                    runIO { getMonstersList(offset, limit) }
                } else {
                    runIO { searchMonsters(searchText, offset, limit) }
                }
                hasMore = monstersPage.hasMore()
                val list = if (isLoadmore)
                    monstersLiveData.value?.first?.toMutableList() ?: mutableListOf()
                else mutableListOf()
                list.addAll(monstersPage.data)
                monstersLiveData.value = list to isLoadmore
            } catch (e: Exception) {
                networkErrorLiveData.value = e
                when (e) {
                    is IOException -> { // Network error
                        Timber.d(
                            e,
                            "Network Error when trying to get monsters offset $offset, limit $limit"
                        )
                    }
                    is HttpException -> { // Server error
                        Timber.w(
                            e,
                            "Http Error when trying to get monsters offset $offset, limit $limit"
                        )
                    }
                    else -> { // Client error
                        Timber.e(
                            e,
                            "Error when trying to get monsters offset $offset, limit $limit"
                        )
                    }
                }
            }
            loadingStateLiveData.value = LoadingState.IDLE
        }
    }

    fun searchTextChanged(newText: String?) {
        query.value = newText ?: ""
    }
}
