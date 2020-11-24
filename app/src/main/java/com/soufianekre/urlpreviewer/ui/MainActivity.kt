package com.soufianekre.urlpreviewer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soufianekre.urlpreviewer.R
import com.soufianekre.urlpreviewer.listeners.ViewListener
import com.soufianekre.urlpreviewer.views.UrlPreviewItemView

class MainActivity :AppCompatActivity(){

    var urlPreview: UrlPreviewItemView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlPreview = findViewById<UrlPreviewItemView>(R.id.test_url_preview)
        showPreview()
    }


    private fun showPreview(){
        urlPreview?.setUrl("https://www.youtube.com/watch?v=5sKPaDmcJH0",object : ViewListener {
            override fun onSuccess(status: Boolean) {
                //TODO("Not yet implemented")
            }

            override fun onError(e: Exception?) {
                //TODO("Not yet implemented")
            }
        });
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}