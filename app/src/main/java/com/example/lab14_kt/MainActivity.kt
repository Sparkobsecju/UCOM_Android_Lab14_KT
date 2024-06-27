package com.example.lab14__kt

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab14_kt.R

class MainActivity : AppCompatActivity() {
    private var counter1: Int = 0
    private var counter2: Int = 0

    companion object {
        private const val MAIN_THREAD_TO_PAGE2 = 1234
        private const val MAIN_THREAD_INCREASE_COUNTER2 = 5678
        private const val WAIT_TIME_IN_MILLI = 10000L
    }

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button1.setOnClickListener { v ->
            Thread(object : Runnable {
                override fun run() {
                    Thread.sleep(WAIT_TIME_IN_MILLI)
                    runOnUiThread(object : Runnable {
                        override fun run() {
                            setContentView(R.layout.page2)
                        }
                    })
                }
            }).start()

        }
        button2.setOnClickListener {
            Thread {
                Thread.sleep(WAIT_TIME_IN_MILLI)
                runOnUiThread { setContentView(R.layout.page2) }
            }.start()
        }
        button3.setOnClickListener {
            Thread{
                Thread.sleep(WAIT_TIME_IN_MILLI)
                handler.sendEmptyMessage(MAIN_THREAD_TO_PAGE2)
            }.start()
        }
        runCounter()
        runCounter2()
    }

    private fun runCounter2() {
        Thread {
            while (true) {
                Thread.sleep(40)
                handler.sendEmptyMessage(MAIN_THREAD_INCREASE_COUNTER2)
            }
        }.start()
    }

    private fun runCounter() {
        Thread {
            while (true) {
                Thread.sleep(50)
                runOnUiThread { textView1.text = "counter1 = ${counter1++}" }
            }

        }.start()
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MAIN_THREAD_INCREASE_COUNTER2 -> {
                    textView2.text = "counter2 = ${counter2++}"
                }

                MAIN_THREAD_TO_PAGE2 -> {
                    setContentView(R.layout.page2)
                }
            }
        }
    }
}