package nano.httpd.server.app

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import nano.httpd.server.app.utils.NetworkUtils

class MainActivity : AppCompatActivity() {

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.containsValue(false)) {
                finishAndToast("Please allow external storage permission.")
            }
        }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        if(!NetworkUtils.isWiFiEnabled(this)){
            finishAndToast("Please connect WiFi and launch app again.")
            return
        }

        activityResultLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun finishAndToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        finish()
    }
}