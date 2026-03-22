package com.giulio.calendardaywidget

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var eventATitleInput: EditText
    private lateinit var eventBTitleInput: EditText
    private lateinit var eventAColorPreview: View
    private lateinit var eventBColorPreview: View
    private lateinit var noEventColorPreview: View
    private lateinit var noPermissionColorPreview: View
    private lateinit var widgetPreferences: WidgetPreferences

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        updatePermissionStatus(granted)
        CalendarStatusWidgetProvider.updateAllWidgets(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        widgetPreferences = WidgetPreferences(this)

        statusText = findViewById(R.id.permission_status)
        eventATitleInput = findViewById(R.id.event_a_title_input)
        eventBTitleInput = findViewById(R.id.event_b_title_input)
        eventAColorPreview = findViewById(R.id.event_a_color_preview)
        eventBColorPreview = findViewById(R.id.event_b_color_preview)
        noEventColorPreview = findViewById(R.id.no_event_color_preview)
        noPermissionColorPreview = findViewById(R.id.no_permission_color_preview)

        val requestButton: Button = findViewById(R.id.request_permission_button)
        val refreshButton: Button = findViewById(R.id.refresh_widget_button)
        val saveSettingsButton: Button = findViewById(R.id.save_settings_button)
        val eventAColorButton: Button = findViewById(R.id.event_a_color_button)
        val eventBColorButton: Button = findViewById(R.id.event_b_color_button)
        val noEventColorButton: Button = findViewById(R.id.no_event_color_button)
        val noPermissionColorButton: Button = findViewById(R.id.no_permission_color_button)

        updatePermissionStatus(hasCalendarPermission())
        loadCurrentSettings()

        requestButton.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_CALENDAR)
        }

        refreshButton.setOnClickListener {
            CalendarStatusWidgetProvider.updateAllWidgets(this)
        }

        saveSettingsButton.setOnClickListener {
            widgetPreferences.saveEventTitles(
                eventATitleInput.text.toString(),
                eventBTitleInput.text.toString()
            )
            loadCurrentSettings()
            CalendarStatusWidgetProvider.updateAllWidgets(this)
        }

        eventAColorButton.setOnClickListener {
            showColorPickerDialog(widgetPreferences.getEventAColor()) { color ->
                widgetPreferences.saveEventAColor(color)
                refreshColorPreviews()
                CalendarStatusWidgetProvider.updateAllWidgets(this)
            }
        }

        eventBColorButton.setOnClickListener {
            showColorPickerDialog(widgetPreferences.getEventBColor()) { color ->
                widgetPreferences.saveEventBColor(color)
                refreshColorPreviews()
                CalendarStatusWidgetProvider.updateAllWidgets(this)
            }
        }

        noEventColorButton.setOnClickListener {
            showColorPickerDialog(widgetPreferences.getNoEventColor()) { color ->
                widgetPreferences.saveNoEventColor(color)
                refreshColorPreviews()
                CalendarStatusWidgetProvider.updateAllWidgets(this)
            }
        }

        noPermissionColorButton.setOnClickListener {
            showColorPickerDialog(widgetPreferences.getNoPermissionColor()) { color ->
                widgetPreferences.saveNoPermissionColor(color)
                refreshColorPreviews()
                CalendarStatusWidgetProvider.updateAllWidgets(this)
            }
        }
    }

    private fun loadCurrentSettings() {
        eventATitleInput.setText(widgetPreferences.getEventATitle())
        eventBTitleInput.setText(widgetPreferences.getEventBTitle())
        refreshColorPreviews()
    }

    private fun refreshColorPreviews() {
        eventAColorPreview.setBackgroundColor(widgetPreferences.getEventAColor())
        eventBColorPreview.setBackgroundColor(widgetPreferences.getEventBColor())
        noEventColorPreview.setBackgroundColor(widgetPreferences.getNoEventColor())
        noPermissionColorPreview.setBackgroundColor(widgetPreferences.getNoPermissionColor())
    }

    private fun showColorPickerDialog(initialColor: Int, onColorSelected: (Int) -> Unit) {
        val pickerView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)
        val hueSeekBar = pickerView.findViewById<SeekBar>(R.id.hue_seekbar)
        val saturationSeekBar = pickerView.findViewById<SeekBar>(R.id.saturation_seekbar)
        val valueSeekBar = pickerView.findViewById<SeekBar>(R.id.value_seekbar)
        val alphaSeekBar = pickerView.findViewById<SeekBar>(R.id.alpha_seekbar)
        val colorPreview = pickerView.findViewById<View>(R.id.color_picker_preview)
        val colorHexText = pickerView.findViewById<TextView>(R.id.color_hex_text)

        val hsv = FloatArray(3)
        Color.colorToHSV(initialColor, hsv)
        val initialAlpha = Color.alpha(initialColor)

        hueSeekBar.max = 360
        saturationSeekBar.max = 100
        valueSeekBar.max = 100
        alphaSeekBar.max = 100

        hueSeekBar.progress = hsv[0].toInt()
        saturationSeekBar.progress = (hsv[1] * 100).toInt()
        valueSeekBar.progress = (hsv[2] * 100).toInt()
        alphaSeekBar.progress = ((initialAlpha * 100f) / 255f).roundToInt()

        fun getSelectedColor(): Int {
            val alpha = ((alphaSeekBar.progress * 255f) / 100f).roundToInt().coerceIn(0, 255)
            return Color.HSVToColor(
                alpha,
                floatArrayOf(
                    hueSeekBar.progress.toFloat(),
                    saturationSeekBar.progress / 100f,
                    valueSeekBar.progress / 100f
                )
            )
        }

        val updateColorPreview = {
            val selectedColor = getSelectedColor()
            colorPreview.setBackgroundColor(selectedColor)
            colorHexText.text = getString(
                R.string.selected_color_value,
                formatColorWithAlpha(selectedColor)
            )
        }

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorPreview()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        }

        hueSeekBar.setOnSeekBarChangeListener(listener)
        saturationSeekBar.setOnSeekBarChangeListener(listener)
        valueSeekBar.setOnSeekBarChangeListener(listener)
        alphaSeekBar.setOnSeekBarChangeListener(listener)

        updateColorPreview()

        AlertDialog.Builder(this)
            .setTitle(R.string.select_color)
            .setView(pickerView)
            .setPositiveButton(R.string.save) { _, _ ->
                onColorSelected(getSelectedColor())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun formatColorWithAlpha(color: Int): String {
        return String.format("#%08X", color)
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
