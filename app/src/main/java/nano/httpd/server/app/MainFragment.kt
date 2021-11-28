package nano.httpd.server.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nano.httpd.server.app.databinding.FragmentMainBinding
import nano.httpd.server.app.service.WebService
import nano.httpd.server.app.utils.NetworkUtils
import java.lang.StringBuilder

class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    private lateinit var webService: WebService
    private var bound: Boolean = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as WebService.LocalBinder
            this@MainFragment.webService = binder.getService()
            bound = true
            binding.log = "WebService connected."
            Log.d(Constants.TAG, "WebService connected.")
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
            binding.log = "WebService disconnected."
            Log.d(Constants.TAG, "WebService disconnected.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAction()
    }

    private fun initEvent() {
        binding.btn.setOnClickListener {
            toggleWebServer()
        }
    }

    private fun initAction() {
        binding.description = getDescription()
        binding.isRunning = false

        startService()
    }

    private fun getDescription(): String {
        return StringBuilder().apply {
            val ipAddress = "http://${NetworkUtils.getLocalIpStr(requireContext())}:${Constants.SERVER_PORT}"
            append("[Supported APIs]").appendLine()
            Constants.API_LIST.forEach { apiPath ->
                append(" - ").append(ipAddress).append(apiPath).appendLine()
            }

            appendLine()

            append("[Json file location]").appendLine()
            Constants.FILE_LIST.forEach { filePath ->
                append(" - ").append(filePath).appendLine()
            }

            appendLine()

            append("[ADB command - Push file to device]").appendLine()
            append(" - ").append("adb push [fileName].json /sdcard/Download/").appendLine()
        }.toString()
    }

    private fun toggleWebServer() {
        if(!bound) {
            Log.d(Constants.TAG, "WebService not connected.")
            return
        }
        Log.d(Constants.TAG, "webServer isAlive= ${webService.isWebServiceAlive}")

        if(webService.isWebServiceAlive) {
            webService.stopWebServer {
                binding.log = it
                binding.isRunning = false
            }
        } else {
            webService.startWebServer(
                onSuccess = {
                    binding.isRunning = true
                    binding.log = it
                },
                onError = {
                    binding.isRunning = false
                    binding.log = it

                }
            )
        }
    }

    private fun startService(){
        context?.run {
            if(!bound) {
                Intent(this, WebService::class.java).also { intent ->
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
            }
        }
    }

    private fun stopService() {
        context?.run {
            if(bound) {
                unbindService(connection)
                bound = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopService()
    }
}