package com.saifkhichi.app.payments.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.chip.Chip
import com.saifkhichi.app.R
import com.saifkhichi.app.databinding.ActivityClientDetailsBinding
import com.saifkhichi.app.payments.ui.viewmodel.ClientsViewModel
import com.saifkhichi.app.payments.model.Client
import com.saifkhichi.app.payments.ui.adapter.InvoiceAdapter
import com.stripe.model.Invoice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientDetailsActivity : AppCompatActivity() {

    /**
     * View bindings for the activity.
     */
    private lateinit var binding: ActivityClientDetailsBinding

    /**
     * View model to observe and manipulate the activity data.
     */
    private val viewModel: ClientsViewModel by viewModels()

    private val invoices: MutableList<Invoice> = mutableListOf()
    private val invoiceAdapter: InvoiceAdapter = InvoiceAdapter(invoices)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val client = intent.getStringExtra(ClientsActivity.EXTRA_CLIENT)?.let {
            viewModel.get(it)
        } ?: return finish()

        updateUI(client)
    }

    private fun updateUI(client: Client) {
        supportActionBar?.title = client.name
        binding.clientEmail.text = client.email
        binding.clientCurrencies.apply {
            client.currencies.data.forEach {
                val item = Chip(ContextThemeWrapper(context, R.style.App_Widget_CurrencyItem))
                item.chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.colorPrimary))
                item.setTextColor(R.color.colorAccent)
                item.text = it.currency
                addView(item)
            }
        }
        binding.invoiceList.adapter = invoiceAdapter

        viewModel.getClientInvoices(client.email)
        viewModel.clientInvoices.observe(this, {
            val data = it[client.email].orEmpty()
            invoices.clear()
            invoices.addAll(data.reversed())
            invoiceAdapter.notifyDataSetChanged()
        })
        invoiceAdapter.setOnItemClickListener { }
    }

}