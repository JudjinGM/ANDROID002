package com.judjingm.android002.home.presentation.recycleView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.judjingm.android002.R
import com.judjingm.android002.databinding.ItemPopularContentBinding
import com.judjingm.android002.home.presentation.models.PopularContentUi


class PopularContentViewHolder(
    private val binding: ItemPopularContentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: PopularContentUi) {
        binding.contentNameTextView.text = content.title
        Glide
            .with(itemView)
            .load(IMAGE_BASE_URL + content.posterPath)
            .placeholder(R.drawable.content_cover_rv)
            .centerInside()
            .into(binding.contentCoverImageView)
    }

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}