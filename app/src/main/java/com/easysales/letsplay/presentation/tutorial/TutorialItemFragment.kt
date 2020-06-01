package com.easysales.letsplay.presentation.tutorial

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_tutorial_item.*
import com.easysales.letsplay.R
import com.easysales.letsplay.data.dto.TutorialDto
import com.easysales.letsplay.presentation.core.BaseFragment
import com.easysales.letsplay.utils.ImageUtils

class TutorialItemFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_tutorial_item
    }

    companion object {
        private const val ARG_ITEM = "ARG_ITEM"

        fun newInstance(item: TutorialDto) : TutorialItemFragment {
            val args = Bundle()
            args.putSerializable(ARG_ITEM, item)

            val tutorialFragment = TutorialItemFragment()
            tutorialFragment.arguments = args
            return tutorialFragment
        }
    }

    override fun initialize() {
        super.initialize()
        val item = arguments!!.getSerializable(ARG_ITEM) as TutorialDto

        ImageUtils.load(context!!, ivContent, item.url, R.drawable.loader)
    }
}