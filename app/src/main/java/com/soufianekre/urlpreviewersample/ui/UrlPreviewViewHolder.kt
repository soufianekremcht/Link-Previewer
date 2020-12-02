package com.soufianekre.urlpreviewersample.ui

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.soufianekre.urlpreviewer.views.UrlPreviewItemView
import com.soufianekre.urlpreviewersample.R


class UrlPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val urlPreviewView: UrlPreviewItemView = itemView.findViewById<UrlPreviewItemView>(R.id.item_url_preview)

    init {

    }



}