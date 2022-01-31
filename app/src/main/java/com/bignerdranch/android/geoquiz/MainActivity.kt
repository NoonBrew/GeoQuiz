package com.bignerdranch.android.geoquiz


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton
    // Creates a list of objects created with our Question class Model
    // We pass the Question class a resource location and the answer.
    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false))
    // Creates an index we will use to iterate over the list.
    private var currentIndex = 0
    private var questionsRight = 0
    private var questionsAnswered = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        questionTextView.setOnClickListener {
            // If text is clicked we get the next question and update the question shown.
            nextQuestion()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            // If next is clicked grab the next question and update text view
            nextQuestion()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            // Previous button checks the current Index, if we are not at index 0
            // we minus 1 from the index and update the question.
            currentIndex -= 1
            if (currentIndex == -1) {
                currentIndex = questionBank.indexOf(questionBank.last())
            }
            updateQuestion()
        }

        updateQuestion()
        buttonToggle()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun nextQuestion() {
        // Add one to the index and than modular divide it by the size of our list.
        // This allows us to loop back over our list by setting the index back to 0
        // when it equals the size of the list.
        currentIndex = (currentIndex + 1) % questionBank.size

    }

    // Function that gets the string resource value stored at the current index of the list
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResID
        // Sets the text view with the string at that resource location.
        questionTextView.setText(questionTextResId)
        buttonToggle()
    }

    // Takes a boolean and compares that with the boolean stored in our Class Model.
    private fun checkAnswer(userAnswer: Boolean) {
        // Gets the boolean at the current index stored in our list.
        val correctAnswer = questionBank[currentIndex].answer
        // Compares the user selected answer to the one stored in our list.
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            questionsRight++
        } else {
            messageResId = R.string.incorrect_toast
        }
        questionsAnswered++
        answered()
        buttonToggle()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizScore()
    }

    private fun answered() {
        questionBank[currentIndex].isAnswered = true
    }

    private fun buttonToggle() {
        if(questionBank[currentIndex].isAnswered){
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun quizScore(){
        if (questionsAnswered == (questionBank.size)) {
            val totalScore = ((questionsRight * 100)/questionsAnswered).toDouble()
            Toast.makeText(this, "Score $totalScore%", Toast.LENGTH_LONG).show()
        }
    }

}