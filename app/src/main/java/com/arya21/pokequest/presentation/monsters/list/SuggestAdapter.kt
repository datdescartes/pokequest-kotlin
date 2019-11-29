package com.arya21.pokequest.presentation.monsters.list

import android.content.Context
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.arya21.pokequest.R

private const val SUGGEST_COLUMN = "keyword"

class SuggestAdapter(context: Context?) : SimpleCursorAdapter(
    context,
    R.layout.rv_item_suggestion,
    null,
    arrayOf(SUGGEST_COLUMN),
    intArrayOf(R.id.textview),
    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
) {
    private var suggestions: List<String> = listOf()
    fun setSuggestions(suggestions: List<String>) {
        val c = MatrixCursor(
            arrayOf(
                BaseColumns._ID,
                SUGGEST_COLUMN
            )
        )
        suggestions.forEachIndexed { index, s -> c.addRow(listOf(index, s)) }
        changeCursor(c)
        this.suggestions = suggestions
    }

    fun getItemAt(position: Int): String? {
        return suggestions[position]
    }
}