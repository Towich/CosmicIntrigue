package com.towich.cosmicintrigue.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.towich.cosmicintrigue.data.source.CustomMath
import com.towich.cosmicintrigue.data.util.MyLocationListener
import com.towich.cosmicintrigue.databinding.FragmentMapBinding
import com.towich.cosmicintrigue.ui.util.App
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


        initTopics()
        initButtons()
        initObservers()

        viewModel.getStartTaskMarks()
        getLocationUpdates()
        startLocationUpdates()
    }

    private fun initTopics() {

        // Topic for getting players geo positions
        viewModel.subscribeGeoPosTopic { geoPosition: GeoPositionModel ->
            if (geoPosition.latitude != null && geoPosition.longitude != null) {
                val latLngGeoPos = LatLng(geoPosition.latitude, geoPosition.longitude)
                val ourPlayerId = viewModel.getPlayerId() ?: -1

                for (i in 0 until listOfUsersMarks.size) {
                    if (listOfUsersMarks[i].first == geoPosition.id) {
                        listOfUsersMarks[i].second?.remove()
                        Log.i("MapFragment", "Removed player $i in listOfUsersMarks")
                    }
                }

                // If we got dead player from topic
                if(geoPosition.isDead){
                    if(geoPosition.id == ourPlayerId){
                        viewModel.dispose()
                        stopLocationUpdates()
                    }

                    listOfUsersMarks.removeIf { it.first == geoPosition.id }

                    return@subscribeGeoPosTopic
                }

                val newMarker = map.addMarker(
                    MarkerOptions()
                        .position(latLngGeoPos)
                        .title(getString(R.string.player) + " ${geoPosition.id}")
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                if (ourPlayerId == geoPosition.id)
                                    BitmapDescriptorFactory.HUE_RED
                                else
                                    BitmapDescriptorFactory.HUE_YELLOW
                            )
                        )
                )

                if(!listOfUsersMarks.map { it.first }.contains(geoPosition.id)){
                    listOfUsersMarks.add(Pair(geoPosition.id, newMarker))
                }
                else{
                    for(i in 0 until listOfUsersMarks.size){
                        if(listOfUsersMarks[i].first == geoPosition.id){
                            listOfUsersMarks[i] = Pair(geoPosition.id, newMarker)
                        }
                    }
                }



                // If we are Imposter --> check if we can kill player if he's near
                if (viewModel.getIsImposter() == true) {

                    // Getting our location
                    var ourLocation: GeoPositionModel? = null
                    for (mark in listOfUsersMarks) {
                        if (mark.first == ourPlayerId) {
                            ourLocation = GeoPositionModel(
                                id = ourPlayerId,
                                latitude = mark.second?.position?.latitude,
                                longitude = mark.second?.position?.longitude,
                                isDead = false
                            )
                            break
                        }
                    }

                    // Looking for any player which are nearly with us
                    var foundedClosePlayerPairMark: Pair<Long, Marker?>? = null
                    for (pairMark in listOfUsersMarks) {
                        if (pairMark.first != ourLocation?.id && CustomMath.checkIfPlayerIsNear(
                                ourGeoPos = ourLocation,
                                otherPlayerGeoPos = pairMark.second
                            )
                        ) {
                            foundedClosePlayerPairMark = pairMark
                        }
                    }

                    // If we found one
                    if (foundedClosePlayerPairMark != null) {
                        changeKillFABStatus(true, foundedClosePlayerPairMark)
                        Log.i(
                            "MapFragment",
                            "We are ready to kill Player #${foundedClosePlayerPairMark.first}!"
                        )
                    } else {
                        changeKillFABStatus(false, null)
                    }
                }
            }
        }

        // Topic for getting tasks geo positions
        viewModel.subscribeCoordinatesTopic { listOfTasksGeoPositions ->
            map.clear()
            listOfTasksMarks.clear()

            listOfTasksGeoPositions.forEach {
                Log.i(
                    "MapFragment",
                    "id = ${it.id}, latitude = ${it.latitude}, longitude = ${it.longitude}"
                )

                if (it.latitude != null && it.longitude != null) {
                    listOfTasksMarks.add(
                        Pair(
                            first = it.id,
                            second = map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(it.latitude, it.longitude))
                                    .title(getString(R.string.mapTask) + " ${it.id}")
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

            viewModel.countCurrTaskMarks.value = listOfTasksGeoPositions.size
        }
    }

    private fun initButtons() {
        binding.voteFab.setOnClickListener {
            findNavController().navigate(R.id.action_Map_to_Vote)
        }

        if (viewModel.getIsImposter() == true) {
            binding.killFab.visibility = View.VISIBLE
            binding.killTextView.visibility = View.VISIBLE
            binding.killFab.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            binding.killFab.setOnClickListener {

                // Send dead player to topic
                val playerToKill = viewModel.getCurrPlayerIdToKill()
                if(playerToKill != null){
                    viewModel.sendGeoPosition(
                        geoPosition = GeoPositionModel(
                            id = playerToKill,
                            latitude = 0.0,
                            longitude = 0.0,
                            isDead = true
                        )
                    )

                    changeKillFABStatus(false, null)
                }
            }
        }

        binding.buttonmap.setOnClickListener {
            findNavController().navigate(R.id.action_Map_to_Task)
        }
    }

    private fun initObservers() {
        // Observer for getting count of current tasks
        val countCurrTasksObserver = Observer<Int> { currCount ->
            val completedTasks = viewModel.totalTaskMarks.value?.minus(currCount)
            binding.completedTasksTextView.text = completedTasks.toString()
            binding.progressBar2.progress = completedTasks ?: 0
        }
        viewModel.countCurrTaskMarks.observe(viewLifecycleOwner, countCurrTasksObserver)

        // Observer for getting total count of tasks
        val totalTasksObserver = Observer<Int> { totalTasks ->
            binding.totalTasksTextView.text = totalTasks.toString()
            binding.progressBar2.max = totalTasks
        }
        viewModel.totalTaskMarks.observe(viewLifecycleOwner, totalTasksObserver)
    }

    override fun onMapReady(googleMap: GoogleMap) {
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

        map = googleMap
        // Add a marker in Moscow and move the camera
        val park50years = LatLng(55.684132, 37.502607)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(park50years, 14.5f))

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
                                    .title(getString(R.string.mapTask) + " ${taskGeo.id}")
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
                            id = viewModel.getPlayerId() ?: -1,
                            latitude = location?.latitude,
                            longitude = location?.longitude,
                            isDead = false
                        )
                    )


                    val taskIdToShow = getTaskIdToCompleteIfNearby(currLocation = location)
                    if (taskIdToShow != null) {
                        viewModel.setCurrTaskId(taskIdToShow)
                        Log.i("MapFragment", "Task ${taskIdToShow} is ready for start completing!")
                        binding.buttonmap.visibility = View.VISIBLE
                        binding.buttonmap.text = "Выполнить задание #$taskIdToShow"
                    } else {
                        viewModel.setCurrTaskId(-1)
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

    private fun stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getTaskIdToCompleteIfNearby(currLocation: Location?): Long? {
        if (currLocation == null) {
            return null
        }

        var foundedTaskId: Long? = null
        for (taskGeoPos in listOfTasksMarks) {
            if (taskGeoPos.second != null) {
                val distance = CustomMath.distanceInKm(
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
                if (distance < Constants.ACTION_DISTANCE) {
                    foundedTaskId = taskGeoPos.first
                }
            }
        }
//        return null
        return foundedTaskId
    }

    private fun changeKillFABStatus(isActive: Boolean, foundedClosePlayerPairMark: Pair<Long, Marker?>?){
        if(isActive){
            binding.killTextView.text =
                getString(R.string.kill) + " #${foundedClosePlayerPairMark?.first}"
            binding.killFab.isEnabled = true
            binding.killFab.backgroundTintList = ColorStateList.valueOf(Color.RED)
            viewModel.setCurrPlayerIdToKill(id = foundedClosePlayerPairMark?.first)
        }
        else{
            binding.killTextView.text = ""
            binding.killFab.isEnabled = false
            binding.killFab.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            viewModel.setCurrPlayerIdToKill(id = null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.dispose()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}