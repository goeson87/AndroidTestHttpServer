package nano.httpd.server.app.server

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import nano.httpd.server.app.Constants
import nano.httpd.server.app.Constants.API_ANDROID
import nano.httpd.server.app.Constants.API_IOS
import nano.httpd.server.app.Constants.PATH_JSON_ANDROID
import nano.httpd.server.app.Constants.PATH_JSON_IOS
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class WebServer(port: Int) : NanoHTTPD(port) {

    override fun serve(
        uri: String?,
        method: Method?,
        headers: MutableMap<String, String>?,
        parms: MutableMap<String, String>?,
        files: MutableMap<String, String>?
    ): Response {

        Log.d(Constants.TAG, "serve uri = $uri")

        return when (uri) {
            API_ANDROID -> getJsonResponse(PATH_JSON_ANDROID)
            API_IOS -> getJsonResponse(PATH_JSON_IOS)
            else -> Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        }
    }

    private fun getJsonResponse(path: String): Response {
        val response = try {
            val jsonFile = FileReader(path)
            val bufferedReader = BufferedReader(jsonFile)
            val stringBuilder = StringBuilder()

            bufferedReader.forEachLine {
                stringBuilder.append(it)
            }

            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            e.toString()
        }

        Log.d(Constants.TAG, "getJsonResponse, response = $response")
        return Response(response)
    }

}