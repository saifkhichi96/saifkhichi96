package com.saifkhichi.app.mail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.saifkhichi.app.core.model.Result
import com.saifkhichi.app.databinding.ActivityInboxBinding
import com.saifkhichi.app.mail.model.Thread
import com.saifkhichi.app.mail.ui.adapter.InboxAdapter
import com.saifkhichi.app.mail.ui.viewmodel.InboxViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxActivity : AppCompatActivity() {

    private lateinit var inboxAdapter: InboxAdapter
    private lateinit var inbox: MutableList<Thread>

    /**
     * View bindings for the activity.
     */
    private lateinit var binding: ActivityInboxBinding

    /**
     * View model to observe and manipulate the activity data.
     */
    private val viewModel: InboxViewModel by viewModels()

    /**
     * Performs the refresh operation.
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getAllMessages()
    }

    /**
     * Observes the result of the refresh operation.
     */
    private val refreshResultObserver = Observer<Result<List<Thread>>> {
        binding.swipeRefresh.isRefreshing = false
        when (it) {
            is Result.Success -> onRefreshed(it.data)
            is Result.Error -> it.exception.message?.let { error -> onRefreshFailed(error) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.mainContent)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(binding.mainContent)
            duration = 250L
        }
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inbox = mutableListOf()
        inboxAdapter = InboxAdapter(inbox)
        inboxAdapter.setOnItemClickListener { thread, view -> openMessage(thread, view) }

        binding.messagesList.adapter = inboxAdapter
        viewModel.threads.observe(this, refreshResultObserver)

        binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
        refreshManually()
    }

    private fun openMessage(thread: Thread, view: View) {
        val i = Intent(this, ThreadActivity::class.java)
        i.putExtra(THREAD_KEY, thread)

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
    private fun onRefreshed(data: List<Thread>) {
        inbox.clear()
        inbox.addAll(data.reversed())
        inboxAdapter.notifyDataSetChanged()
    }

    /**
     * Called when the refresh operation fails.
     *
     * @param error An error message explaining the cause of failure.
     */
    private fun onRefreshFailed(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

}