package com.bignerdranch.android.geoquiz


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: Button
    // Creates a list of objects created with our Question class Model
    // We pass the Question class a resource location and the answer.
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    // Creates an index we will use to iterate over the list.
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (currentIndex > 0) {
                currentIndex -= 1
                updateQuestion()
            } else {
                //Other wise if the index is at 0 we get the index of the last element in our list
                currentIndex = questionBank.indexOf(questionBank.last())
                // We do this so we never go out of bounds of the range of our list.
                updateQuestion()
            }


        }

        updateQuestion()
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
    }
    // Takes a boolean and compares that with the boolean stored in our Class Model.
    private fun checkAnswer(userAnswer: Boolean) {
        // Gets the boolean at the current index stored in our list.
        val correctAnswer = questionBank[currentIndex].answer
        // Compares the user selected answer to the one stored in our list.
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}