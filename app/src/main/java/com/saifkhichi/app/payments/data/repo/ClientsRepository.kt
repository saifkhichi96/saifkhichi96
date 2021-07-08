package com.saifkhichi.app.payments.data.repo

import com.orhanobut.hawk.Hawk
import com.saifkhichi.app.payments.data.source.ClientsDataSource
import com.saifkhichi.app.payments.model.Client
import com.stripe.model.Invoice
import javax.inject.Inject

/**
 * Requests client-related data from the remote data source and maintains an in-memory
 * cache of the clients' details.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class ClientsRepository @Inject constructor(var dataSource: ClientsDataSource) {

    /**
     * In-memory cache of messages
     */
    var clients: List<Client>? = null
        private set

    /**
     * In-memory cache of messages
     */
    val clientInvoices: HashMap<String, List<Invoice>> = HashMap()

    init {
        kotlin.runCatching { clients = Hawk.get(CLIENTS_KEY) }
        clients?.forEach {
            val invoices = Hawk.get<List<Invoice>>("${INVOICES_KEY}_${it.email}").orEmpty()
            clientInvoices[it.email] = invoices
        }
    }

    suspend fun getAll(): List<Client> {
        return kotlin.runCatching {
            val clients = dataSource.listAll()
            setClientsData(clients)
            clients
        }.getOrNull().orEmpty()
    }

    suspend fun getClientInvoices(clientEmail: String): List<Invoice> {
        return kotlin.runCatching {
            val client = clients?.find { it.email.equals(clientEmail, true) }!!
            val invoices = dataSource.listClientInvoices(client)
            setClientInvoices(client, invoices)
            invoices
        }.getOrNull().orEmpty()
    }

    private fun setClientsData(clients: List<Client>) {
        this.clients = clients
        Hawk.put(CLIENTS_KEY, clients)
    }

    private fun setClientInvoices(client: Client, invoices: List<Invoice>) {
        this.clientInvoices[client.email] = invoices
        Hawk.put("${INVOICES_KEY}_${client.email}", invoices)
    }

    companion object {
        private const val CLIENTS_KEY = "clients"
        private const val INVOICES_KEY = "invoices"
    }

}