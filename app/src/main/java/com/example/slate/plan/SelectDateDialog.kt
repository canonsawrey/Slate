package com.example.slate.plan

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.graphics.component1
import androidx.fragment.app.DialogFragment
import com.example.slate.R
import java.util.*

class SelectDateDialog(pListener: DatePickerDialog.OnDateSetListener) : DialogFragment() {

    private val listener: DatePickerDialog.OnDateSetListener = pListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var dialog = DatePickerDialog(requireContext(), R.style.DatePicker, listener, year, month, day)
        dialog.datePicker.apply {
            minDate = System.currentTimeMillis()
            maxDate = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14
        }
        // Create a new instance of DatePickerDialog and return it
        return dialog
    }
}