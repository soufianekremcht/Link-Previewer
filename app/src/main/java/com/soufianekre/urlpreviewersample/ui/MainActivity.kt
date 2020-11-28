package com.soufianekre.urlpreviewersample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.soufianekre.urlpreviewersample.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var urlList: ArrayList<String> = ArrayList();
    lateinit var urlPreviewAdapter: UrlPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        var urlList: ArrayList<String> = ArrayList();
        urlList.add("https://stackoverflow.com/questions/49990933/configuration-on-demand-is-not-supported-by-the-current-version-of-the-android-g")
        urlList.add("https://www.youtube.com/watch?v=766qmHTc2ro&list=RD766qmHTc2ro&start_radio=1")
        urlList.add("https://www.youtube.com/watch?v=3SJ0Rd7XU4Y")
        links_recycler_view.setHasFixedSize(true)
        links_recycler_view.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        urlPreviewAdapter = UrlPreviewAdapter(this, urlList)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun setupUi() {
        add_url_btn.setOnClickListener {
            urlList.add(add_url_field.text.toString())
            urlPreviewAdapter.notifyItemInserted(urlPreviewAdapter.itemCount)
        }
    }
}