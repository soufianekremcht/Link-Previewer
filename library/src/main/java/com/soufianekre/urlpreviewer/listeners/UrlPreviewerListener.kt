package com.soufianekre.urlpreviewer.listeners

import android.view.View
import com.soufianekre.urlpreviewer.data.UrlPreviewItem




interface UrlPreviewerListener {
    fun onClicked(view: View?, meta: UrlPreviewItem?)
}