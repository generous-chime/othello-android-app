package com.example.groupproject

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() {

    private lateinit var playButton: Button
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize UI components
        playButton = findViewById(R.id.playButton)
        adView = findViewById(R.id.adView)

        // Set click listeners for the buttons
        playButton.setOnClickListener {
            var gameBoard : Intent = Intent(this,OthelloActivity::class.java )
            startActivity(gameBoard, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }
}
