package uz.gita.latizx.uz_eng.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Window
import android.widget.TextView
import android.widget.Toast
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

fun Context.shareText(text: String?, title: String = "Share") {
    if (!text.isNullOrEmpty()) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(shareIntent, title))
    } else Toast.makeText(this, "Text is empty!", Toast.LENGTH_SHORT).show()
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

fun Context.getClipboardText(): String {
    val clipboard = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.primaryClip?.getItemAt(0)?.text.toString()
}