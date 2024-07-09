package jp.co.yumemi.android.code_check.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.network.model.FilteredItem

val DiffUtil = object : DiffUtil.ItemCallback<FilteredItem>() {
    override fun areItemsTheSame(oldItem: FilteredItem, newItem: FilteredItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FilteredItem, newItem: FilteredItem): Boolean {
        return oldItem == newItem
    }
}

class GithubRepositoryAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<FilteredItem, GithubRepositoryAdapter.ViewHolder>(DiffUtil) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: FilteredItem)
        fun favoriteClick(item: FilteredItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text = item.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }

        if (item.isFavorite) {
            (holder.itemView.findViewById<View>(R.id.btn_favorite) as ImageView).setImageResource(R.drawable.baseline_favorite_24)
        } else {
            (holder.itemView.findViewById<View>(R.id.btn_favorite) as ImageView).setImageResource(R.drawable.baseline_unfavorite_24)
        }

        holder.itemView.findViewById<View>(R.id.btn_favorite).setOnClickListener {
            itemClickListener.favoriteClick(item, position)
        }
    }
}
