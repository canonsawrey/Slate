package com.example.slate.launch

import android.os.Bundle
import com.example.slate.R
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.slate.main.MainActivity
import com.example.slate.Util
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val disp = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        loadSharedPreferences()
        nextStep()
    }

    private fun nextStep() {
        disp.add(Observable.timer(4000, TimeUnit.MILLISECONDS).subscribe {
            MainActivity.launch(this, applicationContext)
        })
    }

    private fun loadSharedPreferences() {
        getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        disp.dispose()
    }
}

