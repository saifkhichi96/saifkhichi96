package com.saifkhichi.app.auth.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saifkhichi.app.R
import com.saifkhichi.app.auth.data.repo.LoginRepository
import com.saifkhichi.app.auth.model.User
import com.saifkhichi.app.auth.ui.viewmodel.livedata.LoginFormState
import com.saifkhichi.app.core.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Stores and manages the login data.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(var repository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    init {
        if (repository.isLoggedIn) {
            repository.currentUser?.let {
                _loginResult.value = Result.Success(it)
            }
        }
    }

    /**
     * Asynchronously executes the login task and returns the login
     * Result to associated live data.
     *
     * @param email The input email address.
     * @param password The input password.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = repository.login(email, password)
        }
    }

    /**
     * Called when the input data changes.
     *
     * Performs data validation.
     *
     * @param email The new email input.
     * @param password The new password input.
     */
    fun onDataChanged(email: String, password: String) {
        _loginForm.value = when {
            !isEmailValid(email) -> LoginFormState(emailError = R.string.invalid_email)
            !isPasswordValid(password) -> LoginFormState(passwordError = R.string.invalid_password)
            else -> LoginFormState(isDataValid = true)
        }
    }

    /**
     * Validates the email address.
     *
     * Checks that the email address is correctly formatted.
     *
     * @param email The email address to validate
     * @return True if a valid email, else false
     */
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    /**
     * Validates the password.
     *
     * Checks that the password is more than five characters long.
     *
     * @param password The password to validate.
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}