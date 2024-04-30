package com.example.mbkz_semestralka

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class Exercise : AppCompatActivity() {
    val OPERATORS = arrayOf("+", "-")
    var SOLUTION = -1
    var LOADING = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        this.generateMathProblem()
    }

    fun generateMathProblem(){
        val MIN_EZ = -50            // PARA 1
        val MAX_EZ = 50             // PARA 2
        val MAX_HARD = 10

        var operator = ""
        var num1 = -1
        var num2 = -1
        var ans = Int.MAX_VALUE

        operator = OPERATORS[Random.nextInt(OPERATORS.size)]
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

        val answer: TextView = findViewById(R.id.answer)
        answer.text = ""

        // TextView show
        val position = Random.nextInt(0, 3)
        SOLUTION = problem_int[position]
        problem_str[position] = "?"

        val math: TextView = findViewById(R.id.math)
        math.text = String.format("%s %s %s = %s", problem_str[0], operator, problem_str[1], problem_str[2])

        // Randomize answers
        val answers = arrayOf(SOLUTION, SOLUTION + Random.nextInt(1, 4), SOLUTION - Random.nextInt(1, 4), SOLUTION + Random.nextInt(4, 7))
        answers.shuffle()

        val answer1 = answers[0]
        val answer2 = answers[1]
        val answer3 = answers[2]
        val answer4 = answers[3]

        // Buttons show
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

        LOADING = false
    }

    fun guess(v: View){
        val guess = v as Button

        if (LOADING){
            return
        }

        val answer: TextView = findViewById(R.id.answer)
        if (guess.text == "$SOLUTION"){
            answer.text = String.format("Correct: %s", guess.text)
            guess.setBackgroundColor(resources.getColor(R.color.answerCorrect))
            LOADING = true
            Handler(Looper.getMainLooper()).postDelayed({
                this.generateMathProblem()
            }, 1000)
        } else {
            answer.text = String.format("Not correct: %s", guess.text)
            guess.setBackgroundColor(resources.getColor(R.color.answerWrong))
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Návrat do menu")
            .setMessage("Opravdu se chcete vrátit do menu?")
            .setPositiveButton("Ano") { dialog, _ ->
                // Perform any necessary cleanup or exit actions
                super.onBackPressed()
            }
            .setNegativeButton("Ne") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}