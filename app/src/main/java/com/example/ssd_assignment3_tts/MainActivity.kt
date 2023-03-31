package com.example.ssd_assignment3_tts

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var inputText: EditText
    private lateinit var speakButton: Button
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.input_text)
        speakButton = findViewById(R.id.speak_button)
        textToSpeech = TextToSpeech(this, this)

        speakButton.setOnClickListener {
            val text = inputText.text.toString()
            speakOut(text)
        }
        val speechToTextButton: Button = findViewById(R.id.go_to_speech_to_text_demo_button)
        speechToTextButton.setOnClickListener {
            val intent = Intent(this, SpeechToTextActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.CANADA)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("Language not supported")
            }
        } else {
            println("Initialization failed")
        }
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}