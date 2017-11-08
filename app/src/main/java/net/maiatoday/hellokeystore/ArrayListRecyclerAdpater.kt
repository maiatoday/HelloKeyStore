package net.maiatoday.hellokeystore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView

/**
 * Created by maia on 2017/11/08.
 */
class ArrayListRecyclerAdpater(var items: List<String>) : RecyclerView.Adapter<ArrayListRecyclerAdpater.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int)= holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: String) = with(view) {
            val text1 = findViewById<TextView>(android.R.id.text1)
            text1.text = s
        }

    }



}