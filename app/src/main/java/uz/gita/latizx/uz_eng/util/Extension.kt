package uz.gita.latizx.uz_eng.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

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