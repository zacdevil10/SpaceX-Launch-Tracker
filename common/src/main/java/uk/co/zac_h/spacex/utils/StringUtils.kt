package uk.co.zac_h.spacex.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import java.net.URLEncoder
import java.text.DecimalFormat

fun String.generateCenterSpannableText(): SpannableString =
    SpannableString(this).apply {
        setSpan(RelativeSizeSpan(1.7f), 0, 8, 0)
        setSpan(StyleSpan(Typeface.NORMAL), 8, length - 11, 0)
        setSpan(RelativeSizeSpan(.8f), 8, length - 11, 0)
        setSpan(ForegroundColorSpan("29b6f6".rgb()), length - 11, length, 0)
    }

fun String.rgb(): Int {
    val color = java.lang.Long.parseLong(this.replace("#", ""), 16).toInt()
    val r = color shr 16 and 0xFF
    val g = color shr 8 and 0xFF
    val b = color shr 0 and 0xFF
    return Color.rgb(r, g, b)
}

fun Any.metricFormat(): String = DecimalFormat("#,###.##").format(this)

fun String.encodeUtf8(): String = URLEncoder.encode(this, "UTF-8")

fun String.formatWithUrls(
    //urls: List<TweetUrlModel>?,
    //mentions: List<TweetMentionsModel>?,
    //tags: List<TweetHashTagModel>?
): String {
    var message = this

    /*urls?.forEach {
        message = message.replace(it.url, "<a href='${it.url}'>${it.displayUrl}</a>")
    }

    val pattern = Pattern.compile("((https://t.co/)\\w+)\$")
    val matcher = pattern.matcher(message)

    message = matcher.replaceAll("")

    mentions?.forEach {
        message = message.replace(
            "@${it.screenName}",
            "<a href='https://twitter.com/${it.screenName}'>@${it.screenName}</a>",
            true
        )
    }

    tags?.forEach {
        message = message.replace(
            "#${it.tag}",
            "<a href='https://twitter.com/hashtag/${it.tag}'>#${it.tag}</a>",
            true
        )
    }*/

    return message
}

fun String?.orUnknown(): String = this ?: "Unknown"