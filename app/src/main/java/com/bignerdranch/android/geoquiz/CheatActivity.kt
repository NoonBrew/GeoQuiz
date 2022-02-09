package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_ANSWER_SHOWN =
    "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.bignerdranch.android.geoquiz.answer_is_true"
private const val CHEAT_STATUS_KEY = "cheat-status-key"


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    // Private var used to assign Boolean type for saving outState Bundle data.
    private var userCheated = false
    // Private var used to hold the answer of the question passed from the view model index.
    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        // Stores the value of the key-value pair extra passed with the intent from the main activity
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        // Stores are saved instance state that is passed when the activity is rotated.
        // This allows us to close a loop-hole that would allow a user to rotate the app before pressing
        // back to remove the cheating status.
        val answerWasShown =
            savedInstanceState?.getBoolean(CHEAT_STATUS_KEY, false) ?: false
        // Call our setAnswerShownResult with the saved instance state.
        setAnswerShownResult(answerWasShown)


        showAnswerButton.setOnClickListener {
            // Reads the Boolean extra passed with extras and assigns an string resource
            // based on if it is true or false.
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            // reads the string resource and sets the text view based on it.
            answerTextView.setText(answerText)
            // passes true to our function to let the parent activity know the user cheated.
            setAnswerShownResult(true)

        }
    }
    // Sends data back to the parent telling if the user checked the answer.
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        // setResult() tells the state of how the child activity ended.
        setResult(Activity.RESULT_OK, data)
        // sets our variable to true so that it can be sent with an outState bundle.
        userCheated = true
    }

    companion object { // allows for access of functions with out instantiation
        // This function can be called and passed a context and a Boolean and returns an intent
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    } // end of common object.

    // onSaveInstanceState() allows us to monitor the cheat status through different life cycles.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CHEAT_STATUS_KEY, userCheated)
    }
}