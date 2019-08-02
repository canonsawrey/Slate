package com.example.slate.list.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.slate.MainActivity
import com.example.slate.R
import kotlinx.android.synthetic.main.activity_add_list_item.*

class AddListItemActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list_item)

        cancel_button.setOnClickListener(this)
        add_item_button.setOnClickListener(this)

        cancel_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    override fun onClick(view: View?) {
        when(view) {
            cancel_button ->
        }
    }
}
