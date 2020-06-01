package com.easysales.letsplay.presentation.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.easysales.letsplay.data.dto.TutorialDto

class TutorialAdapter(fragmentManager: FragmentManager, private val tutorials: ArrayList<TutorialDto>)
    : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return TutorialItemFragment.newInstance(tutorials[position])
    }

    override fun getCount(): Int {
        return tutorials.size
    }

    fun addAll(images: List<TutorialDto>) {
        this.tutorials.clear()
        this.tutorials.addAll(images)
        notifyDataSetChanged()
    }

    fun isLast(currentIndex: Int) : Boolean {
        return currentIndex == count - 1
    }

}