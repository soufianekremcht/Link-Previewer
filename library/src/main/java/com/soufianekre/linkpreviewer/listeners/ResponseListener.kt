package com.soufianekre.linkpreviewer.listeners

import com.soufianekre.linkpreviewer.data.UrlPreviewItem


internal interface ResponseListener {

    fun onResponse(urlPreview: UrlPreviewItem?)
    fun onError(e: Exception?)
}