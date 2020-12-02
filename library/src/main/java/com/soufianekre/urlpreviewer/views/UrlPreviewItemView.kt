package com.soufianekre.urlpreviewer.views


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.soufianekre.urlpreviewer.R
import com.soufianekre.urlpreviewer.UrlPreviewer
import com.soufianekre.urlpreviewer.data.UrlMetaData
import com.soufianekre.urlpreviewer.listeners.ResponseListener
import com.soufianekre.urlpreviewer.listeners.UrlPreviewerListener
import com.soufianekre.urlpreviewer.listeners.PreviewListener
import com.squareup.picasso.Picasso


open class UrlPreviewItemView(context: Context, attrs: AttributeSet?) : RelativeLayout(context,attrs) {

    private var urlMetaData: UrlMetaData? = null

    private var urlPreviewerLayout: RelativeLayout? = null
    private var urlImgView: ImageView? = null
    private var urlTitle: TextView? = null
    private var urlDescText: TextView? = null
    private var urlText: TextView? = null

    private var mainUrl: String? = null

    private var isDefaultClick = true

    private var urlPreviewerListener: UrlPreviewerListener? = null



    override fun onFinishInflate() {
        super.onFinishInflate()
    }


    fun setupView() {
        View.inflate(context, R.layout.item_url_preview, this)
        urlPreviewerLayout = findViewById<View>(R.id.url_preview_layout) as RelativeLayout
        urlImgView = findViewById<View>(R.id.item_url_img) as ImageView
        urlTitle = findViewById<View>(R.id.item_url_title_text) as TextView
        urlText = findViewById<View>(R.id.item_url_text) as TextView
        urlDescText = findViewById<View>(R.id.item_url_content_desc) as TextView

        if (urlMetaData?.imageUrl!!.isEmpty()) {
            urlImgView?.visibility = View.GONE
        } else {
            urlImgView?.visibility = View.VISIBLE
            Picasso.get()
                .load(urlMetaData?.imageUrl)
                .into(urlImgView)
        }
        if (urlMetaData?.title!!.isEmpty()) {
            urlTitle?.visibility = View.GONE
        } else {
            urlTitle?.visibility = View.VISIBLE
            urlTitle?.text = urlMetaData?.title
        }
        if (urlMetaData?.url!!.isEmpty()) {
            urlText?.visibility = View.GONE
        } else {
            urlText?.visibility = View.VISIBLE
            urlText?.text = urlMetaData?.url
        }
        if (urlMetaData?.description!!.isEmpty()) {
            urlDescText!!.visibility = View.GONE
        } else {
            urlDescText!!.visibility = View.VISIBLE
            urlDescText?.text = urlMetaData?.description
        }
        urlPreviewerLayout!!.setOnClickListener { view ->
            if (isDefaultClick) {
                onUrlPreviewClick()
            } else {
                if (urlPreviewerListener != null)
                    urlPreviewerListener?.onClicked(view, urlMetaData)
                else
                    onUrlPreviewClick()
            }
        }
    }


    private fun onUrlPreviewClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mainUrl))
        context!!.startActivity(intent)
    }



    fun setUrl(url: String?, previewListener: PreviewListener) {
        mainUrl = url
        val urlPreview = UrlPreviewer(object : ResponseListener {
            override fun onData(metaData: UrlMetaData?) {
                urlMetaData = metaData
                if (urlMetaData?.title!!.isNotEmpty()){
                    metaData?.let {
                        previewListener.onSuccess(it,true) }
                }
                setupView()
            }

            override fun onError(e: Exception?) {
                previewListener.onError(e)
            }
        })
        urlPreview.getPreview(url)
    }

    private fun findLinearLayoutChild(): LinearLayout? {
        return if (childCount > 0 && getChildAt(0) is LinearLayout) {
            getChildAt(0) as LinearLayout
        } else null
    }



}