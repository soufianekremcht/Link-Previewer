package com.soufianekre.linkpreviewer.data

import java.io.Serializable

data class UrlPreviewItem @JvmOverloads constructor(var url : String? = "",
                                                    var title:String? = "",
                                                    var description:String? = "",
                                                    var imageUrl :String?= "",
                                                    var siteName:String?= "",
                                                    var mediaType :String?= "",
                                                    var favicon:String?= "") : Serializable {
}