package uz.gita.latizx.uz_eng.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechToTextHelper(private val context: Context) {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechRecognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    // Natijalar kelganda chaqiriladigan callback
    var onSpeechResults: (List<String>) -> Unit = {}

    init {
        // SpeechRecognizer listener ni o'rnatamiz
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                // Speech-to-text natijalarini olish
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) ?: arrayListOf()
                if (matches.isNotEmpty()) {
                    onSpeechResults(matches)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    // Qidiruvni boshlash metodini yaratamiz
    fun startListening(callback: (List<String>) -> Unit) {
        onSpeechResults = callback // Natija kelganda callbackni o'rnatamiz
        speechRecognizer.startListening(speechRecognizerIntent) // Speech recognizer boshlanadi
    }

    // Stop qilish
    fun stopListening() {
        speechRecognizer.stopListening()
    }

    // Destroy qilish
    fun destroy() {
        speechRecognizer.destroy()
    }
}
