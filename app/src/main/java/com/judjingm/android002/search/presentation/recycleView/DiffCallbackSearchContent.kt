package com.judjingm.android002.search.presentation.recycleView

import androidx.recyclerview.widget.DiffUtil
import com.judjingm.android002.search.presentation.models.SearchContentUiItem

class DiffCallbackSearchContent(
    private val oldList: List<SearchContentUiItem>,
    private val newList: List<SearchContentUiItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old == new
    }

}