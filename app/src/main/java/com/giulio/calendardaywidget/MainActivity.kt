package com.giulio.calendardaywidget

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        updatePermissionStatus(granted)
        CalendarStatusWidgetProvider.updateAllWidgets(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.permission_status)
        val requestButton: Button = findViewById(R.id.request_permission_button)
        val refreshButton: Button = findViewById(R.id.refresh_widget_button)

        updatePermissionStatus(hasCalendarPermission())

        requestButton.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_CALENDAR)
        }

        refreshButton.setOnClickListener {
            CalendarStatusWidgetProvider.updateAllWidgets(this)
        }
    }

    private fun hasCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updatePermissionStatus(granted: Boolean) {
        statusText.text = if (granted) {
            getString(R.string.permission_granted)
        } else {
            getString(R.string.permission_missing)
        }
    }
}
