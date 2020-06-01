package com.easysales.letsplay.presentation.core

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val context: Context
        get() = this.itemView.context

    fun <V: View> findView(@IdRes viewId: Int): V {
        return this.itemView.findViewById(viewId)
    }

    fun setText(@IdRes viewId: Int, text: String) : BaseViewHolder {
        this.itemView.findViewById<TextView>(viewId).text = text
        return this
    }

    fun setVisibility(@IdRes viewId: Int, isVisible: Boolean) : BaseViewHolder {
        this.itemView.findViewById<View>(viewId).visibility = if(isVisible) View.VISIBLE else View.GONE
        return this
    }

    fun setEnabled(@IdRes viewId: Int, isEnabled: Boolean) : BaseViewHolder {
        this.itemView.findViewById<View>(viewId).isEnabled = isEnabled
        return this
    }

    fun addOnClickListener(@IdRes viewId: Int, action: () -> Unit): BaseViewHolder {
        this.itemView.findViewById<View>(viewId).setOnClickListener { action() }
        return this
    }

    fun setNestedRecycleView(@IdRes viewId: Int, layoutManager: RecyclerView.LayoutManager , adapter: RecyclerView.Adapter<BaseViewHolder>) : BaseViewHolder {
        val recyclerView = this.itemView.findViewById<RecyclerView>(viewId)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return this
    }
}