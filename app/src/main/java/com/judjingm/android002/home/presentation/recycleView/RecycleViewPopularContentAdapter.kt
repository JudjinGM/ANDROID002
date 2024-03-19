package com.judjingm.android002.home.presentation.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.judjingm.android002.databinding.ItemPopularContentBinding
import com.judjingm.android002.home.presentation.models.PopularContentUi

class RecycleViewPopularContentAdapter(
    private var clickListener: (PopularContentUi) -> Unit = {}
) : RecyclerView.Adapter<PopularContentViewHolder>() {
    var items = listOf<PopularContentUi>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                DiffCallbackPopularContent(field, newList)
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularContentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PopularContentViewHolder(
            ItemPopularContentBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PopularContentViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            clickListener(items[holder.adapterPosition])
        }
    }
}