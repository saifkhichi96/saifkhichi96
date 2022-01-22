package com.saifkhichi.app.payments.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saifkhichi.app.databinding.ActivityClientsBinding
import com.saifkhichi.app.payments.model.Client
import com.saifkhichi.app.payments.ui.adapter.ClientAdapter
import com.saifkhichi.app.payments.ui.viewmodel.ClientsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientsActivity : AppCompatActivity() {

    private lateinit var clientAdapter: ClientAdapter
    private lateinit var clients: MutableList<Client>

    /**
     * View bindings for the activity.
     */
    private lateinit var binding: ActivityClientsBinding

    /**
     * View model to observe and manipulate the activity data.
     */
    private val viewModel: ClientsViewModel by viewModels()

    /**
     * Performs the refresh operation.
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getAll()
    }

    /**
     * Observes the result of the refresh operation.
     */
    private val refreshResultObserver = Observer<List<Client>> {
        binding.swipeRefresh.isRefreshing = false
        onRefreshed(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        clients = mutableListOf()
        clientAdapter = ClientAdapter(clients)
        clientAdapter.setOnItemClickListener { openClientDetails(it) }

        binding.clientsList.adapter = clientAdapter
        viewModel.clients.observe(this, refreshResultObserver)

        binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
        refreshManually()
    }

    private fun openClientDetails(client: Client) {
        val i = Intent(this, ClientDetailsActivity::class.java)
        i.putExtra(EXTRA_CLIENT, client.email)
        startActivity(i)
    }

    /**
     * Refreshes the activity content manually (i.e. not with the swipe-refresh gesture)
     */
    private fun refreshManually() {
        binding.swipeRefresh.isRefreshing = true
        onRefreshListener.onRefresh()
    }

    /**
     * Called when refresh operation finishes successfully
     *
     * @param data The updated data.
     */
    private fun onRefreshed(data: List<Client>) {
        clients.clear()
        clients.addAll(data.sortedBy { it.name })
        clientAdapter.notifyDataSetChanged()
    }

    /**
     * Called when the refresh operation fails.
     *
     * @param error An error message explaining the cause of failure.
     */
    private fun onRefreshFailed(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_CLIENT = "EXTRA_CLIENT"
    }

}