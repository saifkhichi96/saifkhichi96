package com.saifkhichi.app.auth.ui.viewmodel.livedata

/**
 * Data validation state of the login form.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
)