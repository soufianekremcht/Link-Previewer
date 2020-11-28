package com.soufianekre.urlpreviewer.listeners

import android.view.View
import com.soufianekre.urlpreviewer.data.UrlMetaData




interface UrlPreviewerListener {
    fun onClicked(view: View?, meta: UrlMetaData?)
}