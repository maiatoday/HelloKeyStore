package net.maiatoday.hellokeystore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_supported_services_list.*
import kotlinx.android.synthetic.main.content_supported_services_list.*
import net.maiatoday.keystorehelper.listSupportedAlgorithms
import android.support.v7.widget.DividerItemDecoration

class SupportedServicesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supported_services_list)
        setSupportActionBar(toolbar)

        supportedList.layoutManager = LinearLayoutManager(this)

        supportedList.adapter = ArrayListRecyclerAdpater(listSupportedAlgorithms())
        supportedList.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
    }

}
