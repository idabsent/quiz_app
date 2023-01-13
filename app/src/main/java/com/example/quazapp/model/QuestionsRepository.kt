package com.example.quazapp.model

import com.example.quazapp.R
import androidx.lifecycle.ViewModel

class QuestionsRepository : ViewModel() {
    private val quests = listOf(
        Question(R.string.quest_1, true),
        Question(R.string.quest_2, false),
        Question(R.string.quest_3, false),
        Question(R.string.quest_4, true),
        Question(R.string.quest_5, true),
    )

    private var questIterator = quests.listIterator(0)

    fun nextQuest() : Question? {
        return when (questIterator.hasNext()) {
            true -> questIterator.next()
            false -> null
        }
    }

    fun quest() : Question {
        val nextInd = questIterator.nextIndex()
        return quests[if (nextInd > 0) nextInd - 1 else nextInd]
    }

    fun questInd() : Int {
        return questIterator.nextIndex() - 1
    }

    fun previousQuest() : Question? {
        return when (questIterator.hasPrevious()) {
            true -> questIterator.previous()
            false -> null
        }
    }

    fun resetTo(index: Int) {
        questIterator = quests.listIterator(index)
    }
}