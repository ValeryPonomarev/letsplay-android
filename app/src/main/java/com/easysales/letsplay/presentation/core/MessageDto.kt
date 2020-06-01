package com.easysales.letsplay.presentation.core

import androidx.annotation.StringRes

data class MessageDto(
    @StringRes var title: Int,
    @StringRes var description: Int,
    var type: MessageType = MessageType.INFO,
    var params: List<String> = ArrayList()
)
enum class MessageType {
    INFO, ERROR
}