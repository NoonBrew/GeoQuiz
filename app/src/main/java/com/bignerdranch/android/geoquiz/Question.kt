package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes
// I really like how simple they made this class in the book. I assume a lot of the power of this
// object comes from the @StringRes notation.
data class Question (@StringRes val textResID: Int, val answer: Boolean, var isAnswered: Boolean)