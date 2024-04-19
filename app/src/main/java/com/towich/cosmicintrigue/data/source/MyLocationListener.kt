package com.towich.cosmicintrigue.data.source

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback


internal class MyLocationListener(
    private val context: Context
) : LocationListener {

    override fun onLocationChanged(p0: Location) {
        imHere = p0
        Toast.makeText(context, "${p0.latitude} ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}


    companion object {
        var imHere: Location? =
            null // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

        fun setUpLocationListener(context: Context, ) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationListener: LocationListener = MyLocationListener(context)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "No permissions!", Toast.LENGTH_SHORT).show()
                return
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1f,
                locationListener
            )

            imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }
}