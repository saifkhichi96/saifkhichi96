package com.saifkhichi.app.payments.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.databinding.ViewListitemTwolineBinding
import com.saifkhichi.app.payments.ui.holder.TwoLineListItemHolder
import com.saifkhichi.app.payments.model.Client

class ClientAdapter(private val dataSet: List<Client>) :
    RecyclerView.Adapter<TwoLineListItemHolder<Client>>() {

    private var onItemClicked: ((Client) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Client) -> Unit)) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoLineListItemHolder<Client> {
        val view = ViewListitemTwolineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TwoLineListItemHolder(view)
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
    override fun onBindViewHolder(holder: TwoLineListItemHolder<Client>, position: Int) {
        val item = dataSet[position]
        holder.item = item
        holder.text1.text = item.name
        holder.text2.text = item.email
        holder.onItemClicked = onItemClicked
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount() = dataSet.size

}