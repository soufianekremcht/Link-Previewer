package com.soufianekre.urlpreviewersample.ui

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.soufianekre.urlpreviewer.data.UrlMetaData
import com.soufianekre.urlpreviewer.listeners.PreviewListener
import kotlinx.android.synthetic.main.list_item_url_preview.view.*

class UrlPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    init {

    }
    public fun bind(url :String){
        itemView.item_url_preview.setUrl(url,object :PreviewListener{
            override fun onSuccess(metadata:UrlMetaData,status: Boolean) {
                Toast.makeText(itemView.context,
                    "The preview Is Working",Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(itemView.context,
                    "Preview Error" + e?.localizedMessage,Toast.LENGTH_LONG).show()
            }

        })
    }




}