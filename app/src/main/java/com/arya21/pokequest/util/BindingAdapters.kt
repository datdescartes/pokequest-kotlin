package com.arya21.pokequest.util

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.arya21.pokequest.BR
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "imageRequestListener"], requireAll = false)
    fun loadImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        if (imageUrl == null) {
            imageView.setImageDrawable(null)
            return
        }
        else Glide.with(imageView.context)
            .load(imageUrl)
            .listener(listener)
            .placeholder(null)
            .into(imageView)
    }

    @JvmStatic @BindingAdapter("entries", "layout")
    fun <T> setEntries(viewGroup: ViewGroup, entries: List<T>?, layoutId: Int) {
        viewGroup.removeAllViews()
        if (entries != null) {
            val inflater = viewGroup.context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            entries.forEach {entry ->
                val binding = DataBindingUtil
                    .inflate<ViewDataBinding>(inflater, layoutId, viewGroup, true)
                binding.setVariable(BR.item, entry)
            }
        }
    }
}
