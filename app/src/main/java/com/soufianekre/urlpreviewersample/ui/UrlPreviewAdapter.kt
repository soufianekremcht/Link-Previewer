package com.soufianekre.urlpreviewersample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soufianekre.urlpreviewersample.R

class UrlPreviewAdapter(var context : Context, var urlList :ArrayList<String>) : RecyclerView.Adapter<UrlPreviewViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlPreviewViewHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.list_item_url_preview,parent,false)
        return UrlPreviewViewHolder(root)
    }

    override fun getItemCount(): Int = urlList.size

    override fun onBindViewHolder(holder: UrlPreviewViewHolder, position: Int) {
        holder.bind(urlList[position]);
    }


}