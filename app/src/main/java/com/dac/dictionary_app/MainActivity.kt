package com.dac.dictionary_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// Main activity that provides a simple dictionary lookup UI
class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DictionaryDbHelper
    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var resultView: TextView

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DictionaryDbHelper(this)
        editText = findViewById(R.id.editWord)
        button = findViewById(R.id.btnLookup)
        resultView = findViewById(R.id.txtResult)

        button.setOnClickListener {
            val input = editText.text.toString().trim()
            if (input.isEmpty()) {
                resultView.text = "Please enter a word."
                return@setOnClickListener
            }

            val results = dbHelper.searchWord(input)
            resultView.text = if (results.isEmpty()) {
                "No match found."
            } else {
                results.joinToString("\n\n") { "• ${it.first}:\n${it.second}" }
            }
        }
    }
}
