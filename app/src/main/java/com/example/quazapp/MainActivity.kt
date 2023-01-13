package com.example.quazapp

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.os.Build
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

import com.example.quazapp.model.Question
import com.example.quazapp.model.QuestionsRepository

class MainActivity : AppCompatActivity(){
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionText: TextView
    private val quests by lazy {
        ViewModelProvider(this)[QuestionsRepository::class.java]
    }

    private var correctedCount = 0
    private var cheatUsage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        /*savedInstanceState?.let {
            quests.resetTo(it.getInt(BUNDLE_INDEX_KEY, 0))
            cheatUsage = it.getBoolean(BUNDLE_CHEAT_KEY, false)
        }*/

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prevButton = findViewById<MaterialButton>(R.id.prev_button)
        nextButton = findViewById<MaterialButton>(R.id.next_button)
        trueButton = findViewById<MaterialButton>(R.id.true_button)
        falseButton = findViewById<MaterialButton>(R.id.false_button)
        cheatButton = findViewById<MaterialButton>(R.id.cheat_button)
        questionText = findViewById(R.id.question_text)

        prevButton.setOnClickListener { showPrevQuestion() }
        nextButton.setOnClickListener { showNextQuestion() }
        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }
        cheatButton.setOnClickListener { toShowAnswer() }

        showQuest(quests.quest())

        findViewById<TextView>(R.id.api_lvl).text = getString(R.string.api_lvl_text, Build.VERSION.SDK_INT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE_CHEAT) return
        if (resultCode != Activity.RESULT_OK) return

        cheatUsage = data?.getBooleanExtra(CheatActivity.EXTRA_RESULT, false) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_INDEX_KEY, quests.questInd())
        outState.putBoolean(BUNDLE_CHEAT_KEY, cheatUsage)
    }

    private fun showPrevQuestion() {
        val quest = quests.previousQuest()

        if (quest == null){
            Toast.makeText(this, R.string.prev_not_exist, Toast.LENGTH_SHORT)
                .show()
            return
        }

        showQuest(quest)
    }

    private fun showNextQuestion() {
        var quest = quests.nextQuest()

        if (quest == null){
            Toast.makeText(this, getString(R.string.correct_answers, correctedCount), Toast.LENGTH_LONG)
                .show()
            correctedCount = 0
            quests.resetTo(0)
            quest = quests.nextQuest()
        }

        cheatUsage = false
        showQuest(quest!!)
    }

    private fun checkAnswer(answer: Boolean) {
        val quest = quests.quest()

        val viewText = if (quest.answer == answer) R.string.correct_answer else R.string.uncorrected_answer

        Toast.makeText(this, viewText, Toast.LENGTH_SHORT)
            .show()

        if (answer == quest.answer && !cheatUsage) ++correctedCount
    }

    private fun toShowAnswer() {
        val intent = CheatActivity.getAnswerIntent(this, quests.quest().answer)
        startActivityForResult(intent, REQUEST_CODE_CHEAT)
    }

    private fun showQuest(quest: Question) {
        questionText.text = getString(quest.questionId)
    }

    companion object {
        const val REQUEST_CODE_CHEAT = 0

        const val BUNDLE_INDEX_KEY = "index"
        const val BUNDLE_CHEAT_KEY = "cheat_usage"
    }
}