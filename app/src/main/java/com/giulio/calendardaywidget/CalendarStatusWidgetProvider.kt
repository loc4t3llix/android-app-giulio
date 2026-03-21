package com.giulio.calendardaywidget

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.ZoneId

class CalendarStatusWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE,
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            CalendarContract.ACTION_EVENT_REMINDER -> {
                updateAllWidgets(context)
            }
        }
    }

    companion object {
        private const val EVENT_A_TITLE = "MAMMA"
        private const val EVENT_B_TITLE = "PAPI"

        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val provider = ComponentName(context, CalendarStatusWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(provider)
            ids.forEach { id ->
                updateWidget(context, manager, id)
            }
        }

        private fun updateWidget(
            context: Context,
            manager: AppWidgetManager,
            widgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_calendar_status)
            val state = CalendarTodayReader.readTodayState(context, EVENT_A_TITLE, EVENT_B_TITLE)

            val color = when (state) {
                TodayState.EVENT_A -> ContextCompat.getColor(context, R.color.event_a_color)
                TodayState.EVENT_B -> ContextCompat.getColor(context, R.color.event_b_color)
                TodayState.NO_PERMISSION -> ContextCompat.getColor(context, R.color.no_permission_color)
                TodayState.NONE -> ContextCompat.getColor(context, R.color.no_event_color)
            }

            views.setInt(R.id.widget_root, "setBackgroundColor", color)

            val clickIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            manager.updateAppWidget(widgetId, views)
        }
    }
}

private enum class TodayState {
    NONE,
    EVENT_A,
    EVENT_B,
    NO_PERMISSION
}

private object CalendarTodayReader {

    fun readTodayState(context: Context, eventATitle: String, eventBTitle: String): TodayState {
        if (!hasCalendarPermission(context)) {
            return TodayState.NO_PERMISSION
        }

        val nowDate = LocalDate.now()
        val zone = ZoneId.systemDefault()

        val startOfDay = nowDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val endOfDay = nowDate.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        CalendarContract.Instances.appendId(builder, startOfDay)
        CalendarContract.Instances.appendId(builder, endOfDay)

        val projection = arrayOf(
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.ALL_DAY,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END
        )

        val selection = "${CalendarContract.Instances.ALL_DAY} = 1"

        var hasEventA = false
        var hasEventB = false

        context.contentResolver.query(
            builder.build(),
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            val titleIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.TITLE)
            val beginIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.BEGIN)
            val endIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.END)

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleIdx)?.trim().orEmpty()
                val begin = cursor.getLong(beginIdx)
                val end = cursor.getLong(endIdx)

                // Consider only events that overlap with today in the device timezone.
                val overlapsToday = begin < endOfDay && end > startOfDay
                if (!overlapsToday) continue

                if (title.equals(eventATitle, ignoreCase = true)) {
                    hasEventA = true
                }
                if (title.equals(eventBTitle, ignoreCase = true)) {
                    hasEventB = true
                }

                if (hasEventA && hasEventB) {
                    break
                }
            }
        }

        return when {
            hasEventA -> TodayState.EVENT_A
            hasEventB -> TodayState.EVENT_B
            else -> TodayState.NONE
        }
    }

    private fun hasCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }
}
