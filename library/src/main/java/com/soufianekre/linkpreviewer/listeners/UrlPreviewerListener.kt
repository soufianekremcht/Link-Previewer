package com.soufianekre.linkpreviewer.listeners

import android.view.View
import com.soufianekre.linkpreviewer.data.UrlPreviewItem




interface UrlPreviewerListener {
    fun onClicked(view: View?, meta: UrlPreviewItem?)
}