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
    var POSITIONS = mutableListOf<Int>()
    var OPERATORS = mutableListOf<String>()
    var TOTAL = 0
    var MIN_EZ = 0
    var MAX_EZ = 0
    var MIN_HARD = 0
    var MAX_HARD = 0
    var DEBUG = false

    var SOLUTION = -1
    var ANIMATION = false
    var PROBLEM = 0
    var WRONG = 0
    var WRONG_GUESS = emptyArray<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Initialize variables
        loadPreferencesData()

        SOLUTION = -1
        ANIMATION = false
        PROBLEM = 0
        WRONG = 0
        WRONG_GUESS = emptyArray<Boolean>()

        // Setup wrong counter
        val wrongCounter: TextView = findViewById(R.id.score)
        wrongCounter.text = String.format("%s %d", resources.getString(R.string.wrong_counter), 0)

        // Start generating math problems
        generateMathProblem()
    }

    fun generateMathProblem(){
        // Reset variables
        var num1 = -1
        var num2 = -1
        var ans = Int.MAX_VALUE

        // Generate new problem
        val operator = OPERATORS[Random.nextInt(OPERATORS.size)]
        if (operator == "+"){
            ans = Random.nextInt(MIN_EZ + 2, MAX_EZ)
            num1 = Random.nextInt(MIN_EZ, ans)
            num2 = ans - num1
        } else if (operator == "-"){
            ans = Random.nextInt(MIN_EZ, MAX_EZ - 2)
            num1 = Random.nextInt(ans, MAX_EZ)
            num2 = num1 - ans
        } else if (operator == "*"){
            num1 = Random.nextInt(MIN_HARD, MAX_HARD)
            num2 = Random.nextInt(MIN_HARD, MAX_HARD)
            while(num1 == 0 || num2 == 0){
                num1 = Random.nextInt(MIN_HARD, MAX_HARD)
                num2 = Random.nextInt(MIN_HARD, MAX_HARD)
            }
            ans = num1 * num2
        } else if (operator == "/"){
            num2 = Random.nextInt(MIN_HARD, MAX_HARD)
            ans = Random.nextInt(MIN_HARD, MAX_HARD)
            while(num2 == 0 || ans == 0){
                num2 = Random.nextInt(MIN_HARD, MAX_HARD)
                ans = Random.nextInt(MIN_HARD, MAX_HARD)
            }
            num1 = ans * num2
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
            val example: TextView = findViewById(R.id.example)
            example.text = String.format("%s %s %s = %s", problemStr[0], operator, problemStr[1], problemStr[2]
            )
        }

        // Print hidden problem
        val position = POSITIONS[Random.nextInt(POSITIONS.size)]
        SOLUTION = problemInt[position]
        problemStr[position] = "?"

        val math: TextView = findViewById(R.id.math)
        math.text = String.format("%s %s %s = %s", problemStr[0], operator, problemStr[1], problemStr[2])

        // Randomize answers
        val answers = arrayOf(SOLUTION, SOLUTION + Random.nextInt(1, 4), SOLUTION - Random.nextInt(1, 4), SOLUTION + Random.nextInt(4, 7))
        answers.shuffle()

        val answer1 = answers[0]
        val answer2 = answers[1]
        val answer3 = answers[2]
        val answer4 = answers[3]

        // Buttons show
        ANIMATION = false

        val ans1: Button = findViewById(R.id.ans1)
        val ans2: Button = findViewById(R.id.ans2)
        val ans3: Button = findViewById(R.id.ans3)
        val ans4: Button = findViewById(R.id.ans4)

        ans1.text = "$answer1"
        ans2.text = "$answer2"
        ans3.text = "$answer3"
        ans4.text = "$answer4"

        ans1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans2.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans3.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        ans4.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        // Show problem count
        PROBLEM++
        val problemCounter: TextView = findViewById(R.id.problem)
        problemCounter.text = String.format("%s %d/%d", resources.getString(R.string.problem_counter), PROBLEM, TOTAL)
    }

    fun guess(v: View){
        // Animation timer
        if (ANIMATION){
            return
        }

        val guess: Button = v as Button
        val wrongCounter: TextView = findViewById(R.id.score)

        if (guess.text == "$SOLUTION"){
            guess.setBackgroundColor(resources.getColor(R.color.answerCorrect))
            ANIMATION = true
            Handler(Looper.getMainLooper()).postDelayed({
                if (TOTAL > PROBLEM){
                    WRONG_GUESS = arrayOf(false, false, false, false)
                    generateMathProblem()
                } else {
                    finalDialog()
                }
            }, 1500)
        } else {
            if (guess.id == R.id.ans1 && !WRONG_GUESS[0]){
                WRONG += 1
                WRONG_GUESS[0] = true
            } else if (guess.id == R.id.ans2 && !WRONG_GUESS[1]){
                WRONG += 1
                WRONG_GUESS[1] = true
            } else if (guess.id == R.id.ans3 && !WRONG_GUESS[2]){
                WRONG += 1
                WRONG_GUESS[2] = true
            } else if (guess.id == R.id.ans4 && !WRONG_GUESS[3]){
                WRONG += 1
                WRONG_GUESS[3] = true
            }

            wrongCounter.text = String.format("%s %d", resources.getString(R.string.wrong_counter), WRONG)
            guess.setBackgroundColor(resources.getColor(R.color.answerWrong))
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_return_title))
            .setMessage(resources.getString(R.string.dialog_return_text))
            .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ -> super.onBackPressed() }
            .setNegativeButton(resources.getString(R.string.dialog_no)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun finalDialog(){
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_final_title))
            .setMessage(String.format("%s %d\n%s %d", resources.getString(R.string.dialog_final_text1), TOTAL, resources.getString(R.string.dialog_final_text2), WRONG))
            .setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ -> super.onBackPressed() }
            .show()
    }

    fun loadPreferencesData(){
        val preferences : SharedPreferences = getSharedPreferences(resources.getString(R.string.shared_pref), 0)

        val POSITIONS_BOOL = arrayOf(
            preferences.getBoolean(resources.getString(R.string.position1), resources.getBoolean(R.bool.position1_val)),
            preferences.getBoolean(resources.getString(R.string.position2), resources.getBoolean(R.bool.position2_val)),
            preferences.getBoolean(resources.getString(R.string.position3), resources.getBoolean(R.bool.position3_val))
        )
        val OPERATORS_BOOL = arrayOf(
            preferences.getBoolean(resources.getString(R.string.operation1), resources.getBoolean(R.bool.operation1_val)),
            preferences.getBoolean(resources.getString(R.string.operation2), resources.getBoolean(R.bool.operation2_val)),
            preferences.getBoolean(resources.getString(R.string.operation3), resources.getBoolean(R.bool.operation3_val)),
            preferences.getBoolean(resources.getString(R.string.operation4), resources.getBoolean(R.bool.operation4_val))
        )

        POSITIONS = mutableListOf()
        if (POSITIONS_BOOL[0]){
            POSITIONS.add(0)
        }
        if (POSITIONS_BOOL[1]){
            POSITIONS.add(1)
        }
        if (POSITIONS_BOOL[2]){
            POSITIONS.add(2)
        }

        OPERATORS = mutableListOf()
        if (OPERATORS_BOOL[0]){
            OPERATORS.add("+")
        }
        if (OPERATORS_BOOL[1]){
            OPERATORS.add("-")
        }
        if (OPERATORS_BOOL[2]){
            OPERATORS.add("*")
        }
        if (OPERATORS_BOOL[3]){
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