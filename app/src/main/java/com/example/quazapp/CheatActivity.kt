package com.example.quazapp

import android.os.Bundle
import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.app.Activity

class CheatActivity : AppCompatActivity() {
    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        showAnswerButton = findViewById(R.id.answer_button)
        answerTextView = findViewById(R.id.answer)

        showAnswerButton.setOnClickListener {
            showAnswerAndBack()
        }
    }

    private fun showAnswerAndBack() {
        val answerText = when (intent.getBooleanExtra(EXTRA_ANSWER, false)) {
            true -> getString(R.string.true_button_text)
            false -> getString(R.string.false_button_text)
        }
        answerTextView.text = answerText
        setResult(Activity.RESULT_OK, getResultIntent(true))
    }

    companion object {
        const val EXTRA_ANSWER = "com.example.quazapp.CheatActivity.answer"
        const val EXTRA_RESULT = "com.example.quazapp.CheatActivity.answer_showed"

        fun getAnswerIntent(packageContext: Context, answer: Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java)
                .putExtra(EXTRA_ANSWER, answer)
        }

        fun getResultIntent(result: Boolean) : Intent {
            return Intent()
                .putExtra(EXTRA_RESULT, result)
        }
    }
}