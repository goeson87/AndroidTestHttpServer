package nano.httpd.server.app

import android.os.Environment

object Constants {
    const val TAG = "NANO_HTTPD"


    val PATH_DOWNLOAD_DIR = "${Environment.getExternalStorageDirectory()}/Download"
    val PATH_JSON_ANDROID = "$PATH_DOWNLOAD_DIR/android.json"
    val PATH_JSON_IOS = "$PATH_DOWNLOAD_DIR/ios.json"

    const val SERVER_PORT = 8080
    const val API_ANDROID = "/test/android"
    const val API_IOS = "/test/ios"

    val FILE_LIST = arrayOf(PATH_JSON_ANDROID, PATH_JSON_IOS)
    val API_LIST = arrayOf(API_ANDROID, API_IOS)
}