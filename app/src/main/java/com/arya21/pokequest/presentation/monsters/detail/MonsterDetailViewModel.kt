package com.arya21.pokequest.presentation.monsters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arya21.pokequest.core.SingleLiveEvent
import com.arya21.pokequest.domain.GetMonsterDetailUsecase
import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.domain.MonsterDetail
import com.arya21.pokequest.presentation.common.BaseViewModel
import com.arya21.pokequest.presentation.common.LoadingState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MonsterDetailViewModel @Inject constructor(
    private val getMonsterDetail: GetMonsterDetailUsecase
) : BaseViewModel() {
    private val monsterDetailLiveData = MutableLiveData<MonsterDetail>()
    private val loadingStateLiveData = MutableLiveData<LoadingState>()
    private val networkErrorLiveData = SingleLiveEvent<Exception?>()

    val loadingState: LiveData<LoadingState> = loadingStateLiveData
    val networkError: LiveData<Exception?> = networkErrorLiveData
    val monsterDetail: LiveData<MonsterDetail> = monsterDetailLiveData

    fun loadMonsterDetail(monsterId: Int) {
        launch {
            loadingStateLiveData.value = LoadingState.LOADING
            try {
                val monsterDetail = runIO { getMonsterDetail(monsterId) }
                monsterDetailLiveData.value = monsterDetail
            } catch (e: Exception) {
                networkErrorLiveData.value = e
                when (e) {
                    is IOException -> { // Network error
                        Timber.d(
                            e,
                            "Network Error when trying to get monster id: $monsterId"
                        )
                    }
                    is HttpException -> { // Server error
                        Timber.w(
                            e,
                            "Http Error when trying to get monster id: $monsterId"
                        )
                    }
                    else -> { // Client error
                        Timber.e(
                            e,
                            "Error when trying to get monster id: $monsterId"
                        )
                    }
                }
            }
            loadingStateLiveData.value = LoadingState.IDLE
        }
    }
}
