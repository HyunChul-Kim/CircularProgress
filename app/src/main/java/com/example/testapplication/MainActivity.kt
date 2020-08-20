package com.example.testapplication

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startTime = 10f
        progress_circular.setProgressMaxValue(startTime)
        progress_circular.setProgressValue(startTime)
        object: CountDownTimer(startTime.toLong() * 1000, 10) {
            override fun onFinish() {
                progress_circular.updateProgressValue(0f)
            }

            override fun onTick(millsec: Long) {
                progress_circular.updateProgressValue(millsec / 1000f)
            }

        }.start()
    }

}