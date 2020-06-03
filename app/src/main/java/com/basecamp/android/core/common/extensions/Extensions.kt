package com.basecamp.android.core.common.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun BCGlide(context: Context) = Glide.with(context.applicationContext)

/**
 * Invokes the Activity method with same name in order to allow back navigation.
 */
fun Fragment.closeFragment() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}

fun Context.hideKeyboard() {
    val view = (this as Activity).currentFocus
    if (view != null) {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.openKeyboard() {
    val view = (this as Activity).currentFocus
    if (view != null) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
            view.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
    }
}

fun Int.getGroupName(): String{
    return when (this){
        1-> "La Tríada de Chan Chu Yo"
        2-> "Famiglia Staffa"
        3-> "Famiglia Staffa"
        else-> "Cártel Armando Guerra"
    }
}