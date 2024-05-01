package com.example.mbkz_semestralka

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class Exercise : AppCompatActivity() {
    private var POSITIONS = mutableListOf<Int>()
    private var OPERATORS = mutableListOf<String>()
    private var TOTAL = 0
    private var MIN_EZ = 0
    private var MAX_EZ = 0
    private var MIN_HARD = 0
    private var MAX_HARD = 0
    private var DEBUG = false

    private var solution = 0
    private var animating = false
    private var problem = 0
    private var wrong = 0
    private var wrongGuess = arrayOf(false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Initialize variables
        loadPreferencesData()
        solution = 0
        animating = false
        problem = 0
        wrong = 0
        wrongGuess = arrayOf(false, false, false, false)

        // Show wrong answer count
        findViewById<TextView>(R.id.score).text = String.format("%s %d", resources.getString(R.string.wrong_counter), wrong)

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

        // Randomize answers
        val answers = arrayOf(solution, solution + Random.nextInt(1, 4), solution - Random.nextInt(1, 4), solution + Random.nextInt(4, 7))
        answers.shuffle()

        val answer1 = answers[0]
        val answer2 = answers[1]
        val answer3 = answers[2]
        val answer4 = answers[3]

        // Buttons show
        animating = false

        val ans1 = findViewById<Button>(R.id.ans1)
        val ans2 = findViewById<Button>(R.id.ans2)
        val ans3 = findViewById<Button>(R.id.ans3)
        val ans4 = findViewById<Button>(R.id.ans4)

        ans1.text = "$answer1"
        ans2.text = "$answer2"
        ans3.text = "$answer3"
        ans4.text = "$answer4"

        ans1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans2.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans3.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans4.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        // Show problem count
        problem++
        val problemCounter = findViewById<TextView>(R.id.problem)
        problemCounter.text = String.format("%s %d/%d", resources.getString(R.string.problem_counter), problem, TOTAL)
    }

    fun guess(v: View){
        // Animation timer
        if (animating){
            return
        }

        // Compare answer to solution
        val guess: Button = v as Button
        if (guess.text == "$solution"){
            guess.setBackgroundColor(resources.getColor(R.color.answerCorrect))
            animating = true
            Handler(Looper.getMainLooper()).postDelayed({
                if (TOTAL > problem){
                    wrongGuess = arrayOf(false, false, false, false)
                    generateMathProblem()
                } else {
                    showFinalDialog()
                }
            }, 1500)
        } else {
            if (guess.id == R.id.ans1 && !wrongGuess[0]){
                wrong += 1
                wrongGuess[0] = true
            } else if (guess.id == R.id.ans2 && !wrongGuess[1]){
                wrong += 1
                wrongGuess[1] = true
            } else if (guess.id == R.id.ans3 && !wrongGuess[2]){
                wrong += 1
                wrongGuess[2] = true
            } else if (guess.id == R.id.ans4 && !wrongGuess[3]){
                wrong += 1
                wrongGuess[3] = true
            }

            // Show wrong answer highlight and count
            findViewById<TextView>(R.id.score).text = String.format("%s %d", resources.getString(R.string.wrong_counter), wrong)
            guess.setBackgroundColor(resources.getColor(R.color.answerWrong))
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

    private fun showFinalDialog(){
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_final_title))
            .setMessage(String.format("%s %d\n%s %d", resources.getString(R.string.dialog_final_text1), TOTAL, resources.getString(R.string.dialog_final_text2), wrong))
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
        TOTAL = preferences.getInt(resources.getString(R.string.problems), resources.getInteger(R.integer.problems_val))
        MIN_EZ = preferences.getInt(resources.getString(R.string.min1), resources.getInteger(R.integer.min1_val))
        MAX_EZ = preferences.getInt(resources.getString(R.string.max1), resources.getInteger(R.integer.max1_val))
        MIN_HARD = preferences.getInt(resources.getString(R.string.min2), resources.getInteger(R.integer.min2_val))
        MAX_HARD = preferences.getInt(resources.getString(R.string.max2), resources.getInteger(R.integer.max2_val))
        DEBUG = preferences.getBoolean(resources.getString(R.string.debug), resources.getBoolean(R.bool.debug_val))
    }
}