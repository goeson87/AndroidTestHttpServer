package nano.httpd.server.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import nano.httpd.server.app.Constants
import nano.httpd.server.app.Constants.TAG
import nano.httpd.server.app.server.WebServer
import java.io.IOException

class WebService : Service() {

    private val binder = LocalBinder()
    private val webServer = WebServer(Constants.SERVER_PORT)

    var isWebServiceAlive : Boolean = false
        private set
        get() = webServer.isAlive

    inner class LocalBinder : Binder() {
        fun getService(): WebService = this@WebService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun startWebServer(onSuccess: (String) -> Unit, onError:(String) -> Unit) {
        try {
            webServer.start()
            Log.d(TAG, "Web Server started.")
            onSuccess("webServer started.")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "Web Server not started: $e")
            onError(e.toString())
        }
    }

    fun stopWebServer(onComplete: (String) -> Unit = {}) {
        webServer.stop()
        Log.d(TAG, "webServer stopped.")
        onComplete("Web Server stopped.")
    }
}