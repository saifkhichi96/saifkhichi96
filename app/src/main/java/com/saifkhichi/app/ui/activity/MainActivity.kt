package com.saifkhichi.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.saifkhichi.app.R
import com.saifkhichi.app.databinding.ActivityMainBinding
import com.saifkhichi.app.payments.ui.activity.ClientsActivity
import com.saifkhichi.books.ui.activity.BooksListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.clientsButton.setOnClickListener { openClientsPage() }
        binding.libraryButton.setOnClickListener { openLibrary() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_inbox -> {
                openInbox()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Opens the clients activity.
     */
    private fun openClientsPage() {
        startActivity(Intent(this, ClientsActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * Opens the inbox activity.
     */
    private fun openInbox() {
        startActivity(Intent(this, InboxActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * Opens the books activity.
     */
    private fun openLibrary() {
        startActivity(Intent(this, BooksListActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}