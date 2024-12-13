package uz.gita.latizx.uz_eng.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uz.gita.latizx.uz_eng.R
import kotlin.random.Random

class SpeechToTextHelper(
    private val context: Context,
    private var fabAnimation: FloatingActionButton
) {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    // Natijalar kelganda chaqiriladigan callback
    var onSpeechResults: (List<String>) -> Unit = {}

    companion object {
        const val QUIT_RMSDB_MAX = 3.0f
        const val MEDIUM_RMSDB_MAX = 6.0f
    }

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {
                fabAnimation.let { fab ->
                    val newHeightPart = when {
                        rmsdB < QUIT_RMSDB_MAX -> {
                            fab.setColorFilter(Color.WHITE) // Past darajadagi ovoz uchun rang
                            0.2f
                        }

                        rmsdB in QUIT_RMSDB_MAX..MEDIUM_RMSDB_MAX -> {
                            fab.setColorFilter(ContextCompat.getColor(context, R.color.ll_word)) // O'rta darajadagi ovoz uchun rang
                            var height = 0.3f + Random.nextFloat()
                            if (height > 0.6f) height = 0.6f
                            height
                        }

                        else -> {
                            fab.setColorFilter(Color.RED) // Yuqori darajadagi ovoz uchun rang
                            var height = 0.7f + Random.nextFloat()
                            if (height > 1f) height = 1f
                            height
                        }
                    }
                    updateBackgroundEffect(newHeightPart)
                }
            }

            private fun updateBackgroundEffect(newHeightPart: Float) {
                val params = fabAnimation.layoutParams as ViewGroup.MarginLayoutParams
                val dm = fabAnimation.resources.displayMetrics

                params.width = convertDpToPx((56 + 8 * newHeightPart), dm)
                params.height = convertDpToPx((56 + 8 * newHeightPart), dm)

                fabAnimation.layoutParams = params
            }

            private fun convertDpToPx(dp: Float, dm: DisplayMetrics): Int =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm).toInt()

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(errorCode: Int) {
                val message = when (errorCode) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    // Add other cases based on SpeechRecognizer error codes
                    else -> "Unknown error"
                }
                Toast.makeText(context, "Error occurred: $message", Toast.LENGTH_SHORT).show()
            }

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
        val recognitionIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
//        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.startListening(recognitionIntent) // Speech recognizer boshlanadi
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }
}
