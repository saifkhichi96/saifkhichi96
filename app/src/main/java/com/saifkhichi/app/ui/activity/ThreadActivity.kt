package com.saifkhichi.app.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saifkhichi.app.databinding.ActivityThreadBinding
import com.saifkhichi.app.model.Message
import com.saifkhichi.app.model.Thread
import com.saifkhichi.app.ui.adapter.ThreadAdapter
import java.text.SimpleDateFormat
import java.util.*

const val THREAD_KEY = "message"

class ThreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val messageList = intent.getSerializableExtra(THREAD_KEY) as ArrayList<Message>? ?: return finish()
        val thread = Thread.fromList(messageList) ?: return finish()
        val threadAdapter = ThreadAdapter(thread)
        threadAdapter.setOnItemClickListener { replyTo(it) }

        binding.messagesList.adapter = threadAdapter

    }

    private fun replyTo(message: Message) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(message.email))
            putExtra(Intent.EXTRA_SUBJECT, "Your message on Saif's Portfolio: ${message.subject}")
            putExtra(Intent.EXTRA_TEXT, "Hello ${message.name},\n" +
                    "\n" +
                    "Thank you for your message.\n" +
                    "\n\n" +
                    "Best Regards,\n" +
                    "Saif Khan\n" +
                    "\n\n\n" +
                    "---\n" +
                    "\n" +
                    "On ${
                        SimpleDateFormat("dd MMM, yyyy",
                            Locale.getDefault()).format(message.timestamp)
                    }, at ${
                        SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(message.timestamp)
                    }, ${message.name} <${message.email}> wrote:\n\n" +
                    message.message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}