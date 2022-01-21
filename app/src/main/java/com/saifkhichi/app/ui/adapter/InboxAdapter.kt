package com.saifkhichi.app.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.R
import com.saifkhichi.app.databinding.ViewThreadBinding
import com.saifkhichi.app.model.Thread
import com.saifkhichi.app.ui.holder.ThreadHolder
import java.text.DateFormat
import java.util.*

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
        val view = ViewThreadBinding.inflate(
            LayoutInflater.from(parent.context),
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
        val thread = threads[position]
        holder.senderName.text = thread.senderName.split('@')[0]
        holder.senderIcon.text = thread.senderEmail.firstOrNull().toString().uppercase()
        holder.senderIcon.setBackgroundColor(convertToColor(holder.itemView.context, thread.senderEmail))
        if (thread.size > 1) {
            holder.messageCount.text = thread.size.toString()
        }

        thread.lastOrNull()?.let { lastMessage ->
            holder.messageSubject.text = lastMessage.subject
            holder.messageContents.text = lastMessage.body.replace('\n', ' ').replace("\\s+".toRegex(), " ")
            holder.messageTimestamp.text = DateFormat.getDateInstance(
                DateFormat.SHORT,
                Locale.getDefault()
            ).format(lastMessage.timestamp)
        }

        holder.thread = thread
        holder.onItemClicked = onItemClicked
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount() = threads.size

    companion object {
        @ColorInt
        fun convertToColor(context: Context, o: Any): Int {
            return try {
                val i = o.hashCode()
                Color.parseColor(
                    "#FF" + Integer.toHexString(i shr 16 and 0xFF) +
                            Integer.toHexString(i shr 8 and 0xFF) +
                            Integer.toHexString(i and 0xFF)
                )
            } catch (ignored: Exception) {
                context.getColor(R.color.colorPrimary)
            }
        }
    }

}