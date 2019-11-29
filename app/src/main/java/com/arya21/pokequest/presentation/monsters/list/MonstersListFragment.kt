package com.arya21.pokequest.presentation.monsters.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.arya21.pokequest.R
import com.arya21.pokequest.core.GridAutofitLayoutManager
import com.arya21.pokequest.core.SimpleDataBoundListAdapter
import com.arya21.pokequest.databinding.MonsterItemBinding
import com.arya21.pokequest.databinding.MonstersListFragmentBinding
import com.arya21.pokequest.di.Injectable
import com.arya21.pokequest.domain.Monster
import com.arya21.pokequest.presentation.common.BaseFragment
import com.arya21.pokequest.presentation.common.LoadingState
import com.arya21.pokequest.presentation.monsters.MonstersActivity
import javax.inject.Inject


class MonstersListFragment : BaseFragment(), Injectable {

    companion object {
        fun newInstance() =
            MonstersListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MonstersListViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: SimpleDataBoundListAdapter<Monster, MonsterItemBinding>

    private lateinit var binding: MonstersListFragmentBinding

    private var searchView: SearchView? = null

    private lateinit var suggestAdapter: SuggestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = "PokéQuest"
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_monsters_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        suggestAdapter =
            SuggestAdapter(
                context
            )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMonsters(false)
        }

        adapter = SimpleDataBoundListAdapter(R.layout.rv_item_monster) { item, binding ->
            exitTransition = Fade()
            (activity as? MonstersActivity)?.viewMonsterDetail(
                item, listOf(
                    binding.ivThumbnail to item.id.toString(),
                    binding.tvName to "${item.id}${item.name}"
                )
            )
        }
        binding.rvMonsters.adapter = adapter
        binding.rvMonsters.layoutManager = GridAutofitLayoutManager(
            context!!,
            resources.getDimension(R.dimen.list_item_monster_mind_width).toInt()
        )

        viewModel.loadingState.observeViewLifecycle {
            binding.loadingState = it
        }
        viewModel.monsters.observeViewLifecycle {
            adapter.submitList(it.first)
            if (!it.second) {
                (binding.rvMonsters.layoutManager as? LinearLayoutManager)?.scrollToPosition(0)
            }
            searchView?.clearFocus()
        }

        if (viewModel.monsters.value == null) {
            viewModel.loadMonsters()
        }

        binding.rvMonsters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1 &&
                    viewModel.hasMore && viewModel.loadingState.value == LoadingState.IDLE
                ) {
                    viewModel.loadMonsters(true)
                }
            }
        })

        viewModel.networkError.observeViewLifecycle {
            context?.let {
                Toast.makeText(it, "Network Error!", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.suggestMonsters.observeViewLifecycle {
            suggestAdapter.setSuggestions(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_monsters, menu)
        val searchMenu = menu.findItem(R.id.app_bar_search)
        val searchView = searchMenu.actionView as SearchView
        this.searchView = searchView
        viewModel.currentQueryText?.takeIf { it.isNotEmpty() }?.let {
            searchView.setQuery(it, false)
            searchView.isIconified = false
            searchView.clearFocus()
        }
        searchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.loadMonsters()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchTextChanged(newText)
                return true
            }
        })
        searchView.suggestionsAdapter = suggestAdapter
        searchView.setOnCloseListener {
            viewModel.loadMonsters()
            false
        }
        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                searchView.setQuery(suggestAdapter.getItemAt(position), true)
                return false
            }
        })
    }
}
