package com.saifkhichi.app.payments.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saifkhichi.app.payments.data.repo.ClientsRepository
import com.saifkhichi.app.payments.model.Client
import com.stripe.model.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Stores and prepares the Inbox data for displaying.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@HiltViewModel
class ClientsViewModel @Inject constructor(var repository: ClientsRepository) : ViewModel() {

    private val _clients = MutableLiveData<MutableList<Client>>()
    val clients: LiveData<MutableList<Client>> = _clients

    private val _clientInvoices = MutableLiveData<Map<String, List<Invoice>>>()
    val clientInvoices: LiveData<Map<String, List<Invoice>>> = _clientInvoices

    init {
        repository.clients?.let { _clients.value = it.toMutableList() }
        repository.clientInvoices.let { _clientInvoices.value = it }
    }

    fun get(email: String): Client? {
        return clients.value?.find { it.email == email }
    }

    /**
     * Asynchronously fetches details of all clients and returns the
     * result to the associated live data.
     */
    fun getAll() {
        viewModelScope.launch {
            _clients.value = repository.getAll().toMutableList()
        }
    }

    fun getClientInvoices(clientEmail: String) {
        viewModelScope.launch {
            val map = _clientInvoices.value?.toMutableMap()
            map?.set(clientEmail, repository.getClientInvoices(clientEmail))
            _clientInvoices.value = map.orEmpty()
        }
    }

}