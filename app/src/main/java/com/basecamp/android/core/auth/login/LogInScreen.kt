package com.basecamp.android.core.auth.login

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.closeFragment
import com.google.android.material.textfield.TextInputEditText
import kotlin.reflect.KClass

class LogInScreen : Screen<LogInPresenter>(), LogInContract.View, LogInContract.Router {

    private val signUpButton by lazy { findViewById<Button>(R.id.screen_login_signup_button) }
    private val emailField by lazy { findViewById<TextInputEditText>(R.id.screen_login_email_field) }
    private val passwordField by lazy { findViewById<TextInputEditText>(R.id.screen_login_password_field) }
    private val forgotPasswordButton by lazy { findViewById<TextView>(R.id.screen_login_forgot_password_text) }
    private val errorTextView by lazy { findViewById<TextView>(R.id.screen_login_error_text) }
    private val logInButton by lazy { findViewById<Button>(R.id.screen_login_login_button_button) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_login_progress_bar) }

    override fun getLayout(): Int = R.layout.screen_login

    override fun getPresenter(): KClass<LogInPresenter> = LogInPresenter::class

    override fun init() {
        forgotPasswordButton.setOnClickListener {
            navigate(LogInScreenDirections.actionLoginScreenToForgotpasswordScreen())
        }
        signUpButton.setOnClickListener { closeFragment() }
        logInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (checkFields()) {
                notify { onLogInClick(emailField.text.toString(), passwordField.text.toString()) }
            }
        }
    }

    override fun setError(error: String?) {
        progressBar.visibility = View.GONE
        errorTextView.text = error ?: context?.getString(R.string.something_went_wrong)
    }

    private fun checkFields(): Boolean {
        return if (emailField.checkIfEmpty() && passwordField.checkIfEmpty()) {
            true
        } else {
            setError(context?.getString(R.string.all_fields_required))
            false
        }
    }

    private fun TextInputEditText.checkIfEmpty(): Boolean {
        return text?.isEmpty() == false
    }

}