package com.easysales.letsplay.presentation.article

import com.easysales.letsplay.presentation.core.ViewState
import java.io.Serializable

data class ViewDto(
    val contentHtml: String
) : ViewState

data class Args(
    val articleId: String
): Serializable

