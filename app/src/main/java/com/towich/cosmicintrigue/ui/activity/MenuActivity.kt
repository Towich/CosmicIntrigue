package com.towich.cosmicintrigue.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.databinding.ActivityMenuBinding
import com.towich.cosmicintrigue.ui.util.App
import io.reactivex.disposables.CompositeDisposable

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize websocket connection
        val repository = (application as App).repository
        repository.initGeoPositionsStompClient(compositeDisposable)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
    }
}