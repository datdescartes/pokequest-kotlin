package com.arya21.pokequest.presentation

import com.arya21.pokequest.presentation.monsters.detail.MonsterDetailFragment
import com.arya21.pokequest.presentation.monsters.MonstersActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.arya21.pokequest.presentation.monsters.list.MonstersListFragment


@Module
abstract class BuildersModule {
    @ContributesAndroidInjector(modules = [MonstersActivityModule::class])
    abstract fun contributesMonstersActivityInjector(): MonstersActivity
}

@Module
abstract class MonstersActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMonstersListFragmentInjector(): MonstersListFragment

    @ContributesAndroidInjector
    abstract fun contributeMonsterDetailFragmentInjector(): MonsterDetailFragment
}