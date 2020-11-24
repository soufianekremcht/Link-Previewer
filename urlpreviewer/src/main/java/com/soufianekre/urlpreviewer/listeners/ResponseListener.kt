package com.soufianekre.urlpreviewer.listeners

import com.soufianekre.urlpreviewer.data.UrlMetaData


interface ResponseListener {
    fun onData(metaData: UrlMetaData?)

    fun onError(e: Exception?)
}