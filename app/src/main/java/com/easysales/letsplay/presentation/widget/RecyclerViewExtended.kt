package com.easysales.letsplay.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.content.res.getResourceIdOrThrow
import androidx.recyclerview.widget.RecyclerView
import com.easysales.letsplay.R

class RecyclerViewExtended : RecyclerView {
    @IdRes
    private var emptyViewId: Int? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init(attrs)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                val observerAdapter = getAdapter()
                if(observerAdapter != null) {
                    if(observerAdapter.itemCount == 0) { showEmptyView() } else { hideEmptyView() }
                }
            }
        })
    }

    private fun init(attrs: AttributeSet?) {
        if(attrs == null) return
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewExtended)
        try {
            emptyViewId = attributes.getResourceIdOrThrow(R.styleable.RecyclerViewExtended_empty_view_id)
        } catch (e: Exception) { }
    }

    private fun showEmptyView() {
        val emptyView = getEmptyView()
        if(emptyView != null) {
            emptyView.visibility = View.VISIBLE
            this.visibility = View.GONE
        }
    }

    private fun hideEmptyView() {
        val emptyView = getEmptyView()
        if(emptyView != null) {
            emptyView.visibility = View.GONE
            this.visibility = View.VISIBLE
        }
    }

    private fun getEmptyView(): View? {
        if(emptyViewId == null) return null
        val parentView = parent as ViewGroup
        if(parentView != null) return parentView.findViewById(emptyViewId!!)
        return findViewById<View>(emptyViewId!!)
    }
}