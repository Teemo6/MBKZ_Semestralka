package com.example.mbkz_semestralka

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadPreferences()
    }
    private fun setAppLocale(language: String) {

    }

    fun savePreferences(v: View){
        val preferences : SharedPreferences.Editor = getSharedPreferences(resources.getString(R.string.shared_pref), 0).edit()

        // Read values
        val pos1 = findViewById<CheckBox>(R.id.position1).isChecked
        val pos2 = findViewById<CheckBox>(R.id.position2).isChecked
        val pos3 = findViewById<CheckBox>(R.id.position3).isChecked

        val op1 = findViewById<CheckBox>(R.id.operation1).isChecked
        val op2 = findViewById<CheckBox>(R.id.operation2).isChecked
        val op3 = findViewById<CheckBox>(R.id.operation3).isChecked
        val op4 = findViewById<CheckBox>(R.id.operation4).isChecked

        val prob = findViewById<TextView>(R.id.problems).text.toString().toInt()

        val min1 = findViewById<TextView>(R.id.min1).text.toString().toInt()
        val max1 = findViewById<TextView>(R.id.max1).text.toString().toInt()
        val min2 = findViewById<TextView>(R.id.min2).text.toString().toInt()
        val max2 = findViewById<TextView>(R.id.max2).text.toString().toInt()

        val debug = findViewById<CheckBox>(R.id.debug).isChecked

        // Check values
        if (!pos1 && !pos2 && !pos3){
            showInputErrorDialog(resources.getString(R.string.input_error_position))
            return
        }

        if (!op1 && !op2 && !op3 && !op4){
            showInputErrorDialog(resources.getString(R.string.input_error_operation))
            return
        }

        if (prob <= 0){
            showInputErrorDialog(resources.getString(R.string.input_error_problems))
            return
        }

        if (min1 > max1 || min2 > max2){
            showInputErrorDialog(resources.getString(R.string.input_error_minmax))
            return
        }

        if (max1 - min1 < 3 || max2 - min2 < 3){
            showInputErrorDialog(resources.getString(R.string.input_error_minmax2))
            return
        }

        // Write values
        preferences.putBoolean(resources.getString(R.string.position1), pos1)
        preferences.putBoolean(resources.getString(R.string.position2), pos2)
        preferences.putBoolean(resources.getString(R.string.position3), pos3)

        preferences.putBoolean(resources.getString(R.string.operation1), op1)
        preferences.putBoolean(resources.getString(R.string.operation2), op2)
        preferences.putBoolean(resources.getString(R.string.operation3), op3)
        preferences.putBoolean(resources.getString(R.string.operation4), op4)

        preferences.putInt(resources.getString(R.string.problems), prob)

        preferences.putInt(resources.getString(R.string.min1), min1)
        preferences.putInt(resources.getString(R.string.max1), max1)
        preferences.putInt(resources.getString(R.string.min2), min2)
        preferences.putInt(resources.getString(R.string.max2), max2)

        preferences.putBoolean(resources.getString(R.string.debug), debug)

        preferences.apply()

        // Go to menu
        startActivity(Intent(this, Menu::class.java))
    }

    private fun loadPreferences(){
        val preferences : SharedPreferences = getSharedPreferences(resources.getString(R.string.shared_pref), 0)

        findViewById<CheckBox>(R.id.position1).setChecked(preferences.getBoolean(resources.getString(R.string.position1), resources.getBoolean(R.bool.position1_val)))
        findViewById<CheckBox>(R.id.position2).setChecked(preferences.getBoolean(resources.getString(R.string.position2), resources.getBoolean(R.bool.position2_val)))
        findViewById<CheckBox>(R.id.position3).setChecked(preferences.getBoolean(resources.getString(R.string.position3), resources.getBoolean(R.bool.position3_val)))

        findViewById<CheckBox>(R.id.operation1).setChecked(preferences.getBoolean(resources.getString(R.string.operation1), resources.getBoolean(R.bool.operation1_val)))
        findViewById<CheckBox>(R.id.operation2).setChecked(preferences.getBoolean(resources.getString(R.string.operation2), resources.getBoolean(R.bool.operation2_val)))
        findViewById<CheckBox>(R.id.operation3).setChecked(preferences.getBoolean(resources.getString(R.string.operation3), resources.getBoolean(R.bool.operation3_val)))
        findViewById<CheckBox>(R.id.operation4).setChecked(preferences.getBoolean(resources.getString(R.string.operation4), resources.getBoolean(R.bool.operation4_val)))

        findViewById<TextView>(R.id.problems).text = preferences.getInt(resources.getString(R.string.problems), resources.getInteger(R.integer.problems_val)).toString()

        findViewById<TextView>(R.id.min1).text = preferences.getInt(resources.getString(R.string.min1), resources.getInteger(R.integer.min1_val)).toString()
        findViewById<TextView>(R.id.max1).text = preferences.getInt(resources.getString(R.string.max1), resources.getInteger(R.integer.max1_val)).toString()
        findViewById<TextView>(R.id.min2).text = preferences.getInt(resources.getString(R.string.min2), resources.getInteger(R.integer.min2_val)).toString()
        findViewById<TextView>(R.id.max2).text = preferences.getInt(resources.getString(R.string.max2), resources.getInteger(R.integer.max2_val)).toString()

        findViewById<CheckBox>(R.id.debug).setChecked(preferences.getBoolean(resources.getString(R.string.debug), resources.getBoolean(R.bool.debug_val)))
    }

    fun saveDefaultPreferences(v: View){
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
        loadPreferences()
    }

    fun showInputErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_return_title))
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.dialog_ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}