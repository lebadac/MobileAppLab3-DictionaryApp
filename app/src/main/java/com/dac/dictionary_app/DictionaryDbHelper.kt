package com.dac.dictionary_app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLiteOpenHelper class to manage creation and versioning of the dictionary database
class DictionaryDbHelper(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    // Called when the database is first created
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE dictionary (word TEXT PRIMARY KEY, meaning TEXT)")
        insertInitialData(db)
    }

    // Inserts a predefined list of word-meaning pairs into the dictionary table
    private fun insertInitialData(db: SQLiteDatabase) {
        val words = listOf(
            "verisimilitude" to "The appearance of being true or real.",
            "vision" to "The ability to see.",
            "visible" to "Able to be seen.",
            "version" to "A particular form of something.",
            "virtue" to "High moral standards."
        )
        words.forEach { (word, meaning) ->
            db.execSQL("INSERT INTO dictionary (word, meaning) VALUES (?, ?)", arrayOf(word, meaning))
        }
    }

    // Called when the database needs to be upgraded (not implemented for version 1)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    // Searches for a word in the dictionary
    fun searchWord(query: String): List<Pair<String, String>> {
        val db = readableDatabase
        val result = mutableListOf<Pair<String, String>>()
        val cursor = db.rawQuery("SELECT word, meaning FROM dictionary WHERE word = ?", arrayOf(query))
        if (cursor.moveToFirst()) {
            result.add(cursor.getString(0) to cursor.getString(1))
        } else {
            cursor.close()
            val likeCursor = db.rawQuery("SELECT word, meaning FROM dictionary WHERE word LIKE ?", arrayOf("%$query%"))
            while (likeCursor.moveToNext()) {
                result.add(likeCursor.getString(0) to likeCursor.getString(1))
            }
            likeCursor.close()
        }
        cursor.close()
        return result
    }
}
