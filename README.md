# Url Previewer
URL Previewer Library For Android built in Kotlin.

[![](https://jitpack.io/v/SoufianeKreX/UrlPreviewer.svg)](https://jitpack.io/#SoufianeKreX/UrlPreviewer)

### Still In Development . It not Working For The current Moment
### I Welcome Anyone who can help add Improvement on this project.


## Gradle Dependency

for android studio 3.x

add this to app level build gradle file

~~~gradle
allprojects {
	repositories {
	...
	maven { url 'https://jitpack.io' }
	}
}
~~~

Add this to your module build gradle file

~~~gradle
dependencies {
	implementation 'com.github.SoufianeKreX:UrlPreviewer:0.0.1'
}
~~~

## The Usage

##### Add the view to xml Layout 

~~~xml
<com.soufianekre.urlpreviewer.views.UrlPreviewItemView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
~~~
##### Add the following code to your kotlin class

~~~kotlin
urlPreviewView.setUrl(url,object : PreviewListener {
            override fun onSuccess(metadata: UrlMetaData, status: Boolean) {
                Toast.makeText(context,
                    "The preview Is Working", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,
                    "Preview Error" + e?.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })
~~~
#### That is it . you can now see your Url Preview as CardView

### Getting Url Data and Implementing a your Custom View  (Not yet)
