package com.giulio.calendardaywidget

import android.content.Context
import androidx.core.content.ContextCompat

class WidgetPreferences(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val defaultEventATitle = context.getString(R.string.default_event_a_title)
    private val defaultEventBTitle = context.getString(R.string.default_event_b_title)

    fun getEventATitle(): String {
        return prefs.getString(KEY_EVENT_A_TITLE, defaultEventATitle)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: defaultEventATitle
    }

    fun getEventBTitle(): String {
        return prefs.getString(KEY_EVENT_B_TITLE, defaultEventBTitle)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: defaultEventBTitle
    }

    fun saveEventTitles(eventATitle: String, eventBTitle: String) {
        prefs.edit()
            .putString(KEY_EVENT_A_TITLE, eventATitle.trim())
            .putString(KEY_EVENT_B_TITLE, eventBTitle.trim())
            .apply()
    }

    fun getEventAColor(): Int = getColor(KEY_EVENT_A_COLOR, R.color.event_a_color)

    fun getEventBColor(): Int = getColor(KEY_EVENT_B_COLOR, R.color.event_b_color)

    fun getNoEventColor(): Int = getColor(KEY_NO_EVENT_COLOR, R.color.no_event_color)

    fun getNoPermissionColor(): Int = getColor(KEY_NO_PERMISSION_COLOR, R.color.no_permission_color)

    fun saveEventAColor(color: Int) {
        prefs.edit().putInt(KEY_EVENT_A_COLOR, color).apply()
    }

    fun saveEventBColor(color: Int) {
        prefs.edit().putInt(KEY_EVENT_B_COLOR, color).apply()
    }

    fun saveNoEventColor(color: Int) {
        prefs.edit().putInt(KEY_NO_EVENT_COLOR, color).apply()
    }

    fun saveNoPermissionColor(color: Int) {
        prefs.edit().putInt(KEY_NO_PERMISSION_COLOR, color).apply()
    }

    private fun getColor(key: String, defaultColorRes: Int): Int {
        return if (prefs.contains(key)) {
            prefs.getInt(key, ContextCompat.getColor(context, defaultColorRes))
        } else {
            ContextCompat.getColor(context, defaultColorRes)
        }
    }

    companion object {
        private const val PREFS_NAME = "widget_preferences"

        private const val KEY_EVENT_A_TITLE = "event_a_title"
        private const val KEY_EVENT_B_TITLE = "event_b_title"

        private const val KEY_EVENT_A_COLOR = "event_a_color"
        private const val KEY_EVENT_B_COLOR = "event_b_color"
        private const val KEY_NO_EVENT_COLOR = "no_event_color"
        private const val KEY_NO_PERMISSION_COLOR = "no_permission_color"
    }
}
