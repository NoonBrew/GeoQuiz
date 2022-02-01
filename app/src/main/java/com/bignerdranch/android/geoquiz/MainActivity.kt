package com.bignerdranch.android.geoquiz


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val KEY_INDEX = "index"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton
    // Creates a list of objects created with our Question class Model
    // We pass the Question class a resource location and the answer.

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        // Loads the index value stored in our KEY_INDEX key and sets it as the current question to
        // display. If their was no index saved we default the index to 0.
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        // houses are resource setup.
        findViewSetup()


        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        questionTextView.setOnClickListener {
            // If text is clicked we get the next question and update the question shown.
            quizViewModel.moveToNext()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            // If next is clicked grab the next question and update text view
            quizViewModel.moveToNext()
            // updates the question with the new index.
            updateQuestion()
            // Calls our quizScore method to see if it is time to give our user their score.
            quizScore()
        }

        prevButton.setOnClickListener {
            // calls a function from the ViewModel to move the index
            quizViewModel.moveToPrev()
            // updates the question with the changed index.
            updateQuestion()
            // Calls our quizScore method to see if it is time to give our user their score.
            quizScore()
        }
        // Updates question, useful for rotation and pausing.
        updateQuestion()
        // Same as update question, useful for rotation and pausing.
        buttonToggle()
    }
    // Sets up our resources, I find this makes everything a little cleaner.
    private fun findViewSetup() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
    }
    // Test logs from the book.
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
    // Saves the index of our current question in the event of the home button being pressed
    // Protects our data as long as the back button isn't pressed.
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    // Function that gets the string resource value stored at the current index of the list
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        // Sets the text view with the string at that resource location.
        questionTextView.setText(questionTextResId)
        // calls our button toggle to check when a question is updated if it has been answered.
        buttonToggle()
    }

    // Takes a boolean and compares that with the boolean stored in our Class Model.
    private fun checkAnswer(userAnswer: Boolean) {
        // Gets the boolean at the current index stored in our list.
        val correctAnswer = quizViewModel.currentQuestionAnswer
        // pulled the initialization out of the if statement so that it could do more.
        val messageResId: Int
        // Compares the user selected answer to the one stored in our list.
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            //Increments our question right variable to keep track of how many correct guesses.
            quizViewModel.questionsRight++
        } else {
            messageResId = R.string.incorrect_toast
        }
        quizViewModel.questionsAnswered++
        //This calls a method on our ViewModel to set a question to answered.
        quizViewModel.answered()
        // Calls are button toggle after every guess to see if a button should be disabled.
        buttonToggle()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        // Calls are quizScore function after every guess to check if the quiz is completed.

    }



    private fun buttonToggle() {
        // Checks to see if a question has already been answered.
        if(quizViewModel.currentQuestionIsAnswered){
            // Questions that have been answered will have their guess buttons disabled.
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            // if the question has not been answered we will leave the buttons enabled.
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    // Want to change this to a snack bar in the future. This function should probably be moved to
    // Could move to the ViewModel but was unsure of how to control displaying the toast message
    // And did not know how to call a toast message from the ViewModel on the mainActivity
    private fun quizScore(){
        // Checks to see if the questions answered is equal to the size of our quiz.
        if (quizViewModel.questionsAnswered == quizViewModel.numberOfQuestions) {
            // If we have answered all the questions the user will have their number correct
            // multiplied by 100 and divided by the number of questions they answered
            // Also we cast to a Double so that it will display more like a percent.
            val totalScore =
                ((quizViewModel.questionsRight * 100)/quizViewModel.questionsAnswered).toDouble()
            // This toast will tell the user their percent score
            Toast.makeText(this, "Score $totalScore%", Toast.LENGTH_LONG).show()
        }
    }

}