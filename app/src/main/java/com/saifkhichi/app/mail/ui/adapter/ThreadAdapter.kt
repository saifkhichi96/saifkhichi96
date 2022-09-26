package com.saifkhichi.app.mail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.core.util.ColorUtils
import com.saifkhichi.app.databinding.ViewMessageBinding
import com.saifkhichi.app.mail.model.Message
import com.saifkhichi.app.mail.model.Thread
import com.saifkhichi.app.mail.ui.holder.MessageHolder
import java.text.DateFormat
import java.util.*


class ThreadAdapter(private val thread: Thread) : RecyclerView.Adapter<MessageHolder>() {

    private var onItemClicked: ((Message) -> Unit)? = null

    private var onItemDeleteClicked: ((Message) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Message) -> Unit)) {
        onItemClicked = listener
    }

    fun setOnItemDeleteClickListener(listener: ((Message) -> Unit)) {
        onItemDeleteClicked = listener
    }

    /**
     * Create new views, which defines the UI of the list item
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = ViewMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageHolder(view)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * Get element from the dataset at this [position] and replace the
     * contents of the view with that element
     *
     * @param holder The view to update with new content
     * @param position The position of new content in the dataset
     */
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = thread[position]
        holder.senderName.text = message.from
        holder.senderIcon.text = message.from.firstOrNull().toString().uppercase()
        ColorUtils.recolor(holder.senderIcon, thread.senderEmail)

        holder.messageContents.text = message.body
        holder.messageRecipient.text = "to me"
        holder.messageTimestamp.text = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT,
            Locale.getDefault()
        ).format(message.timestamp)

        holder.message = message
        holder.replyButton.isEnabled = !message.read
        holder.replyButton.text = if (message.read) "Read" else "Mark as Read"
        holder.onItemClicked = onItemClicked
        holder.onItemDeleteClicked = onItemDeleteClicked
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount() = thread.size

}