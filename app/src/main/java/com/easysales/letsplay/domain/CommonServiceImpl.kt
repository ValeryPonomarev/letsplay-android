package com.easysales.letsplay.data.service.impl

import com.easysales.letsplay.data.storage.IPreferencesLocalStorage
import com.easysales.letsplay.domain.CommonService

class CommonServiceImpl(private val storage: IPreferencesLocalStorage) :
    CommonService {
    private val KEY_IS_FIRST_LAUNCH: String  = "KEY_IS_FIRST_LAUNCH"

    override fun doFirstLaunch() {
        storage.putBoolean(KEY_IS_FIRST_LAUNCH, false)
    }

    override fun isFirstLaunch(): Boolean {
        return storage.getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }
}