package com.saifkhichi.app.ui.activity

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.saifkhichi.app.R
import com.saifkhichi.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = resources.getStringArray(R.array.main_options);
        val optionsAdapter = OptionsAdapter(options)

        binding.optionsList.adapter = optionsAdapter
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

    private fun openInbox() {
        // TODO: Open inbox activity
    }

    class OptionsAdapter(private val dataSet: Array<String>) :
        RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)

            init {
                view.setOnClickListener {
                    // TODO: Handle clicks on main menu items
                }
            }
        }

        /**
         * Create new views, which defines the UI of the list item
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         *
         * @return A new ViewHolder that holds a View of the given view type.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )

            return ViewHolder(view)
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
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = dataSet[position]
        }

        /**
         * Return the size of the dataset
         */
        override fun getItemCount() = dataSet.size

    }

}