package com.saifkhichi.app.payments.data.source

import com.saifkhichi.app.payments.model.Client
import com.stripe.model.Customer
import com.stripe.model.CustomerCollection
import com.stripe.model.Invoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Class that handles, retrieves and manages data related to clients from the web service.
 *
 * @author saifkhichi96
 */
class ClientsDataSource @Inject constructor() {

    /**
     * Gets a list of all clients.
     */
    @Throws(Exception::class)
    suspend fun listAll(): MutableList<Client> = withContext(Dispatchers.IO) {
        val clients = mutableListOf<Client>()
        Customer.list(HashMap()).data.groupBy {
            it.email
        }.forEach { entry ->
            val email = entry.key
            if (!email.isNullOrBlank()) {
                val currencies = CustomerCollection().apply { data = entry.value }
                val name = currencies.data.find { !it.name.isNullOrBlank() }?.name ?: ""
                val address = currencies.data.find { it.address != null }?.address

                clients.add(
                    Client(
                    email = email,
                    name = name,
                    address = address,
                    currencies = currencies
                )
                )
            }
        }
        clients
    }

    @Throws(Exception::class)
    suspend fun listClientInvoices(client: Client): List<Invoice> = withContext(Dispatchers.IO) {
        val invoices = mutableListOf<Invoice>()
        for (c in client.currencies.data) {
            val params = HashMap<String, Any>()
            params["customer"] = c.id
            invoices.addAll(kotlin.runCatching {
                Invoice.list(params).data
            }.getOrNull().orEmpty())
        }
        invoices
    }

}