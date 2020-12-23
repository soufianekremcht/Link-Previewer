package com.soufianekre.urlpreviewer.listeners

import com.soufianekre.urlpreviewer.data.UrlPreviewItem


internal interface ResponseListener {

    fun onResponse(urlPreview: UrlPreviewItem?)
    fun onError(e: Exception?)
}