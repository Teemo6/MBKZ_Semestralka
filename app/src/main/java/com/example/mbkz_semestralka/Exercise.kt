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
    val OPERATORS = arrayOf("+", "-")   // PARA
    val TOTAL = 5                       // PARA
    val MIN_EZ = -50                    // PARA
    val MAX_EZ = 50                     // PARA
    val MIN_HARD = 10                   // PARA
    val MAX_HARD = 10                   // PARA

    var SOLUTION = -1
    var ANIMATION = false
    var PROBLEM = 0
    var WRONG = 0
    var WRONG_GUESS = arrayOf(false, false, false ,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Reset variables
        SOLUTION = -1
        ANIMATION = false
        PROBLEM = 0
        WRONG = 0
        WRONG_GUESS = arrayOf(false, false, false ,false)

        // Setup wrong counter
        val wrongCounter: TextView = findViewById(R.id.score)
        wrongCounter.text = String.format("%s %d", resources.getString(R.string.wrong_counter), 0)

        // Start generating math problems
        this.generateMathProblem()
    }

    fun generateMathProblem(){
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
        // DEBUG

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

        ANIMATION = false

        // Show problem count
        PROBLEM++
        val problemCounter: TextView = findViewById(R.id.problem)
        problemCounter.text = String.format("%s %d/%d", this.resources.getString(R.string.problem_counter), PROBLEM, TOTAL)

    }

    fun guess(v: View){
        // Animation timer
        if (ANIMATION){
            return
        }

        val guess: Button = v as Button
        val answer: TextView = findViewById(R.id.answer)
        val wrongCounter: TextView = findViewById(R.id.score)

        if (guess.text == "$SOLUTION"){
            // DEBUG
            answer.text = String.format("Correct: %s", guess.text)
            // DEBUG

            guess.setBackgroundColor(resources.getColor(R.color.answerCorrect))
            ANIMATION = true
            Handler(Looper.getMainLooper()).postDelayed({
                if (TOTAL > PROBLEM){
                    WRONG_GUESS = arrayOf(false, false, false, false)
                    this.generateMathProblem()
                } else {
                    finalDialog()
                }
            }, 1000)
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

            // DEBUG
            answer.text = String.format("Not correct: %s", guess.text)
            // DEBUG

            wrongCounter.text = String.format("%s %d", resources.getString(R.string.wrong_counter), WRONG)
            guess.setBackgroundColor(resources.getColor(R.color.answerWrong))
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(this.resources.getString(R.string.dialog_return_title))
            .setMessage(this.resources.getString(R.string.dialog_return_text))
            .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(this.resources.getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun finalDialog(){
        AlertDialog.Builder(this)
            .setTitle(this.resources.getString(R.string.dialog_final_title))
            .setMessage(
                String.format("%s %d\n%s %d",
                    this.resources.getString(R.string.dialog_final_text1), TOTAL,
                    this.resources.getString(R.string.dialog_final_text2), WRONG))
            .setPositiveButton(resources.getString(R.string.dialog_ok)) { dialog, _ ->
                super.onBackPressed()
            }
            .show()
    }
}