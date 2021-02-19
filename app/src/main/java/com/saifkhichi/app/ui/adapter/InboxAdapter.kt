package com.saifkhichi.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.model.Thread
import com.saifkhichi.app.ui.holder.ThreadHolder

class InboxAdapter(private val threads: List<Thread>) :
    RecyclerView.Adapter<ThreadHolder>() {

    private var onItemClicked: ((Thread) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Thread) -> Unit)) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_2,
            parent,
            false
        )
        return ThreadHolder(view)
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
    override fun onBindViewHolder(holder: ThreadHolder, position: Int) {
        holder.senderName.text = threads[position].senderEmail
        holder.messageTimestamp.text = "${threads[position].size} messages"

        holder.thread = threads[position]
        holder.onItemClicked = onItemClicked
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount() = threads.size

}