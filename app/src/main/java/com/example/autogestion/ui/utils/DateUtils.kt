package com.example.autogestion.ui.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * DateUtils is an object that provides utilities related to date formatting.
 * Being an object, it is a singleton, meaning only one instance of this class exists in the entire application.
 */
object DateUtils {

    // dateFormat is a SimpleDateFormat object initialized to format dates in the pattern "dd/MM/yyyy".
    // Locale.getDefault() ensures that the date format is consistent with the default locale of the device.
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun showDatePicker(context: Context, calendar: Calendar, onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}