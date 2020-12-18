package com.soufianekre.urlpreviewer.listeners

import com.soufianekre.urlpreviewer.data.WebPreview


internal interface ResponseListener {
    fun onData(metaData: WebPreview?)

    fun onError(e: Exception?)
}