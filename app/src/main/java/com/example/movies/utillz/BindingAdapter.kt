package com.example.movies.utillz

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("setData")
    fun setData(view: TextView, data: String?) {
        data?.let {
            val formattedString =
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
            view.text = formattedString
        }
    }
}
