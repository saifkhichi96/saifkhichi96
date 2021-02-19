package com.saifkhichi.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.databinding.ViewMessageBinding
import com.saifkhichi.app.model.Message
import com.saifkhichi.app.model.Thread
import com.saifkhichi.app.ui.holder.MessageHolder
import java.text.SimpleDateFormat
import java.util.*


class ThreadAdapter(private val thread: Thread) : RecyclerView.Adapter<MessageHolder>() {

    private var onItemClicked: ((Message) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Message) -> Unit)) {
        onItemClicked = listener
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
        val view = ViewMessageBinding.inflate(LayoutInflater.from(parent.context),
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
        holder.messageContent.text = thread[position].message
        holder.messageSubject.text = thread[position].subject
        holder.messageTimestamp.text =
            SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(thread[position].timestamp)

        holder.message = thread[position]
        holder.onItemClicked = onItemClicked
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount() = thread.size

}