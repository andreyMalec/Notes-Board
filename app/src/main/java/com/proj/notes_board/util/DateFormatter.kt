package com.proj.notes_board.util

import android.content.Context
import com.proj.notes_board.R
import java.util.*

class DateFormatter(private val context: Context) {
    private val millisecondsInDay: Long = 1000 * 60 * 60 * 24

    fun formatDaysAgo(date: Long): String {
        val currentDate = Calendar.getInstance().time.time
        val diff = currentDate - date
        val diffDays = diff / millisecondsInDay

        return when {
            diffDays <= 1 -> context.getString(R.string.date_today)
            diffDays <= 2 -> context.getString(R.string.date_yesterday)
            else -> context.getString(R.string.date_n_days_ago, diffDays)
        }
    }
}