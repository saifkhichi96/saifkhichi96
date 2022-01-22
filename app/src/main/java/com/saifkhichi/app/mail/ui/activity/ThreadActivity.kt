package com.saifkhichi.app.mail.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.saifkhichi.app.databinding.ActivityThreadBinding
import com.saifkhichi.app.mail.data.repo.InboxRepository
import com.saifkhichi.app.mail.model.Message
import com.saifkhichi.app.mail.model.Thread
import com.saifkhichi.app.mail.ui.adapter.ThreadAdapter
import com.saifkhichi.app.mail.util.MailSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.simplejavamail.api.email.Email
import javax.inject.Inject

const val THREAD_KEY = "message"

@AndroidEntryPoint
class ThreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreadBinding

    private lateinit var threadAdapter: ThreadAdapter

    @Inject
    lateinit var repository: InboxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val messageList = intent.getSerializableExtra(THREAD_KEY) as ArrayList<Message>? ?: return finish()
        val thread = Thread.fromList(messageList) ?: return finish()

        threadAdapter = ThreadAdapter(thread)
        threadAdapter.setOnItemClickListener { acknowledgeReceipt(it) }
        supportActionBar?.title = thread.senderName

        binding.messagesList.adapter = threadAdapter
        binding.threadSubject.text = thread.subject
    }

    var progressDialog: ProgressDialog? = null

    /**
     * Acknowledge receipt of a message.
     *
     * Sends an auto-generated email to the original sender informing them that their message
     * has been received, and asking them to wait for a further response.
     *
     * @param message The message to acknowledge receipt of.
     */
    private fun acknowledgeReceipt(message: Message) {
        // Show progress bar
        progressDialog = ProgressDialog.show(this, "Acknowledge Receipt", "Sending an email...", true)

        // Email response
        val mailSender = MailSender()
        mailSender.onPostExecute = { result ->
            progressDialog?.dismiss()
            onAcknowledged(message.id, result.getOrNull(), result.exceptionOrNull())
        }
        mailSender.acknowledgeMessage(message)
    }

    /**
     * Called when the acknowledgement email has been sent.
     *
     * @param messageId The ID of the message that was acknowledged.
     * @param email The email that was sent, or null if an error occurred.
     * @param exception The exception that occurred, or null if no exception occurred.
     */
    private fun onAcknowledged(messageId: String, email: Email?, exception: Throwable?) {
        lifecycleScope.launch {
            repository.markAsRead(messageId)
            runOnUiThread(threadAdapter::notifyDataSetChanged)
        }

        progressDialog?.dismiss()
        Snackbar.make(
            binding.root, when (email) {
                null -> exception?.message ?: "Error emailing acknowledgement"
                else -> "Emailed an acknowledgement to ${email.recipients.joinToString(", ") { it.address }}"
            }, Snackbar.LENGTH_LONG
        ).show()
    }

}