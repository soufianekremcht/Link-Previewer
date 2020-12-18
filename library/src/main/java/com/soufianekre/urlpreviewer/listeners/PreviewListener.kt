package com.soufianekre.urlpreviewer.listeners

import com.soufianekre.urlpreviewer.data.WebPreview

interface PreviewListener {
    fun onSuccess(metadata:WebPreview, status: Boolean)
    fun onError(e: Exception?)
}
