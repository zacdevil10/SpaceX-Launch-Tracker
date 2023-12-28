package uk.co.zac_h.spacex.core.common.utils

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class TextResource {

    data class SimpleTextResource(val string: String) : TextResource()

    data class IdTextResource(@StringRes val id: Int) : TextResource()

    data class IdTextWithArgsResource(@StringRes val id: Int, val args: List<Any>) : TextResource()

    fun asString(res: Resources): String = when (this) {
        is SimpleTextResource -> string
        is IdTextResource -> res.getString(id)
        is IdTextWithArgsResource -> res.getString(id, *args.toTypedArray())
    }

    @Composable
    fun asString(): String = when (this) {
        is SimpleTextResource -> string
        is IdTextResource -> stringResource(id)
        is IdTextWithArgsResource -> stringResource(id,  *args.toTypedArray())
    }

    companion object {
        fun string(string: String): TextResource = SimpleTextResource(string)

        fun string(@StringRes id: Int): TextResource = IdTextResource(id)

        fun string(@StringRes id: Int, vararg args: Any): TextResource =
            IdTextWithArgsResource(id, args.asList())
    }
}