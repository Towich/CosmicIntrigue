package com.towich.cosmicintrigue.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.towich.cosmicintrigue.data.util.MyLocationListener
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.databinding.ActivityMapsBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel
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

    }
}