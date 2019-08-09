package com.example.slate.list.add

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.slate.main.MainActivity
import com.example.slate.R
import kotlinx.android.synthetic.main.activity_add_list_item.*

class AddListItemActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list_item)

        cancel_button.setOnClickListener(this)
        add_item_button.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when(view) {
            cancel_button -> {
                val intent = Intent(this@AddListItemActivity, MainActivity::class.java)
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            add_item_button -> addItemClicked()
        }
    }

    private fun addItemClicked() {
        val intent = Intent(this@AddListItemActivity, MainActivity::class.java)

        val strings =
            if (quantity_text.text.isNotEmpty()) {
                if (quantity_unit_text.text.isNotEmpty()) {
                    arrayOf(
                        item_name_text.text.toString(),
                        quantity_text.text.toString(),
                        quantity_unit_text.text.toString()
                    )
                } else {
                    arrayOf(
                        item_name_text.text.toString(),
                        quantity_text.text.toString()
                    )
                }
            } else {
                arrayOf(item_name_text.text.toString())
            }

        intent.putExtra("item", strings)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
