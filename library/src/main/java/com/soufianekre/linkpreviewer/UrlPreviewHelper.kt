package com.soufianekre.linkpreviewer

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.URLUtil
import com.soufianekre.linkpreviewer.data.UrlPreviewItem
import com.soufianekre.linkpreviewer.listeners.ResponseListener
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit


internal class UrlPreviewHelper() {


    companion object {

        fun getPreview(url: String?, responseListener: ResponseListener?) {
            UrlDataAsync(url!!, responseListener).execute()
        }

        class UrlDataAsync(
            private val url: String,
            private val responseListener: ResponseListener?
        ) : AsyncTask<Void?, Void?, UrlPreviewItem>() {

            override fun onPreExecute() {
                super.onPreExecute()

            }

            override fun doInBackground(vararg params: Void?): UrlPreviewItem {
                try {
                    val startTime: Long = System.currentTimeMillis()
                    val okHttpClient = OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();   // socket timeout

                    val request: Request = Request.Builder().url(url).get().build()
                    val doc = Jsoup.parse(okHttpClient.newCall(request).execute().body()!!.string())
                    val finishTime: Long = System.currentTimeMillis()

                    //val document = Jsoup.connect(url).timeout(10*1000).get()

                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        Log.e(
                            "UrlPreviewHelper",
                            "Link Parsing Time :  ${finishTime - startTime}"
                        )
                    }


                    if (doc == null) {
                        return UrlPreviewItem(
                            url, "", "",
                            "", "", "", ""
                        )
                    }


                    val title: String = getTitle(doc)
                    val imageUrl: String = getImageUrl(doc)
                    val desc: String = getDesc(doc)
                    val mediaType: String = getMediaType(doc)
                    val siteName: String = getSiteName(doc)
                    val favicon: String = getFavIcon(doc)

                    return UrlPreviewItem(url, title, desc, imageUrl, siteName, mediaType, favicon)
                } catch (e: IOException) {
                    Log.e("UrlPreviewHelper ", e.localizedMessage);
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
                responseListener?.onError(Exception("Error , Link Data Fetching has been canceled ."))
            }

            override fun onPostExecute(result: UrlPreviewItem?) {
                super.onPostExecute(result)
                responseListener?.onResponse(result)
            }

            //

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

                var mediaType = "";
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
                var siteName = "";
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
                var linkUrl = "";
                if (linkUrl.isEmpty()) {
                    var uri: URI? = null
                    try {
                        uri = URI(url)
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                    linkUrl = uri!!.host
                }
                return linkUrl
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