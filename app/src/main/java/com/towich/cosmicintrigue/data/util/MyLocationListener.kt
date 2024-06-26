package com.towich.cosmicintrigue.data.util

import android.location.Location
import android.location.LocationListener

class MyLocationListener(
    private val onMyLocationChanged: (newLocation: Location) -> Unit
) : LocationListener {
    override fun onLocationChanged(p0: Location) {
        onMyLocationChanged(p0)
    }
}