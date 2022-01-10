package com.saifkhichi.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.saifkhichi.app.R
import com.saifkhichi.app.databinding.ActivityMainBinding
import com.saifkhichi.app.payments.ui.activity.ClientsActivity
import com.saifkhichi.app.util.ColorUtils
import com.saifkhichi.books.ui.activity.BooksListActivity
import com.saifkhichi.books.ui.activity.BooksListActivity.Companion.EXTRA_CATEGORY

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
            setOnClickListener {  }
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

        startActivity(i)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}