package com.soufianekre.linkpreviewersample.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.soufianekre.linkpreviewer.views.UrlPreviewCard
import com.soufianekre.linkpreviewersample.R


class UrlPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val url_text : TextView = itemView.findViewById(R.id.list_item_url_text)
    val urlPreviewView: UrlPreviewCard = itemView.findViewById<UrlPreviewCard>(R.id.item_url_preview)

    init {

    }



}