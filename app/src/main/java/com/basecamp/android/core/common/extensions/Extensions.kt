package com.basecamp.android.core.common.extensions

import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.basecamp.android.R
import com.bumptech.glide.Glide
import java.util.*

fun BCGlide(context: Context) = Glide.with(context.applicationContext)

/**
 * Invokes the Activity method with same name in order to allow back navigation.
 */
fun Fragment.closeFragment() {
    Navigation.findNavController(this.requireView()).navigateUp()
//    requireActivity().onBackPressedDispatcher.onBackPressed()
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

fun Int.getGroupName(): String {
    return when (this) {
        1 -> "La Tríada de Chan Chu Yo"
        2 -> "Famiglia Staffa"
        3 -> "Klan Calaix Nikov"
        else -> "Cártel Armando Guerra"
    }
}

fun Int.getLeaderName(): String {
    return when (this) {
        1 -> "Chan Chu Yo"
        2 -> "Stefano Staffa"
        3 -> "Calaix Nikov"
        else -> "Armando Guerra"
    }
}

fun Int.getGroupLogo(): Int {
    return when (this) {
        1 -> R.drawable.ic_triada_chachuyo
        2 -> R.drawable.ic_famiglia_staffa
        3 -> R.drawable.ic_klan_calaixnicov
        else -> R.drawable.ic_cartel_armando_guerra
    }
}


fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun convertTimestampToDate(timestamp: Long): String =
    DateFormat.format("EEEE, dd MMMM yyyy", Calendar.getInstance().apply {
        timeInMillis = timestamp
    }).toString()


