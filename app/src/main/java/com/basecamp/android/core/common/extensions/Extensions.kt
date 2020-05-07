package com.basecamp.android.core.common.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun BCGlide(context: Context) = Glide.with(context.applicationContext)

/**
 * Invokes the Activity method with same name in order to allow back navigation.
 */
fun Fragment.closeFragment() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}
