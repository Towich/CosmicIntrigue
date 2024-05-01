package com.towich.cosmicintrigue.data.source

import android.util.Log
import com.google.android.gms.maps.model.Marker
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CustomMath {
    companion object {
        fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val radius = 6371 // Радиус Земли в километрах
            val latDistance = Math.toRadians(lat2 - lat1)
            val lonDistance = Math.toRadians(lon2 - lon1)
            val a = sin(latDistance / 2) * sin(latDistance / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(lonDistance / 2) * sin(lonDistance / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return radius * c
        }

        fun checkIfPlayerIsNear(
            ourGeoPos: GeoPositionModel?,
            otherPlayerGeoPos: Marker?
        ): Boolean {
            if (ourGeoPos == null || otherPlayerGeoPos == null) {
                return false
            }

            val distance = distanceInKm(
                lat1 = ourGeoPos.latitude ?: 0.0,
                lon1 = ourGeoPos.longitude ?: 0.0,
                lat2 = otherPlayerGeoPos.position.latitude,
                lon2 = otherPlayerGeoPos.position.longitude
            )

            Log.i("CustomMath", "distance = ${distance * 1000} m.")

            return distance < Constants.ACTION_DISTANCE
        }
    }
}