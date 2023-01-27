package test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.template.jellymerge.R

class MainActivity : AppCompatActivity() {

    private lateinit var manager: SplitInstallManager
    private lateinit var status: TextView
    private lateinit var openGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = SplitInstallManagerFactory.create(this);
        init()
    }

    private fun init() {
        status = findViewById<TextView>(R.id.status).apply {

        }
        findViewById<Button>(R.id.download).apply {
            setOnClickListener {
                val request =
                    SplitInstallRequest
                        .newBuilder()
                        .addModule("unityLibrary3")
                        .build()
                manager.startInstall(request)
            }
        }
        findViewById<Button>(R.id.updateStatus).apply {
            setOnClickListener {
                updateStatus()
            }
        }
        openGame = findViewById<Button>(R.id.launch).apply {
            setOnClickListener {
                val intent = Intent().apply {
                    setClassName(packageName!!, "com.unity3d.player.UnityPlayerActivity3")
                }
                startActivity(intent)
            }
        }
        manager.registerListener {
            updateStatus(it)
        }
        updateStatus()
    }

    private fun updateStatus(state: SplitInstallSessionState) {
        when (state.status()) {
            SplitInstallSessionStatus.CANCELED, SplitInstallSessionStatus.CANCELING, SplitInstallSessionStatus.FAILED -> {
                status.text = "not installed"
            }
            SplitInstallSessionStatus.DOWNLOADING -> {
                status.text = "Downloading..."
            }
            SplitInstallSessionStatus.INSTALLING, SplitInstallSessionStatus.INSTALLED -> {
                status.text = "installed"
            }
            SplitInstallSessionStatus.PENDING -> {
                status.text = "pending"
            }
        }
    }

    private fun updateStatus() {
        if (manager.installedModules.contains("unityLibrary3")) {
            status.setText("installed")
            openGame.isClickable = true
        } else {
            status.setText("not installed")
            openGame.isClickable = false
        }
    }

}