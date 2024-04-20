package com.towich.cosmicintrigue.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.util.MyLocationListener
import com.towich.cosmicintrigue.databinding.FragmentMapBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val viewModel: MapViewModel by viewModels {
        (requireContext().applicationContext as App).viewModelFactory
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocationListener: MyLocationListener

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val listOfMarks = mutableListOf<Pair<Long, Marker?>>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
                binding.buttonFirst.setOnClickListener {
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }
        */

        viewModel.initGeoPositionsStompClient(
            onReceivedGeoPosition = { geoPosition: GeoPositionModel ->
                Toast.makeText(requireContext(), "New mark!", Toast.LENGTH_SHORT).show()
                if (geoPosition.latitude != null && geoPosition.longitude != null) {
                    val latLngGeoPos = LatLng(geoPosition.latitude, geoPosition.longitude)

                    for(mark in listOfMarks){
                        if(mark.first == geoPosition.id){
                            mark.second?.remove()
                        }
                    }

                    val marker = map.addMarker(MarkerOptions()
                        .position(latLngGeoPos)
                        .title("YAY")
                    )

                    listOfMarks.add(Pair(geoPosition.id, marker))

                }
            }
        )

        getLocationUpdates()
        startLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
        }
        googleMap.isMyLocationEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocationUpdates() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext().applicationContext)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMinUpdateDistanceMeters(0f)
            .setMinUpdateIntervalMillis(1000L)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation

                    viewModel.sendGeoPosition(
                        GeoPositionModel(
                            id = 2,
                            latitude = location?.latitude,
                            longitude = location?.longitude
                        )
                    )
                    Log.i("MY_LOCATION", "${location?.longitude} ${location?.latitude}")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }
}