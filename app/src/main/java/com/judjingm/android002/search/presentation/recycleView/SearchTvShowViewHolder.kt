package com.judjingm.android002.search.presentation.recycleView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.judjingm.android002.R
import com.judjingm.android002.databinding.ItemSearchContentBinding
import com.judjingm.android002.search.presentation.models.SearchContentUiItem


class SearchTvShowViewHolder(
    private val binding: ItemSearchContentBinding,
    private val onItemClicked: (SearchContentUiItem.TvShowUiItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: SearchContentUiItem.TvShowUiItem) {
        binding.titleTextView.text = content.title
        binding.voteTextView.text = content.averageVote
        binding.yearTextView.text = content.firstAirDate
        Glide
            .with(itemView)
            .load(IMAGE_BASE_URL + content.posterPath)
            .placeholder(R.drawable.content_cover_rv)
            .centerInside()
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.corner_radius
                    )
                )
            ).into(binding.posterImageView)

        binding.root.setOnClickListener {
            onItemClicked.invoke(content)
        }
    }

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w92"
    }
}