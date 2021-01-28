package com.soufianekre.urlpreviewer

import android.os.AsyncTask
import android.util.Log
import android.webkit.URLUtil
import com.soufianekre.urlpreviewer.data.UrlPreviewItem
import com.soufianekre.urlpreviewer.listeners.ResponseListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException


internal class UrlPreviewHelper() {


    companion object {

        fun getPreview(url: String?,responseListener: ResponseListener?) {
            UrlDataAsync(url!!, responseListener).execute()
        }
        class UrlDataAsync(
            private val url: String,
            private val responseListener: ResponseListener?) :
            AsyncTask<Void?, Void?, UrlPreviewItem>() {

            override fun onPreExecute() {
                super.onPreExecute()

            }

            override fun doInBackground(vararg params: Void?): UrlPreviewItem {
                try {
                    var startTime: Long = System.currentTimeMillis()
                    var doc: Document? = null
                    doc = Jsoup.connect(url)
                        .timeout(30 * 1000)
                        .get()


                    if (doc == null) {
                        return UrlPreviewItem(url, "", "",
                            "", "", "", "")
                    }

                    val title: String = getTitle(doc)
                    val imageUrl: String = getImageUrl(doc)
                    val desc: String = getDesc(doc)



                    return UrlPreviewItem(url, title, desc, imageUrl, "", "", "")

                } catch (e: IOException) {
                    Log.e("Async WebPreview ",e.localizedMessage);
                    /*
                    responseListener?.onError(
                        Exception(
                            "No Html Received from " +
                                    url + " Check your Internet " + e.localizedMessage
                        )
                    )

                     */
                    return UrlPreviewItem(url, "", "", "", "", "", "")
                }
            }

            override fun onCancelled() {
                super.onCancelled()
                responseListener?.onError(Exception("Error , Data Fetching has been canceled ."))
            }

            override fun onPostExecute(result: UrlPreviewItem?) {
                super.onPostExecute(result)
                responseListener?.onResponse(result)
            }

            private fun getTitle(doc: Document): String {
                // Url title
                var title: String = doc.select("meta[property=og:title]").attr("content")
                if (title.isEmpty()) {
                    title = doc.title()
                }
                return title
            }

            private fun getDesc(doc: Document): String {
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

                return description
            }

            private fun getImageUrl(doc: Document): String {
                var imageUrl: String? = "";
                val imageElements: Elements = doc.select("meta[property=og:image]")

                if (imageElements.size > 0) {
                    val image: String = imageElements.attr("content")
                    if (image.isNotEmpty()) {
                        imageUrl = resolveURL(url, image)!!
                        return imageUrl
                    }
                }

                if (imageUrl!!.isEmpty()) {
                    val cssQueries: ArrayList<String> = ArrayList()
                    cssQueries.add("link[rel=image_src]")
                    cssQueries.add("link[rel=apple-touch-icon]")
                    cssQueries.add("link[rel=icon]")
                    for (i in 0 until cssQueries.size) {
                        val src = doc.select(cssQueries[i]).attr("href")
                        if (src.isNotEmpty()) {
                            imageUrl = resolveURL(url, src)
                            return imageUrl!!
                        }
                    }
                }
                return "";
            }

            private fun getMediaType(doc: Document): String {

                var mediaType: String = "";
                val mediaTypes: Elements = doc.select("meta[name=medium]")
                var type = ""
                type = if (mediaTypes.size > 0) {
                    val media: String = mediaTypes.attr("content")
                    if (media == "image") "photo" else media
                } else {
                    doc.select("meta[property=og:type]").attr("content")
                }
                mediaType = type
                return mediaType
            }

            private fun getFavIcon(doc: Document): String {

                var favIcon: String? = "";
                // Url MediaTyp

                var src: String = doc.select("link[rel=apple-touch-icon]").attr("href")
                if (src.isNotEmpty()) {
                    favIcon = resolveURL(url, src)
                } else {
                    src = doc.select("link[rel=icon]").attr("href")
                    if (src.isNotEmpty()) {
                        favIcon = resolveURL(url, src)
                    }
                }
                return favIcon!!
            }

            private fun getSiteName(doc: Document): String {
                var siteName: String = "";
                val elements: Elements = doc.getElementsByTag("meta")
                for (element in elements) {
                    if (element.hasAttr("property")) {
                        val strProperty: String = element.attr("property").toString().trim()
                        /*
                        if (strProperty == "og:url") {
                            .url = element.attr("content").toString()
                        }

                         */
                        if (strProperty == "og:site_name") {
                            siteName =
                                (element.attr("content").toString())
                        }
                    }
                }
                return siteName
            }


            private fun getUrl(): String {
                var link_url: String = "";

                if (link_url.isEmpty()) {
                    var uri: URI? = null
                    try {
                        uri = URI(url)
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                    link_url = uri!!.host
                }
                return link_url
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
        }
    }
}