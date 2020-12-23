package com.soufianekre.urlpreviewersample.ui

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soufianekre.urlpreviewer.data.UrlPreviewItem
import com.soufianekre.urlpreviewer.views.UrlPreviewCard
import com.soufianekre.urlpreviewersample.R


class UrlPreviewAdapter(var context: Context, var urlList: ArrayList<String>) :
    RecyclerView.Adapter<UrlPreviewViewHolder>(),UrlPreviewCard.OnPreviewLoad {

    var webPreviews: HashMap<String, UrlPreviewItem> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlPreviewViewHolder {
        val root =
            LayoutInflater.from(context).inflate(R.layout.list_item_url_preview, parent, false)
        return UrlPreviewViewHolder(root)
    }

    override fun getItemCount(): Int = urlList.size

    override fun onBindViewHolder(holder: UrlPreviewViewHolder, position: Int) {
        val url = urlList[position]


        holder.urlPreviewView.clear()

        holder.url_text.text = url

        var linkToLoad : String? = null;
        // TODO : Check The link if it is valid

        linkToLoad = url;

        if (linkToLoad != null){
            if (!tryImmediateWebPageLoad(holder,url)){
                holder.urlPreviewView.setUrl(url,this);
            }
        }

    }

    override fun onLinkLoaded(url: String, urlPreview: UrlPreviewItem) {
        webPreviews[url] = urlPreview
    }

    private fun tryImmediateWebPageLoad(holder: UrlPreviewViewHolder, url: String): Boolean {
        return if (webPreviews.containsKey(url)) {
            holder.urlPreviewView.tag = url
            holder.urlPreviewView.displayPreview(webPreviews[url]!!)
            true
        } else {
            false
        }
    }

}