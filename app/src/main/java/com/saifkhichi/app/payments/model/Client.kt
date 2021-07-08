package com.saifkhichi.app.payments.model

import com.stripe.model.Address
import com.stripe.model.CustomerCollection

/**
 * @author saifkhichi96
 * @since 1.0.0
 */
data class Client(
    var email: String = "",
    var name: String = "",
    var address: Address? = null,
    var currencies: CustomerCollection = CustomerCollection(),
)
