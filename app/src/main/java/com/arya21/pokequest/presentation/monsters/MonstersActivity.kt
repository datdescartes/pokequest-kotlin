package com.arya21.pokequest.presentation.monsters

import android.os.Bundle
import android.view.View
import com.arya21.pokequest.R
import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.presentation.common.BaseActivity
import com.arya21.pokequest.presentation.monsters.detail.MonsterDetailFragment
import com.arya21.pokequest.presentation.monsters.list.MonstersListFragment
import kotlinx.android.synthetic.main.activity_monsters.*


class MonstersActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monsters)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        supportFragmentManager.let {
            it.addOnBackStackChangedListener {
                supportActionBar?.setDisplayHomeAsUpEnabled(it.backStackEntryCount > 0)
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment, MonstersListFragment.newInstance())
                .commit()
        }
    }

    fun viewMonsterDetail(monsterInfo: Monster, shareElements: List<Pair<View, String>>?) {
        val fragment = MonsterDetailFragment.newInstance(monsterInfo, this)
        val transaction = supportFragmentManager.beginTransaction()
        shareElements?.forEach { transaction.addSharedElement(it.first, it.second) }
        transaction.replace(R.id.fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
