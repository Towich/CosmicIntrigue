package com.towich.cosmicintrigue.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.ActivityMapsBinding
import com.towich.cosmicintrigue.ui.util.App
import io.reactivex.disposables.CompositeDisposable


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize websocket connection
        (application as App).repository.initGeoPositionsStompClient(compositeDisposable)

        val navController = findNavController(R.id.nav_host_activity_maps)
    }
}