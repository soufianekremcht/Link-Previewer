package com.soufianekre.urlpreviewer.listeners

interface ViewListener {
    fun onSuccess(status: Boolean)
    fun onError(e: Exception?)
}
