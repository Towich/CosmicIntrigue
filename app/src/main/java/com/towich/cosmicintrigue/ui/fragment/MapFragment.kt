package com.towich.cosmicintrigue.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.towich.cosmicintrigue.R
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.source.Constants
import com.towich.cosmicintrigue.data.util.MyLocationListener
import com.towich.cosmicintrigue.databinding.FragmentMapBinding
import com.towich.cosmicintrigue.ui.util.App
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    private val listOfUsersMarks = mutableListOf<Pair<Long, Marker?>>()
    private val listOfTasksMarks = mutableListOf<Pair<Long, Marker?>>()

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

        viewModel.subscribeGeoPosTopic { geoPosition: GeoPositionModel ->
            if (geoPosition.latitude != null && geoPosition.longitude != null) {
                val latLngGeoPos = LatLng(geoPosition.latitude, geoPosition.longitude)

                for (mark in listOfUsersMarks) {
                    if (mark.first == geoPosition.id) {
                        mark.second?.remove()
                    }
                }

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(latLngGeoPos)
                        .title("YAY")
                )

                listOfUsersMarks.add(Pair(geoPosition.id, marker))

            }
        }


        viewModel.subscribeCoordinatesTopic { listOfTasksGeoPositions ->
            map.clear()
            listOfTasksMarks.clear()

            listOfTasksGeoPositions.forEach {
                Log.i(
                    "MapFragment",
                    "id = ${it.id}, latitude = ${it.latitude}, longitude = ${it.longitude}"
                )

                if(it.latitude != null && it.longitude != null) {
                    listOfTasksMarks.add(
                        Pair(
                            first = it.id,
                            second = map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(it.latitude, it.longitude))
                                    .title("Task")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                            )
                        )
                    )
                }
            }
        }



        binding.floatingActionButton2.setOnClickListener {
            viewModel.sendTaskGeoPositionModel(
                TaskGeoPositionModel(3, 10.0, 10.0)
            )

            Log.i(
                "MapFragment",
                "distance = ${distanceInKm(55.801017, 37.805728, 55.670091, 37.480906)}"
            )
        }

        binding.buttonmap.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_TaskFragment4)
        }

        viewModel.getStartTaskMarks()
        getLocationUpdates()
        startLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Add a marker in Moscow and move the camera
        val park50years = LatLng(55.684132, 37.502607)
//        googleMap.addMarker(MarkerOptions().position(park50years).title("Marker in Park 50 years"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park50years, 14.5f))

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

        // Create the observer which updates the UI.
        val nameObserver = Observer<List<TaskGeoPositionModel>> { listOfTaskGeoPositions ->
            // Update the UI
            for (taskGeo in listOfTaskGeoPositions) {
                if (taskGeo.latitude != null && taskGeo.longitude != null && taskGeo.completed != true) {
                    listOfTasksMarks.add(
                        Pair(
                            taskGeo.id,
                            map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(taskGeo.latitude, taskGeo.longitude))
                                    .title("Task ${taskGeo.id}")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                            )
                        )
                    )
                }
            }
        }

        viewModel.currentTaskMarks.observe(viewLifecycleOwner, nameObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.dispose()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getLocationUpdates() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext().applicationContext)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMinUpdateDistanceMeters(0f)
            .setMinUpdateIntervalMillis(5000L)
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


                    val taskIdToShow = getTaskIdToCompleteIfNearby(currLocation = location)
                    if(taskIdToShow != null){
                        Log.i("MapFragment", "Task ${taskIdToShow} is ready for start completing!")
                        binding.buttonmap.visibility = View.VISIBLE
                        binding.buttonmap.text = "Выполнить задание #$taskIdToShow"
                    }
                    else{
                        binding.buttonmap.visibility = View.GONE
                    }

                    Log.i("MY_LOCATION", "${location?.latitude} ${location?.longitude}")
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

    private fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radius = 6371 // Радиус Земли в километрах
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun getTaskIdToCompleteIfNearby(currLocation: Location?): Long? {
        if(currLocation == null){
            return null
        }

        var foundedTaskId: Long? = null
        for(taskGeoPos in listOfTasksMarks){
            if(taskGeoPos.second != null){
                val distance = distanceInKm(
                    lat1 = currLocation.latitude,
                    lon1 = currLocation.longitude,
                    lat2 = taskGeoPos.second?.position?.latitude ?: 0.0,
                    lon2 = taskGeoPos.second?.position?.longitude ?: 0.0
                )
//                Log.i("MapFragment", "CHECK DISTANCE | my location: lat = ${currLocation.latitude} lon = ${currLocation.longitude}")
//                Log.i("MapFragment", "CHECK DISTANCE | id = ${taskGeoPos.first}, distance = ${distance} km.")
//                Log.i("MapFragment", "CHECK DISTANCE | id = ${taskGeoPos.first}, latitude = ${taskGeoPos.second?.position?.latitude}")
//                Log.i("MapFragment", "CHECK DISTANCE | id = ${taskGeoPos.first}, longitude = ${taskGeoPos.second?.position?.longitude}")
//                Log.i("MapFragment", "-------------------------------------------------------------------------------------------------")
                if(distance < Constants.DISTANCE_TO_COMPLETE_QUEST){
//                    return taskGeoPos.first
                    foundedTaskId = taskGeoPos.first
                }
            }
        }
//        return null
        return foundedTaskId
    }
}