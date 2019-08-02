package com.example.slate

import android.view.View
import java.lang.IllegalArgumentException

object Util {

    @JvmStatic
    fun enableButton(button: View) {
        button.isEnabled = true
        button.isClickable = true
        button.alpha = 1.toFloat()
    }

    @JvmStatic
    fun disableButton(button: View) {
        button.isEnabled = false
        button.isClickable = false
        button.alpha = 0.5.toFloat()
    }
}
