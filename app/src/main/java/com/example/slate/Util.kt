package com.example.slate

import android.view.View
import com.example.slate.data.DatabaseListItem
import com.example.slate.list.ListItem
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



    @JvmStatic
    fun listItemToDatabaseListItem(listItem: ListItem): DatabaseListItem {
        return DatabaseListItem(
            0,
            listItem.name,
            listItem.quantity,
            listItem.quantityUnit
        )
    }
}
