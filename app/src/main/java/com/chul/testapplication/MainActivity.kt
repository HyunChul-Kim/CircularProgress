package com.chul.testapplication

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val startTime = 10f
    private var isTimerFinished = true
    private val colors = intArrayOf(
        Color.RED, Color.parseColor("#ff7f00"),
        Color.YELLOW, Color.GREEN,
        Color.BLUE, Color.parseColor("#000080"),
        Color.parseColor("#8b00ff"), Color.RED)
    private val colors2 = intArrayOf(
        Color.parseColor("#0022ff"),
        Color.parseColor("#99d7f3"),
        Color.parseColor("#2ab6f5"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress_circular.setProgressMaxValue(startTime)
        progress_circular.setProgressValue(startTime)
        progress_circular.setProgressColor(colors)
        val timer = object: CountDownTimer(startTime.toLong() * 1000, 10) {
            override fun onFinish() {
                isTimerFinished = true
                timer_button.text = "Start"
                resetProgress()
            }

            override fun onTick(millsec: Long) {
                progress_circular.updateProgressValue(millsec / 1000f)
            }

        }
        timer_button.setOnClickListener {
            if(isTimerFinished) {
                isTimerFinished = false
                timer.start()
                timer_button.text = "Cancel"
            } else {
                isTimerFinished = true
                timer.cancel()
                timer_button.text = "Start"
                resetProgress()
            }
        }
    }

    private fun resetProgress() {
        progress_circular.updateProgressValue(startTime)
    }

}