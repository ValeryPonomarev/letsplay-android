package com.easysales.letsplay.presentation.core

interface ViewState

data class LoaderState(
    val isLoading: Boolean
): ViewState