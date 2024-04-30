package com.example.mbkz_semestralka

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlin.random.Random

class Challenge : AppCompatActivity() {
    val OPERATORS = arrayOf("+", "-")   // PARA
    val MIN_EZ = -50                    // PARA
    val MAX_EZ = 50                     // PARA
    val MIN_HARD = 10                   // PARA
    val MAX_HARD = 10                   // PARA

    val SCORE1 = 2                      // CONST
    val SCORE2 = 4                      // CONST
    val SCORE3 = 6                      // CONST
    val SCORE4 = 8                      // CONST

    var SOLUTION = -1
    var SCORE = 0
    var LOST = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        // Reset variables
        SOLUTION = -1
        SCORE = 0
        LOST = false

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
        }
        if (operator == "-"){
            ans = Random.nextInt(MIN_EZ, MAX_EZ - 2)
            num1 = Random.nextInt(ans, MAX_EZ)
            num2 = num1 - ans
        }

//            if (num2 == 0 && operator == "/"){
//                continue
//            }
//            when (operator) {
//                "+" -> ans = num1 + num2
//                "-" -> ans = num1 - num2
//                "*" -> ans = num1 * num2
//                "/" -> ans = num1 / num2
//            }

        // TextView show
        val problem_int = arrayOf(num1, num2, ans)
        val problem_str = arrayOf("$num1", "$num2", "$ans")

        // DEBUG
        val example: TextView = findViewById(R.id.example)
        example.text = String.format("%s %s %s = %s", problem_str[0], operator, problem_str[1], problem_str[2])
        // DEBUG

        // TextView show
        val position = Random.nextInt(0, 3)
        SOLUTION = problem_int[position]
        problem_str[position] = "?"

        val math: TextView = findViewById(R.id.math)
        math.text = String.format("%s %s %s = %s", problem_str[0], operator, problem_str[1], problem_str[2])

        // Show score
        val scoreCounter: TextView = findViewById(R.id.score)
        scoreCounter.text = String.format("%s %d", resources.getString(R.string.score_counter), SCORE)
    }

    fun sendAnswer(v: View){
        val answer: EditText = findViewById(R.id.input_answer)

        if (answer.text.toString() == "" || answer.text.toString() == "-"){
            return
        }

        println(answer.text)
        println("$SOLUTION")

        if (answer.text.toString() == "$SOLUTION" && !LOST){
            SCORE++
            answer.text.clear()
            generateMathProblem()
        } else {
            LOST = true
            finalDialog()
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_return_title))
            .setMessage(resources.getString(R.string.dialog_return_text))
            .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(resources.getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun finalDialog(){
        val result: String
        if (SCORE <= SCORE1){
            result = resources.getString(R.string.score_1)
        } else if (SCORE <= SCORE2){
            result = resources.getString(R.string.score_2)
        } else if (SCORE <= SCORE3){
            result = resources.getString(R.string.score_3)
        } else if (SCORE <= SCORE4){
            result = resources.getString(R.string.score_4)
        } else {
            result = resources.getString(R.string.score_5)
        }

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.dialog_final_title))
            .setMessage(
                String.format("%s %d\n%s", resources.getString(R.string.score_result), SCORE, result))
            .setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ ->
                super.onBackPressed()
            }
            .show()
    }
}