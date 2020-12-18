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
        urlList.add("https://stackoverflow.com/questions/49990933/configuration-on-demand-is-not-supported-by-the-current-version-of-the-android-g")
        urlList.add("https://www.youtube.com/watch?v=766qmHTc2ro&list=RD766qmHTc2ro&start_radio=1")
        urlList.add("https://www.youtube.com/watch?v=3SJ0Rd7XU4Y")
        urlList.add("https://medium.com/mindorks/creating-different-build-variants-in-android-8291b95352f5")
        urlList.add("https://medium.com/mindorks/tagged/android")
        urlList.add("https://medium.com/mindorks/top-kotlin-blogs-to-learn-more-in-kotlin-82a6387e4dd6")
        urlList.add("https://pomodoro-tracker.com/#")
        urlList.add("https://developer.android.com/studio")
        urlList.add("https://github.com/SoufianeKreX/UrlPreviewer")
        urlList.add("https://www.youtube.com/watch?v=ni2uij7PynU&list=RDni2uij7PynU&start_radio=1")

        links_recycler_view.setHasFixedSize(true)
        links_recycler_view.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        urlPreviewAdapter = UrlPreviewAdapter(this, urlList)
        links_recycler_view.adapter = urlPreviewAdapter

        refresh_layout.setOnRefreshListener {
            links_recycler_view.adapter = urlPreviewAdapter
            refresh_layout.isRefreshing = false

        }
        setupUi()

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