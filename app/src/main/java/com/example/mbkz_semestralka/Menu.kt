package com.example.mbkz_semestralka

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun openExercise(v: View){
        startActivity(Intent(this, Exercise::class.java))
    }

    fun openChallenge(v: View){
        startActivity(Intent(this, Challenge::class.java))
    }

    fun openSettings(v: View){
        startActivity(Intent(this, Settings::class.java))
    }

    fun openAbout(v: View){
        startActivity(Intent(this, About::class.java))
    }
}