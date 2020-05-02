package com.proj.notes_board.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.proj.notes_board.util.DateFormatter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("date")
    fun date(textView: TextView, date: Long) {
        textView.text = DateFormatter(textView.context).formatDaysAgo(date)
    }
}