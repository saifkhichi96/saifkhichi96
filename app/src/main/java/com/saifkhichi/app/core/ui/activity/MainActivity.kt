package com.saifkhichi.app.core.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.saifkhichi.app.R
import com.saifkhichi.app.auth.data.repo.LoginRepository
import com.saifkhichi.app.auth.ui.activity.LoginActivity
import com.saifkhichi.app.core.util.ColorUtils
import com.saifkhichi.app.databinding.ActivityMainBinding
import com.saifkhichi.app.mail.ui.activity.InboxActivity
import com.saifkhichi.app.payments.ui.activity.ClientsActivity
import com.saifkhichi.books.ui.activity.BookDetailsActivity.Companion.EXTRA_USER_ID
import com.saifkhichi.books.ui.activity.BooksListActivity
import com.saifkhichi.books.ui.activity.BooksListActivity.Companion.EXTRA_CATEGORY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var loginRepository: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.clientsButton.apply {
            setOnClickListener { openClientsPage() }
            ColorUtils.recolorAsContainer(this, this.text)
        }

        binding.artButton.apply {
            setOnClickListener { }
            ColorUtils.recolorAsContainer(this, this.text)
        }

        binding.workButton.apply {
            setOnClickListener { }
            ColorUtils.recolorAsContainer(this, this.text)
        }

        binding.fictionButton.apply {
            setOnClickListener { openLibrary("Fiction") }
            ColorUtils.recolorAsContainer(this, this.text)
        }

        binding.nonfictionButton.apply {
            setOnClickListener { openLibrary("Non-Fiction") }
            ColorUtils.recolorAsContainer(this, this.text)
        }

        val colors = ColorUtils.convertToColor(this, binding.projectsLabel.text)
        binding.projectsSection.setCardBackgroundColor(colors.accentContainer)
        binding.projectsEmptyText.setTextColor(colors.onAccentContainer)

        binding.projectsList.emptyView = binding.projectsEmpty
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
            R.id.action_sign_out -> {
                signOut()
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
    }

    /**
     * Opens the inbox activity.
     */
    private fun openInbox() {
        val i = Intent(this, InboxActivity::class.java)
        findViewById<View>(R.id.action_inbox)?.apply {
            transitionName = getString(R.string.transition_inbox)

            val transitionName = getString(R.string.transition_inbox)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, this, transitionName)
            startActivity(i, options.toBundle())
        }
    }

    /**
     * Opens the books activity.
     */
    private fun openLibrary(category: String) {
        val i = Intent(this, BooksListActivity::class.java)
        i.putExtra(EXTRA_CATEGORY, category)
        i.putExtra(EXTRA_USER_ID, loginRepository.currentUser?.id?:"")

        startActivity(i)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun signOut() {
        // Ask for confirmation
        Snackbar.make(
            binding.root,
            "Confirm sign out?",
            Snackbar.LENGTH_LONG
        ).setAction("Sign Out") {
            loginRepository.logout()
            finish()

            startActivity(Intent(this, LoginActivity::class.java))
        }.show()
    }

}