package com.easysales.letsplay.presentation.articlelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easysales.letsplay.R
import com.easysales.letsplay.data.exception.AppException
import com.easysales.letsplay.presentation.core.BaseViewHolder
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ArticleListAdapter : RecyclerView.Adapter<VH>() {
    private var items: List<ArticleItemDto> = ArrayList()
    private val itemClicksSubject: Subject<ArticleClickEvent> = PublishSubject.create()

    public val itemClicks: Observable<ArticleClickEvent> = itemClicksSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = when (viewType) {
            ViewType.DEFAULT.value -> VH(LayoutInflater.from(parent.context).inflate(R.layout.content_article_short, parent, false))
            else -> throw AppException(R.string.error_illegal_parameter, "ViewType")
        }

        return vh
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.setText(R.id.tvTitle, item.title)
            .setText(R.id.tvDescription, item.description)
            .setText(R.id.tvLikesCount, item.likes)
            .addOnClickListener(R.id.container_item) { itemClicksSubject.onNext(ArticleClickEvent(item.id)) }

        val categories = holder.itemView.findViewById<ChipGroup>(R.id.cgCategories)
        for(category in item.categories) {
            val chipCategory = Chip(holder.itemView.context)
            chipCategory.text = category
            categories.addView(chipCategory)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ViewType.DEFAULT.value
    }

    fun reload(items: List<ArticleItemDto>) {
        this.items = items.sortedBy { it.likes }
        notifyDataSetChanged()
    }

    enum class ViewType(val value: Int) {
        DEFAULT(1)
    }
}

class VH(itemView: View) : BaseViewHolder(itemView)