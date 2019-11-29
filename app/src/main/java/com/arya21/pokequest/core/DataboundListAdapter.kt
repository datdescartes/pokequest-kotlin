package com.arya21.pokequest.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.arya21.pokequest.BR

abstract class DataBoundListAdapter<Item, Binding : ViewDataBinding> :
    RecyclerView.Adapter<DataBoundViewHolder<Binding, Item>>() {
    private var items: List<Item>? = null

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<Binding, Item> {
        val binding = onCreateBinding(parent)
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<Binding, Item>, position: Int) {
        holder.item = items?.get(position)
        holder.binding.setVariable(BR.item, holder.item)
        holder.binding.executePendingBindings()
    }

    fun submitList(newList: List<Item>?, notifyChanged: Boolean = true, isAppending: Boolean = false) {
        if (newList === items) return

        if (newList == null) {
            val countRemoved = items!!.size
            items = null
            if (notifyChanged) notifyItemRangeRemoved(0, countRemoved)
            return
        }

        if (items == null) {
            items = newList
            if (notifyChanged) notifyItemRangeInserted(0, newList.size)
            return
        }

        val oldLength = items!!.size
        items = newList
        if (notifyChanged) {
            if (isAppending) notifyItemRangeInserted(oldLength, items!!.size - oldLength)
            else notifyDataSetChanged()
        }
    }

    protected abstract fun onCreateBinding(parent: ViewGroup): Binding
}

class SimpleDataBoundListAdapter<Item, Binding : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    private val itemClickCallback: ((Item, Binding) -> Unit)?) :
    DataBoundListAdapter<Item, Binding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<Binding, Item> {
        val binding = onCreateBinding(parent)
        val viewHolder = DataBoundViewHolder<Binding, Item>(binding)
        if (itemClickCallback != null) {
            binding.root.setOnClickListener {
                viewHolder.item?.let { itemClickCallback.invoke(it, viewHolder.binding) }
            }
        }
        return viewHolder
    }

    override fun onCreateBinding(parent: ViewGroup): Binding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false,
            null
        )
    }
}