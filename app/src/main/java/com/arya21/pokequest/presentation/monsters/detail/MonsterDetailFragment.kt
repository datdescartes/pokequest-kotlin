package com.arya21.pokequest.presentation.monsters.detail

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.arya21.pokequest.R
import com.arya21.pokequest.databinding.MonsterDetailFragmentBinding
import com.arya21.pokequest.di.Injectable
import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.presentation.common.BaseFragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import javax.inject.Inject

class MonsterDetailFragment : BaseFragment(), Injectable {

    companion object {
        private const val ARG_MONSTER_INFO = "ARG_MONSTER_INFO"

        fun newInstance(monsterInfo: Monster, context: Context) = MonsterDetailFragment().apply {
            arguments = bundleOf(ARG_MONSTER_INFO to monsterInfo)
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(R.transition.move)
            sharedElementReturnTransition =
                TransitionInflater.from(context).inflateTransition(R.transition.move)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MonsterDetailViewModel by viewModels { viewModelFactory }

    private lateinit var binding: MonsterDetailFragmentBinding

    private val monsterInfo: Monster by lazy {
        requireArguments().getParcelable<Monster>(ARG_MONSTER_INFO)
    }

    private var handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.title = monsterInfo.name.toUpperCase()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_monster_detail,
            container,
            false
        )
        binding.imageRequestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

        }
        handler.postDelayed(1000) {
            startPostponedEnterTransition()
        }
        binding.monsterInfo = monsterInfo
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.monsterDetail.observeViewLifecycle {
            val infos: List<Pair<String, String>> = listOf(
                "National No." to it.id.toString(),
                "Species" to it.species,
                "Height" to it.height,
                "Weight" to it.weight,
                "Abilities" to it.abilities.joinToString(", "),
                "EV yield" to it.evYield,
                "Catch rate" to it.catchRate.toString(),
                "Base EXP" to it.exp.toString(),
                "Growth Rate" to it.growthRate,
                "Gender" to it.maleFemaleRatio,
                "Egg cycles" to it.eggCycles.toString()
            )
            val baseStats: List<Pair<String, Int>> = listOf(
                "HP" to it.hp,
                "Attack" to it.attack,
                "Defense" to it.defense,
                "Sp. Atk" to it.spAtk,
                "Sp. Def" to it.spDef,
                "Speed" to it.speed
            )
            binding.infos = infos
            binding.baseStats = baseStats
        }

        if (viewModel.monsterDetail.value == null) {
            viewModel.loadMonsterDetail(monsterInfo.id)
        }

        viewModel.loadingState.observeViewLifecycle {
            binding.loadingState = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMonsterDetail(monsterInfo.id)
        }

        viewModel.networkError.observeViewLifecycle {
            context?.let {
                Toast.makeText(it, "Network Error!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
