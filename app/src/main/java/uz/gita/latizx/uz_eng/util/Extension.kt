package uz.gita.latizx.uz_eng.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import uz.gita.latizx.uz_eng.R

fun Window.setColor(color: Int) {
    val windowInsetsController = WindowInsetsControllerCompat(this, this.decorView)
    windowInsetsController.isAppearanceLightStatusBars = false
    windowInsetsController.isAppearanceLightNavigationBars = false
    this.statusBarColor = ContextCompat.getColor(this.context, color)
    this.navigationBarColor = ContextCompat.getColor(this.context, color)
}

fun String.copyToClipBoard(context: Context) {
    val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", this)
    clipBoard.setPrimaryClip(clip)
}


//fun TextView.highlightSearch(fullText: String, searchQuery: String) {
//    if (searchQuery.isNotEmpty()) {
//        val startIndex = fullText.indexOf(searchQuery, ignoreCase = true)
//        if (startIndex >= 0) {
//            // Ranglashni boshlash
//            highlightText(fullText, startIndex, startIndex + searchQuery.length)
//        } else {
//            // Agar so'z topilmasa, faqat matnni ko'rsatish
//            this.text = fullText
//        }
//    }
//}

fun TextView.highlightText(text: String, start: Int, end: Int) {
    val spannableString = SpannableString(text)
    // Ranglash uchun berilgan oraliqni belgilash
    spannableString.setSpan(
        ForegroundColorSpan(Color.RED), // Rangni belgilash
        start,                          // Qidirilgan so‘zning boshlanish indeksi
        end,                            // Qidirilgan so‘zning tugash indeksi
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.setText(spannableString)
}

fun TextView.highlightSearch(fullText: String, query: String, highlightColor: Int = ContextCompat.getColor(this.context, R.color.primary)) {
    val spannableString = SpannableString(fullText)
    val lowerCaseText = fullText.lowercase()
    val lowerCaseQuery = query.lowercase()
    var startIndex = lowerCaseText.indexOf(lowerCaseQuery)

    while (startIndex >= 0) {
        val endIndex = startIndex + query.length
        spannableString.setSpan(
            ForegroundColorSpan(highlightColor),
            startIndex, endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = lowerCaseText.indexOf(lowerCaseQuery, startIndex + query.length)
    }
    this.text = spannableString
}
