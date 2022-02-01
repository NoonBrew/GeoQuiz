package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    var currentIndex = 0
    var questionsRight = 0
    var questionsAnswered = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false))

    // How the book set up getter methods. Grabs the answer stored at the current index.
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    // Grabs the resource ID of the question stored at the index.
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResID
    // Grabs the answered status of the question stored at the index.
    val currentQuestionIsAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered
    // Grabs the size of the list.
    val numberOfQuestions: Int
        get() = questionBank.size


    fun moveToNext() {
        // Add one to the index and than modular divide it by the size of our list.
        // This allows us to loop back over our list by setting the index back to 0
        // when it equals the size of the list.
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        // Previous button reduces the index by one then checks if we are
        // at -1 index we set the current index to the last question
        currentIndex -= 1
        if (currentIndex == -1) {
            currentIndex = questionBank.indexOf(questionBank.last())
        }
    }
    // Sets the isAnswered Status to false so we do not answer a question more than once.
    fun answered() {
        questionBank[currentIndex].isAnswered = true
    }

}