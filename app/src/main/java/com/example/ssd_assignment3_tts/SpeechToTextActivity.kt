package com.example.ssd_assignment3_tts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import java.util.*
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

private const val REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1



class SpeechToTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION_CODE)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_to_text)

        val recognizedText: TextView = findViewById(R.id.recognized_text)
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        val backButton = findViewById<Button>(R.id.return_button)
        backButton.setOnClickListener {
            // Navigate back to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // This is a good place to update the UI to indicate that the app is ready for speech input
            }

            override fun onBeginningOfSpeech() {
                // This is a good place to update the UI to indicate that speech input has started
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                // This is a good place to update the UI to indicate that speech input has ended
            }

            override fun onError(error: Int) {
                // Handle error
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                    SpeechRecognizer.ERROR_SERVER -> "Error from server"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }

                // Display the error message, e.g., in a Toast or TextView
            }

            override fun onResults(results: Bundle?) {
                speechRecognizer.stopListening() // Stop listening when speech input has ended
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.size > 0) {
                    val text = matches[0]
                    Log.d("SpeechToTextActivity", "Recognized text: $text")
                    recognizedText.text = text // Update the TextView with the recognized text
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(recognitionListener)

        val startListeningButton: Button = findViewById(R.id.start_listening_button)
        startListeningButton.setOnClickListener {
            speechRecognizer.startListening(speechRecognizerIntent)
        }


        // Add your code for speech-to-text functionality here
    }
}