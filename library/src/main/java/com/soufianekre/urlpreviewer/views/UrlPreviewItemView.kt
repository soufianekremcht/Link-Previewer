package com.soufianekre.urlpreviewer.views


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.soufianekre.urlpreviewer.R
import com.soufianekre.urlpreviewer.UrlPreviewer
import com.soufianekre.urlpreviewer.data.WebPreview
import com.soufianekre.urlpreviewer.listeners.ResponseListener
import com.soufianekre.urlpreviewer.listeners.UrlPreviewerListener
import com.soufianekre.urlpreviewer.listeners.PreviewListener
import com.squareup.picasso.Picasso


public class UrlPreviewItemView(context: Context, attrs: AttributeSet?) : CardView(context,attrs) {


    private var loadedPreview : WebPreview? = null
    private val urlPreviewLayout: CardView by lazy { findViewById<View>(R.id.url_preview_layout) as CardView }
    private val urlImgView: ImageView by lazy{ findViewById<View>(R.id.item_url_img) as ImageView}
    private val urlTitle: TextView by lazy{findViewById<View>(R.id.item_url_title_text) as TextView}
    private val urlDescText: TextView by lazy{findViewById<View>(R.id.item_url_content_desc) as TextView}
    private val urlText: TextView by lazy{findViewById<View>(R.id.item_url_text) as TextView}
    private val progressBar : ProgressBar by lazy{findViewById(R.id.url_preview_progress_bar) as ProgressBar}

    private var tag: String? = null
    private var isDefaultClick = true
    private var urlPreviewerListener: UrlPreviewerListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_url_preview, this, true)
    }


    fun setupView(webPreview: WebPreview) {
        if (tag != webPreview.url) {
                return
        }
        loadedPreview = webPreview
        progressBar.visibility = View.GONE
        //urlText!!.text = webPreview.url
        if (webPreview.title!!.isEmpty()) {
            urlTitle.visibility = View.GONE
        } else {
            urlTitle.visibility = View.VISIBLE
            urlTitle.text = webPreview.title
        }



        if (webPreview.imageUrl!!.isEmpty()) {
            urlImgView.visibility = View.GONE
        } else {
            urlImgView.visibility = View.VISIBLE
            Picasso.get()
                .load(webPreview.imageUrl)
                .into(urlImgView)
        }

        if (webPreview.description!!.isEmpty()) {
            urlDescText.visibility = View.GONE
        } else {
            urlDescText.visibility = View.VISIBLE
            urlDescText.text = webPreview.description
        }

        urlPreviewLayout.setOnClickListener { view ->
            if (urlPreviewerListener != null)
                urlPreviewerListener?.onClicked(view, webPreview)
            else
                onUrlPreviewClick()
        }
    }


    private fun onUrlPreviewClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tag))
        context!!.startActivity(intent)
    }

    fun setUrl(url: String?, previewListener: PreviewListener) {
        tag = url
        urlText.text = url;
        if (loadedPreview != null){
            setupView(loadedPreview!!)
            return
        }

        val urlPreviewer = UrlPreviewer(object : ResponseListener {
            override fun onData(metaData: WebPreview?) {
                if (metaData?.title!!.isNotEmpty()){
                    metaData.let {
                        previewListener.onSuccess(it,true) }
                }
                setupView(metaData)
            }

            override fun onError(e: Exception?) {
                previewListener.onError(e)
                progressBar.visibility = View.GONE
            }
        })
        urlPreviewer.getPreview(url)
    }

    private fun findLinearLayoutChild(): LinearLayout? {
        return if (childCount > 0 && getChildAt(0) is LinearLayout) {
            getChildAt(0) as LinearLayout
        } else null
    }

    fun clear() {

        urlImgView.visibility = View.GONE
        //progressBar.visibility = View.GONE

        urlTitle.text = ""
        urlDescText.text = ""


        setOnClickListener { }
        setOnLongClickListener { true }
        loadedPreview = null
        tag = ""
    }



}