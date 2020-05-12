package com.basecamp.android.core.auth.forgotpassword

import android.view.View
import android.widget.*
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.closeFragment
import com.google.android.material.textfield.TextInputEditText
import kotlin.reflect.KClass

class ForgotPasswordScreen : Screen<ForgotPasswordPresenter>(), ForgotPasswordContract.View, ForgotPasswordContract.Router {


    private val close by lazy { findViewById<ImageButton>(R.id.screen_forgopassword_goback_button) }
    private val emailField by lazy { findViewById<TextInputEditText>(R.id.screen_forgopassword_email_field) }
    private val errorTextView by lazy { findViewById<TextView>(R.id.screen_login_error_text) }
    private val sendEmailButton by lazy { findViewById<Button>(R.id.screen_forgopassword_button) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_forgopassword_progress_bar) }


    override fun getLayout(): Int = R.layout.screen_forgotpassword

    override fun getPresenter(): KClass<ForgotPasswordPresenter> = ForgotPasswordPresenter::class

    override fun init() {
        close.setOnClickListener { closeFragment() }
        sendEmailButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            emailField.text.toString().let {
                if (it.isEmpty()) {
                    progressBar.visibility = View.GONE
                    setError(context?.getString(R.string.incorrect_mail))
                } else {
                    notify {
                        onForgotPasswordClick(it)
                    }
                }
            }
        }
    }

    override fun setError(error: String?) {
        errorTextView.text = error ?: context?.getString(R.string.something_went_wrong)
    }

    override fun onEmailSent(email: String) {
        Toast.makeText(context, resources.getString(R.string.email_sent, email), Toast.LENGTH_LONG).show()
        closeFragment()
    }
}