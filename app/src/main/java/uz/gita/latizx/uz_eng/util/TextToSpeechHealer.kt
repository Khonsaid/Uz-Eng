package uz.gita.latizx.uz_eng.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechHealer(
    context: Context,
    private val language: Locale = Locale.UK
) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(language)
            isReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun speak(text: String) {
        if (isReady) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
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