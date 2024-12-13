package uz.gita.latizx.uz_eng.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechHealer @Inject constructor(@ApplicationContext context: Context) :
    TextToSpeech.OnInitListener {
    var tts: TextToSpeech? = null
    private var isReady = false
    private val language: Locale = Locale.UK

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(language)
            isReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun speak(text: String, onStop: (() -> Unit)? = null) {
        if (isReady) {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    onStop?.invoke()
                }

                override fun onError(utteranceId: String?) {
                    onStop?.invoke()
                }
            })

            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun setSpeechRate(rate: Float = 1f) {
        tts?.setSpeechRate(rate) // Default rate is 1.0, slower rates are < 1.0
    }

    fun setPitch(pitch: Float = 1f) {
        tts?.setPitch(pitch) // Default pitch is 1.0
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.shutdown()
    }
}