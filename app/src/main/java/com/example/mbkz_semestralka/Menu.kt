package com.example.mbkz_semestralka

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Create preferences if not exist
        if (!getSharedPreferences(resources.getString(R.string.shared_pref), 0).contains(resources.getString(R.string.position1))) {
            saveDefaultPreferences()
        }
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

    private fun saveDefaultPreferences(){
        val preferences : SharedPreferences.Editor = getSharedPreferences(resources.getString(R.string.shared_pref), 0).edit()

        preferences.putBoolean(resources.getString(R.string.position1), resources.getBoolean(R.bool.position1_val))
        preferences.putBoolean(resources.getString(R.string.position2), resources.getBoolean(R.bool.position2_val))
        preferences.putBoolean(resources.getString(R.string.position3), resources.getBoolean(R.bool.position3_val))

        preferences.putBoolean(resources.getString(R.string.operation1), resources.getBoolean(R.bool.operation1_val))
        preferences.putBoolean(resources.getString(R.string.operation2), resources.getBoolean(R.bool.operation2_val))
        preferences.putBoolean(resources.getString(R.string.operation3), resources.getBoolean(R.bool.operation3_val))
        preferences.putBoolean(resources.getString(R.string.operation4), resources.getBoolean(R.bool.operation4_val))

        preferences.putInt(resources.getString(R.string.problems), resources.getInteger(R.integer.problems_val))

        preferences.putInt(resources.getString(R.string.min1), resources.getInteger(R.integer.min1_val))
        preferences.putInt(resources.getString(R.string.max1), resources.getInteger(R.integer.max1_val))
        preferences.putInt(resources.getString(R.string.min2), resources.getInteger(R.integer.min2_val))
        preferences.putInt(resources.getString(R.string.max2), resources.getInteger(R.integer.max2_val))

        preferences.putBoolean(resources.getString(R.string.debug), resources.getBoolean(R.bool.debug_val))

        preferences.apply()
    }
}