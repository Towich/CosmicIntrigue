package com.towich.cosmicintrigue.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.ActivityMapsBinding
import com.towich.cosmicintrigue.ui.util.App
import io.reactivex.disposables.CompositeDisposable


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private val compositeDisposable = CompositeDisposable()
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize websocket connection
        (application as App).repository.initGeoPositionsStompClient(
            compositeDisposable = compositeDisposable,
            onOpened = {

            },
            onError = {
                Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_LONG).show()
            },
            onFailedServerHeartbeat = {

            },
            onClosed = {
                Toast.makeText(applicationContext, getString(R.string.connection_closed), Toast.LENGTH_LONG).show()
            }
        )

        val navController = findNavController(R.id.nav_host_activity_maps)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.tap_one_more_time_to_exit), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(
            { doubleBackToExitPressedOnce = false },
            2000
        )
    }
}