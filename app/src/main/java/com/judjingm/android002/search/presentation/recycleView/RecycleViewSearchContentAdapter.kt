package com.judjingm.android002.search.presentation.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.judjingm.android002.databinding.ItemSearchContentBinding
import com.judjingm.android002.databinding.ItemTitleBinding
import com.judjingm.android002.search.presentation.models.SearchContentUiItem

class RecycleViewSearchContentAdapter(
    private var clickListener: (SearchContentUiItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<SearchContentUiItem>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackSearchContent(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TITLE -> SearchTitleViewHolder(
                ItemTitleBinding.inflate(layoutInflater, parent, false)
            )

            MOVIE -> SearchMovieViewHolder(
                ItemSearchContentBinding.inflate(layoutInflater, parent, false),
                clickListener
            )

            TV_SHOW -> SearchTvShowViewHolder(
                ItemSearchContentBinding.inflate(layoutInflater, parent, false),
                clickListener
            )

            else -> throw IllegalAccessException("Illegal type: $viewType")
        }
    }

    override fun getItemCount(): Int = items.size
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SearchContentUiItem.TitleUiItem -> TITLE
            is SearchContentUiItem.MovieUiItem -> MOVIE
            is SearchContentUiItem.TvShowUiItem -> TV_SHOW
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SearchContentUiItem.TitleUiItem -> (holder as SearchTitleViewHolder).bind(item)
            is SearchContentUiItem.MovieUiItem -> (holder as SearchMovieViewHolder).bind(item)
            is SearchContentUiItem.TvShowUiItem -> (holder as SearchTvShowViewHolder).bind(item)
        }
    }

    companion object {
        const val TITLE = 0
        const val MOVIE = 1
        const val TV_SHOW = 2
    }
}