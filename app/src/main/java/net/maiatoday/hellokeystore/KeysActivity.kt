package net.maiatoday.hellokeystore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_keys.*
import kotlinx.android.synthetic.main.item_key.view.*
import net.maiatoday.keystorehelper.deleteKeyPair
import net.maiatoday.keystorehelper.listAliases

class KeysActivity : AppCompatActivity() {
    lateinit var prefs: Prefs
    lateinit var activeAlias: String

    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keys)
        prefs = Prefs(this)
        activeAlias = prefs.keyAlias
        recyclerView.layoutManager = LinearLayoutManager(this)
        val items:List<String> = listAliases()?.toList() ?: arrayListOf<String>()
        adapter = MyAdapter(items, prefs.keyAlias) {
            if (it.equals(prefs.keyAlias)) {
                //deleting the current alias should also delete the secrets
                toast("$it is the active alias, deleting it will delete the secret message")
                prefs.clear()
            } else {
                toast("Deleting $it")
            }
            deleteKeyPair(it)
            adapter.refreshData(listAliases()?.toList() ?: arrayListOf<String>(), prefs.keyAlias)

        }
        recyclerView.adapter = adapter


    }


    class MyAdapter(var items: List<String>, var activeAlias: String, val listener: (String) -> Unit) : RecyclerView.Adapter<KeysActivity.MyAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_key, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], activeAlias, listener)

        override fun getItemCount() = items.size


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: String, activeAlias: String, listener: (String) -> Unit) = with(itemView) {
                alias.text = item
                if (item.equals(activeAlias)) {
                    current.visibility = View.VISIBLE
                } else {
                    current.visibility = View.GONE
                }
               buttonDelete.setOnClickListener { listener(item) }
            }
        }

        fun refreshData(newItems: List<String>, newAlias:String) {
            items = newItems
            activeAlias = newAlias
            notifyDataSetChanged()

        }
    }

}
