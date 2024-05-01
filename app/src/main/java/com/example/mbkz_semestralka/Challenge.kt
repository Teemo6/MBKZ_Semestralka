package com.example.mbkz_semestralka

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlin.random.Random

class Challenge : AppCompatActivity() {
    private var POSITIONS = mutableListOf<Int>()
    private var OPERATORS = mutableListOf<String>()
    private var MIN_EZ = 0
    private var MAX_EZ = 0
    private var MIN_HARD = 0
    private var MAX_HARD = 0
    private var DEBUG = false

    private var solution = 0
    private var score = 0
    private var lost = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        // Initialize variables
        loadPreferencesData()
        solution = -1
        score = 0
        lost = false

        // Start generating math problems
        generateMathProblem()
    }

    private fun generateMathProblem(){
        // Reset variables
        var num1 = 0
        var num2 = 0
        var ans = 0

        // Generate new problem
        val operator = OPERATORS[Random.nextInt(OPERATORS.size)]
        when (operator) {
            "+" -> {
                ans = Random.nextInt(MIN_EZ + 2, MAX_EZ)
                num1 = Random.nextInt(MIN_EZ, ans)
                num2 = ans - num1
            }
            "-" -> {
                ans = Random.nextInt(MIN_EZ, MAX_EZ - 2)
                num1 = Random.nextInt(ans, MAX_EZ)
                num2 = num1 - ans
            }
            "*" -> {
                num1 = Random.nextInt(MIN_HARD, MAX_HARD)
                num2 = Random.nextInt(MIN_HARD, MAX_HARD)
                while(num1 == 0 || num2 == 0){
                    num1 = Random.nextInt(MIN_HARD, MAX_HARD)
                    num2 = Random.nextInt(MIN_HARD, MAX_HARD)
                }
                ans = num1 * num2
            }
            "/" -> {
                num2 = Random.nextInt(MIN_HARD, MAX_HARD)
                ans = Random.nextInt(MIN_HARD, MAX_HARD)
                while(num2 == 0 || ans == 0){
                    num2 = Random.nextInt(MIN_HARD, MAX_HARD)
                    ans = Random.nextInt(MIN_HARD, MAX_HARD)
                }
                num1 = ans * num2
            }
        }

        // Finalize problem
        val problemInt = arrayOf(num1, num2, ans)
        val problemStr: Array<String>
        if (num2 < 0 && (operator == "*" || operator == "/")) {
            problemStr = arrayOf("$num1", "($num2)", "$ans")
        } else {
            problemStr = arrayOf("$num1", "$num2", "$ans")
        }

        // Print full problem to debug
        if (DEBUG) {
            val example = findViewById<TextView>(R.id.example)
            example.text = String.format("%s %s %s = %s", problemStr[0], operator, problemStr[1], problemStr[2]
            )
        }

        // Print hidden problem
        val position = POSITIONS[Random.nextInt(POSITIONS.size)]
        solution = problemInt[position]
        problemStr[position] = "?"

        val math = findViewById<TextView>(R.id.math)
        math.text = String.format("%s %s %s = %s", problemStr[0], operator, problemStr[1], problemStr[2])

        // Show score
        val scoreCounter = findViewById<TextView>(R.id.score)
        scoreCounter.text = String.format("%s %d", resources.getString(R.string.score_counter), score)
    }

    fun sendAnswer(v: View){
        val answer = findViewById<EditText>(R.id.input_answer)

        if (answer.text.toString() == "" || answer.text.toString() == "-"){
            return
        }

        if (answer.text.toString() == "$solution" && !lost){
            score++
            answer.text.clear()
            generateMathProblem()
        } else {
            lost = true
            finalDialog()
        }
    }

    override fun finish() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_return_title))
            .setMessage(resources.getString(R.string.dialog_return_text))
            .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ -> super.finish() }
            .setNegativeButton(resources.getString(R.string.dialog_no)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun finalDialog(){
        val result: String
        if (score <= this.resources.getInteger(R.integer.score1)){
            result = resources.getString(R.string.score_1)
        } else if (score <= this.resources.getInteger(R.integer.score2)){
            result = resources.getString(R.string.score_2)
        } else if (score <= this.resources.getInteger(R.integer.score3)){
            result = resources.getString(R.string.score_3)
        } else if (score <= this.resources.getInteger(R.integer.score4)){
            result = resources.getString(R.string.score_4)
        } else {
            result = resources.getString(R.string.score_5)
        }

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_final_title))
            .setMessage(String.format("%s %d\n%s", resources.getString(R.string.score_result), score, result))
            .setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ -> super.finish() }
            .show()
    }

    private fun loadPreferencesData(){
        val preferences : SharedPreferences = getSharedPreferences(resources.getString(R.string.shared_pref), 0)

        val positionBools = arrayOf(
            preferences.getBoolean(resources.getString(R.string.position1), resources.getBoolean(R.bool.position1_val)),
            preferences.getBoolean(resources.getString(R.string.position2), resources.getBoolean(R.bool.position2_val)),
            preferences.getBoolean(resources.getString(R.string.position3), resources.getBoolean(R.bool.position3_val))
        )
        val operatorBools = arrayOf(
            preferences.getBoolean(resources.getString(R.string.operation1), resources.getBoolean(R.bool.operation1_val)),
            preferences.getBoolean(resources.getString(R.string.operation2), resources.getBoolean(R.bool.operation2_val)),
            preferences.getBoolean(resources.getString(R.string.operation3), resources.getBoolean(R.bool.operation3_val)),
            preferences.getBoolean(resources.getString(R.string.operation4), resources.getBoolean(R.bool.operation4_val))
        )

        POSITIONS = mutableListOf()
        if (positionBools[0]){
            POSITIONS.add(0)
        }
        if (positionBools[1]){
            POSITIONS.add(1)
        }
        if (positionBools[2]){
            POSITIONS.add(2)
        }

        OPERATORS = mutableListOf()
        if (operatorBools[0]){
            OPERATORS.add("+")
        }
        if (operatorBools[1]){
            OPERATORS.add("-")
        }
        if (operatorBools[2]){
            OPERATORS.add("*")
        }
        if (operatorBools[3]){
            OPERATORS.add("/")
        }
        MIN_EZ = preferences.getInt(resources.getString(R.string.min1), resources.getInteger(R.integer.min1_val))
        MAX_EZ = preferences.getInt(resources.getString(R.string.max1), resources.getInteger(R.integer.max1_val))
        MIN_HARD = preferences.getInt(resources.getString(R.string.min2), resources.getInteger(R.integer.min2_val))
        MAX_HARD = preferences.getInt(resources.getString(R.string.max2), resources.getInteger(R.integer.max2_val))
        DEBUG = preferences.getBoolean(resources.getString(R.string.debug), resources.getBoolean(R.bool.debug_val))
    }
}