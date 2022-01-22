package com.saifkhichi.app.auth.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.saifkhichi.app.R
import com.saifkhichi.app.auth.model.User
import com.saifkhichi.app.auth.ui.viewmodel.LoginViewModel
import com.saifkhichi.app.auth.ui.viewmodel.livedata.LoginFormState
import com.saifkhichi.app.core.model.Result
import com.saifkhichi.app.core.ui.activity.MainActivity
import com.saifkhichi.app.core.util.afterTextChanged
import com.saifkhichi.app.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Login screen of the app allows a user to sign into their account.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     * View bindings for the activity.
     */
    private lateinit var binding: ActivityLoginBinding

    /**
     * View model to observe and manipulate the activity data.
     */
    private val viewModel: LoginViewModel by viewModels()

    /**
     * Observes the state of the input form.
     */
    private val loginFormStateObserver = Observer<LoginFormState> {
        val loginState = it ?: return@Observer

        // Disable login button unless both username / password is valid
        binding.signInButton.isEnabled = loginState.isDataValid

        // Check email is valid
        if (loginState.emailError != null) {
            binding.emailField.error = getString(loginState.emailError)
        }

        // Check password is valid
        if (loginState.passwordError != null) {
            binding.passwordField.error = getString(loginState.passwordError)
        }
    }

    /**
     * Observes the result of the login task.
     */
    private val loginResultObserver = Observer<Result<User>> {
        val loginResult = it ?: return@Observer
        binding.loading.visibility = View.GONE
        binding.signInButton.visibility = View.VISIBLE

        when (loginResult) {
            is Result.Success -> onLoginSuccess(loginResult.data)
            is Result.Error -> onLoginFailed(loginResult.exception)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loginFormState.observe(this@LoginActivity, loginFormStateObserver)
        viewModel.loginResult.observe(this@LoginActivity, loginResultObserver)

        binding.emailField.afterTextChanged {
            viewModel.onDataChanged(
                binding.emailField.text.toString(),
                binding.passwordField.text.toString()
            )
        }

        binding.passwordField.apply {
            afterTextChanged {
                viewModel.onDataChanged(
                    binding.emailField.text.toString(),
                    binding.passwordField.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> login()
                }
                false
            }
        }

        binding.signInButton.setOnClickListener { login() }
    }

    /**
     * Starts the login task in a coroutine.
     */
    private fun login() {
        binding.loading.visibility = View.VISIBLE
        binding.signInButton.visibility = View.GONE
        viewModel.login(
            binding.emailField.text.toString(),
            binding.passwordField.text.toString()
        )
    }

    /**
     * Called when the login succeeds.
     *
     * Proceeds to the main app activity with the logged-in user.
     *
     * @param user The currently logged-in user.
     */
    private fun onLoginSuccess(user: User) {
        val i = Intent(this, MainActivity::class.java).apply {
            putExtra("user", user)
        }

        startActivity(i)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    /**
     * Called when the login fails.
     *
     * Displays an appropriate error message.
     *
     * @param error The Exception which caused the failure.
     */
    private fun onLoginFailed(error: Exception) {
        Toast.makeText(
            applicationContext,
            error.message ?: getString(R.string.login_failed),
            Toast.LENGTH_LONG
        ).show()
    }

}