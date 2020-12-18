package com.soufianekre.urlpreviewersample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.soufianekre.urlpreviewer.data.WebPreview
import com.soufianekre.urlpreviewer.listeners.PreviewListener
import com.soufianekre.urlpreviewersample.R

class UrlPreviewAdapter(var context : Context, var urlList :ArrayList<String>) :
    RecyclerView.Adapter<UrlPreviewViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlPreviewViewHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.list_item_url_preview,parent,false)
        return UrlPreviewViewHolder(root)
    }

    override fun getItemCount(): Int = urlList.size

    override fun onBindViewHolder(holder: UrlPreviewViewHolder, position: Int) {
        val url = urlList[position]
        holder.urlPreviewView.clear();
        holder.urlPreviewView.setUrl(url,object : PreviewListener {
            override fun onSuccess(metadata: WebPreview, status: Boolean) {
                /*Toast.makeText(context,
                    "The preview Is Working", Toast.LENGTH_LONG).show()

                 */
            }

            override fun onError(e: Exception?) {
                /*
                Toast.makeText(context,
                    "Preview Error" + e?.localizedMessage, Toast.LENGTH_LONG).show()
                 */
            }

        })
    }



}