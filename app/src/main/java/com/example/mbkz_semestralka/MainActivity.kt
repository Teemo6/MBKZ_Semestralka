package com.example.mbkz_semestralka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val OPERATORS = arrayOf("+", "-")
    var SOLUTION = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun generateMathProblem(){
        val MAX_EZ = 100
        val MAX_HARD = 10

        var operator = ""
        var num1 = -1
        var num2 = -1
        var ans = -1
        while(ans < 0 || ans !is Int){
            operator = OPERATORS[Random.nextInt(OPERATORS.size)]
            if (operator == "+"){
                ans = Random.nextInt(2, MAX_EZ)
                num1 = Random.nextInt(0, ans)
                num2 = ans - num1
            }
            if (operator == "-"){
                ans = Random.nextInt(0, MAX_EZ - 2)
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
        }

        // TextView show
        val problem_int = arrayOf(num1, num2, ans)
        val problem_str = arrayOf("$num1", "$num2", "$ans")

        val example: TextView = findViewById(R.id.example)
        example.text = String.format("%s %s %s = %s", problem_str[0], operator, problem_str[1], problem_str[2])

        val position = Random.nextInt(0, 3)
        SOLUTION = problem_int[position]
        problem_str[position] = "?"

        // Randomize answers
        val answers = arrayOf(SOLUTION, SOLUTION + Random.nextInt(1, 5), SOLUTION - Random.nextInt(1, 5), SOLUTION + Random.nextInt(5, 10))
        answers.shuffle()

        val answer1 = answers[0]
        val answer2 = answers[1]
        val answer3 = answers[2]
        val answer4 = answers[3]

        val math: TextView = findViewById(R.id.math)
        math.text = String.format("%s %s %s = %s", problem_str[0], operator, problem_str[1], problem_str[2])

        val answer: TextView = findViewById(R.id.answer)
        answer.text = ""

        // Buttons show
        val ans1: TextView = findViewById(R.id.ans1)
        val ans2: TextView = findViewById(R.id.ans2)
        val ans3: TextView = findViewById(R.id.ans3)
        val ans4: TextView = findViewById(R.id.ans4)

        ans1.text = "$answer1"
        ans2.text = "$answer2"
        ans3.text = "$answer3"
        ans4.text = "$answer4"
    }

    fun guess(v: View){
        val guess = v as Button

        val answer: TextView = findViewById(R.id.answer)
        if (guess.text == "$SOLUTION"){
            answer.text = String.format("Correct: %s", guess.text)
        } else {
            answer.text = String.format("Not correct: %s", guess.text)
        }
    }
}