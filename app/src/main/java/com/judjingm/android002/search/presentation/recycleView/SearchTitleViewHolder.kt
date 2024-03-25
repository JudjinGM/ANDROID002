package com.judjingm.android002.search.presentation.recycleView

import androidx.recyclerview.widget.RecyclerView
import com.judjingm.android002.databinding.ItemTitleBinding
import com.judjingm.android002.search.presentation.models.SearchContentUiItem


class SearchTitleViewHolder(
    private val binding: ItemTitleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(content: SearchContentUiItem.TitleUiItem) {
        binding.title.text = content.title.value(binding.root.context)
    }
}