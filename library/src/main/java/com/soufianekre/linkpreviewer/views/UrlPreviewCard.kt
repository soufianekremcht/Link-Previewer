package com.soufianekre.linkpreviewer.views


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.soufianekre.linkpreviewer.R
import com.soufianekre.linkpreviewer.UrlPreviewHelper
import com.soufianekre.linkpreviewer.data.UrlPreviewItem
import com.soufianekre.linkpreviewer.helpers.NetworkUtils
import com.soufianekre.linkpreviewer.listeners.ResponseListener
import com.soufianekre.linkpreviewer.listeners.UrlPreviewerListener
import com.squareup.picasso.Picasso


public class UrlPreviewCard(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    private var loadedPreview: UrlPreviewItem? = null
    private val urlPreviewLayout: CardView by lazy { findViewById<View>(R.id.url_preview_layout) as CardView }
    private val urlImgView: ImageView by lazy { findViewById<View>(R.id.item_url_img) as ImageView }
    private val urlTitle: TextView by lazy { findViewById<View>(R.id.item_url_title_text) as TextView }
    private val urlDescText: TextView by lazy { findViewById<View>(R.id.item_url_content_desc) as TextView }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.url_preview_progress_bar) }

    private var isDefaultClick = true
    private var urlPreviewerListener: UrlPreviewerListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_url_preview, this, true)
    }

    private fun onUrlPreviewClick(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context!!.startActivity(intent)
    }

    fun setUrl(url: String?, listener: OnPreviewLoad?) {
        // TODO : Check For internet Connectivity
        // Reduce Data Usage
        // Optimize for performance

        tag = url
        if (loadedPreview != null) {
            displayPreview(loadedPreview!!)
            return
        }
        progressBar.visibility = View.VISIBLE
        // get current url preview
        if (NetworkUtils.isInternetAvailable(context)){
            UrlPreviewHelper.getPreview(url, object : ResponseListener {
                override fun onResponse(urlPreview: UrlPreviewItem?) {
                    displayPreview(urlPreview!!)
                    listener?.onLinkLoaded(url!!, urlPreview)
                }
                override fun onError(e: Exception?) {
                    Log.e("UrlPreviewCard", e?.message)
                    progressBar.visibility = View.GONE
                }
            })
        }else{
            progressBar.visibility = View.GONE
        }

    }


    fun displayPreview(urlPreview: UrlPreviewItem) {
        if (tag != urlPreview.url)
            return

        loadedPreview = urlPreview

        if (urlPreview.imageUrl!!.isEmpty()) {
            urlImgView.visibility = View.GONE
        } else {
            urlImgView.visibility = View.VISIBLE
            try {
                Picasso.get()
                    .load(urlPreview.imageUrl)
                    .into(urlImgView)
            } catch (e: java.lang.Exception) {
                //
            }
        }
        urlPreviewLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        if (urlPreview.title!!.isEmpty()) {
            urlTitle.visibility = View.GONE
        } else {
            urlTitle.visibility = View.VISIBLE
            urlTitle.text = urlPreview.title
        }

        if (urlPreview.description!!.isEmpty()) {
            urlDescText.visibility = View.GONE
        } else {
            urlDescText.visibility = View.VISIBLE
            urlDescText.text = urlPreview.description
        }

        urlPreviewLayout.setOnClickListener { view ->
            if (urlPreviewerListener != null)
                urlPreviewerListener?.onClicked(view, urlPreview)
            else
                onUrlPreviewClick(urlPreview.url)
        }
    }


    fun clear() {
        urlImgView.visibility = View.GONE
        progressBar.visibility = View.GONE

        urlTitle.text = ""
        urlDescText.text = ""

        setOnClickListener { }
        setOnLongClickListener { true }
        loadedPreview = null
        tag = ""
    }


    public interface OnPreviewLoad {
        fun onLinkLoaded(url: String, urlPreview: UrlPreviewItem)
    }


}