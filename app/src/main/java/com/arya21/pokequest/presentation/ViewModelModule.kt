package com.arya21.pokequest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arya21.pokequest.di.ViewModelFactory
import com.arya21.pokequest.presentation.monsters.detail.MonsterDetailViewModel
import com.arya21.pokequest.presentation.monsters.list.MonstersListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MonstersListViewModel::class)
    abstract fun bindMonstersListViewModel(viewModel: MonstersListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonsterDetailViewModel::class)
    abstract fun bindMonsterDetailViewModel(viewModel: MonsterDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)