package com.basecamp.android.core.auth.signup

import android.widget.Button
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.google.android.material.textfield.TextInputEditText
import kotlin.reflect.KClass

class SignUpScreen : Screen<SignUpPresenter>(), SignUpContract.View, SignUpContract.Router {

    private val logInButton by lazy { findViewById<Button>(R.id.screen_signup_login_button) }
    private val nameField by lazy { findViewById<TextInputEditText>(R.id.screen_signup_name_field) }
    private val emailField by lazy { findViewById<TextInputEditText>(R.id.screen_signup_email_field) }
    private val passwordField by lazy { findViewById<TextInputEditText>(R.id.screen_signup_password_field) }
    private val repeatPasswordField by lazy { findViewById<TextInputEditText>(R.id.screen_signup_password_repeat_field) }
    private val errorTextView by lazy { findViewById<TextView>(R.id.screen_signup_error_text) }
    private val signUpButton by lazy { findViewById<Button>(R.id.screen_signup_signup_button) }

    private val emailRegex = Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$")

    var onLogInClick: () -> Unit = {}

    override fun getLayout(): Int = R.layout.screen_signup

    override fun getPresenter(): KClass<SignUpPresenter> = SignUpPresenter::class

    override fun init() {
        logInButton.setOnClickListener {
            onLogInClick.invoke()
        }
        signUpButton.setOnClickListener {
            if (checkFields()) {
                notify { onSignUpClick(nameField.text.toString(), emailField.text.toString(), passwordField.text.toString()) }
            }
        }
    }

    private fun checkFields(): Boolean {
        return if (nameField.checkIfEmpty() && emailField.checkIfEmpty() && passwordField.checkIfEmpty() && repeatPasswordField.checkIfEmpty()) {
            if (!checkCorrectEmail()) false
            else checkCorrectPasssword()
        } else {
            setError(context?.getString(R.string.all_fields_required))
            false
        }
    }

    override fun setError(error: String?) {
        errorTextView.text = error ?: context?.getString(R.string.something_went_wrong)
    }

    private fun TextInputEditText.checkIfEmpty(): Boolean {
        return text?.isEmpty() == false
    }

    private fun checkCorrectEmail(): Boolean {
        return if (!emailField.text.toString().contains(emailRegex)) {
            setError(getString(R.string.incorrect_mail))
            false
        } else true
    }

    private fun checkCorrectPasssword(): Boolean {
        return when {
            passwordField.text.toString() != repeatPasswordField.text.toString() -> {
                setError(context?.getString(R.string.password_do_not_match))
                false
            }
            passwordField.text.toString().length < 6 -> {
                setError(context?.getString(R.string.password_need_six_characters))
                false
            }
            else -> true
        }
    }


}
