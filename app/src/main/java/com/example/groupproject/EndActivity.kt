package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button

class EndActivity : AppCompatActivity() {

    private lateinit var recordTextView: TextView
    private lateinit var winnerTextView: TextView
    private lateinit var thankyouTextView: TextView
    private lateinit var webView: WebView
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE)

        recordTextView = TextView(this)
        winnerTextView = TextView(this)
        thankyouTextView = TextView(this)

        recordTextView.text = "Number of games played: " + preferences.getInt("games_played", 0).toString()
        recordTextView.textSize = 32f

        val winnerCode = intent.getIntExtra("winner", -10)
        winnerTextView.text = when (winnerCode) {
            0 -> "Black wins"
            1 -> "White wins"
            -1 -> "It's a draw"
            else -> "ERROR"
        }
        winnerTextView.textSize = 32f

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val winnerLayoutParams = LinearLayout.LayoutParams(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.widthPixels / 7
        )

        winnerLayoutParams.topMargin = resources.displayMetrics.widthPixels / 4

        val recordLayoutParams = LinearLayout.LayoutParams(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.widthPixels / 7
        )

        recordLayoutParams.bottomMargin = resources.displayMetrics.widthPixels / 6


        thankyouTextView.text = "TIPS TO IMPROVE!"
        thankyouTextView.textSize = 32f

        val thankyouLayoutParams = LinearLayout.LayoutParams(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.widthPixels / 7
        )
        thankyouLayoutParams.bottomMargin = resources.displayMetrics.widthPixels / 12
        layout.addView(winnerTextView, winnerLayoutParams)
        layout.addView(recordTextView, recordLayoutParams)
        layout.addView(thankyouTextView, thankyouLayoutParams)

        webView = WebView(this)
        webView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.displayMetrics.widthPixels / 2
        )

        // Configure WebView settings
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Load YouTube video
        val videoId = "SvxTrjvPrSY"
        val videoUrl = "https://www.youtube.com/embed/$videoId"
        val html = "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" frameborder=\"0\" allowfullscreen></iframe>"

        // Load YouTube video in WebView
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
        webView.webChromeClient = WebChromeClient()

        layout.addView(webView)

        setContentView(layout)

    }
}
