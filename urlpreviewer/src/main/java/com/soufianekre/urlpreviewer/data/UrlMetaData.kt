package com.soufianekre.urlpreviewer.data

data class UrlMetaData(var url : String? = "",
                       var title:String? = "",
                       var description:String? = "",
                       var imageUrl :String?= "",
                       var siteName:String?= "",
                       var mediaType :String?= "",
                       var favicon:String?= "") {
}