package com.soufianekre.urlpreviewer

import android.os.AsyncTask
import android.util.Log
import android.webkit.URLUtil
import com.soufianekre.urlpreviewer.data.UrlMetaData
import com.soufianekre.urlpreviewer.listeners.ResponseListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException


public class UrlPreviewer(var responseListener: ResponseListener?) {

    var metaData: UrlMetaData? = UrlMetaData()

    var url: String? = null

    fun getPreview(url: String?) {
        this.url = url
        UrlDataAsync(this).execute()
    }
    private fun resolveURL(url: String?, part: String): String? {
        return if (URLUtil.isValidUrl(part)) {
            part
        } else {
            var baseUri: URI? = null
            try {
                baseUri = URI(url)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            baseUri = baseUri?.resolve(part)
            baseUri.toString()
        }
    }

    companion object {
        class UrlDataAsync(private val urlPreviewer: UrlPreviewer) :
            AsyncTask<Void?, Void?, Void?>() {

            override fun onPreExecute() {
                super.onPreExecute()

            }

            override fun doInBackground(vararg params: Void?): Void? {
                var doc: Document? = null
                try {
                    doc = Jsoup.connect(urlPreviewer.url)
                        .timeout(30 * 1000)
                        .get()
                    val elements: Elements = doc.getElementsByTag("meta")
                    // Url title
                    var title: String = doc.select("meta[property=og:title]").attr("content")
                    if (title.isEmpty()) {
                        title = doc.title()
                    }
                    urlPreviewer.metaData?.title =title

                    // url Content Description
                    var description: String = doc
                        .select("meta[name=description]")
                        .attr("content")

                    if (description.isEmpty()) {
                        description = doc
                            .select("meta[name=Description]")
                            .attr("content")
                    }

                    if (description.isEmpty()) {
                        description = doc
                            .select("meta[property=og:description]")
                            .attr("content")
                    }

                    urlPreviewer.metaData?.description=description

                    // Url MediaType
                    val mediaTypes: Elements = doc.select("meta[name=medium]")
                    var type = ""
                    type = if (mediaTypes.size > 0) {
                        val media: String = mediaTypes.attr("content")
                        if (media == "image") "photo" else media
                    } else {
                        doc.select("meta[property=og:type]").attr("content")
                    }
                    urlPreviewer.metaData?.mediaType = type


                    // url Image
                    val imageElements: Elements = doc.select("meta[property=og:image]")

                    if (imageElements.size > 0) {
                        val image: String = imageElements.attr("content")
                        if (image.isNotEmpty()) {
                            urlPreviewer.metaData?.imageUrl =
                                urlPreviewer.resolveURL(urlPreviewer.url, image)
                        }
                    }

                    if (urlPreviewer.metaData?.imageUrl!!.isEmpty()) {
                        val cssQueries : ArrayList<String> = ArrayList()
                        cssQueries.add("link[rel=image_src]")
                        cssQueries.add("link[rel=apple-touch-icon]")
                        cssQueries.add("link[rel=icon]")
                        for (i in 0..cssQueries.size){
                            val src = doc.select(cssQueries[i]).attr("href")
                            if (src.isNotEmpty()){
                                urlPreviewer.metaData?.imageUrl = urlPreviewer.resolveURL(urlPreviewer.url, src)
                                urlPreviewer.metaData?.favicon = urlPreviewer.resolveURL(urlPreviewer.url, src)
                                break
                            }
                        }
                    }

                    // Url FavIcon
                    var src: String = doc.select("link[rel=apple-touch-icon]").attr("href")
                    if (src.isNotEmpty()) {
                        urlPreviewer.metaData?.favicon = (urlPreviewer.resolveURL(urlPreviewer.url, src))
                    } else {
                        src = doc.select("link[rel=icon]").attr("href")
                        if (src.isNotEmpty()) {
                            urlPreviewer.metaData?.favicon = (urlPreviewer.resolveURL(urlPreviewer.url, src))
                        }
                    }
                    for (element in elements) {
                        if (element.hasAttr("property")) {
                            val strProperty: String = element.attr("property").toString().trim()
                            if (strProperty == "og:url") {
                                urlPreviewer.metaData?.url = element.attr("content").toString()
                            }
                            if (strProperty == "og:site_name") {
                                urlPreviewer.metaData?.siteName= (element.attr("content").toString())
                            }
                        }
                    }
                    if (urlPreviewer.metaData?.url!!.isEmpty()) {
                        var uri: URI? = null
                        try {
                            uri = URI(urlPreviewer.url)
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                        }
                        urlPreviewer.metaData?.url = uri!!.host
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    urlPreviewer.responseListener?.onError(Exception("No Html Received from " +
                            urlPreviewer.url + " Check your Internet " + e.localizedMessage))
                }
                return null
            }

            override fun onProgressUpdate(vararg values: Void?) {
                super.onProgressUpdate(*values)
            }

            override fun onCancelled() {
                super.onCancelled()
                urlPreviewer.responseListener?.onError(Exception("Error , Data Fetching has been canceled ."))
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                urlPreviewer.responseListener?.onData(urlPreviewer.metaData)
            }
        }
    }
}