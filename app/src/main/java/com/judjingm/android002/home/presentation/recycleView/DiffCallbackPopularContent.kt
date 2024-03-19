package com.judjingm.android002.home.presentation.recycleView

import androidx.recyclerview.widget.DiffUtil
import com.judjingm.android002.home.presentation.models.PopularContentUi

class DiffCallbackPopularContent(
    private val oldList: List<PopularContentUi>,
    private val newList: List<PopularContentUi>
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