package com.soufianekre.urlpreviewer.listeners

import com.soufianekre.urlpreviewer.data.UrlMetaData

interface PreviewListener {
    fun onSuccess(metadata:UrlMetaData,status: Boolean)
    fun onError(e: Exception?)
}
