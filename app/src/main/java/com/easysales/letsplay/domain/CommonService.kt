package com.easysales.letsplay.domain

interface CommonService {
    fun isFirstLaunch(): Boolean
    fun doFirstLaunch()
}