package com.judjingm.android002.home.presentation.models

import android.content.Context
import androidx.annotation.StringRes

sealed interface StringVO {
    fun value(context: Context): String

    data class Plain(private val value: String) : StringVO {
        override fun value(context: Context): String = value
    }

    data class Resource(@StringRes private val id: Int) : StringVO {
        override fun value(context: Context): String = context.getString(id)
    }
}