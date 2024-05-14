package com.towich.cosmicintrigue

import com.google.android.gms.maps.model.Marker
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.source.CustomMath
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun distanceInKm_isCorrect(){
        val distance = CustomMath.distanceInKm(55.801121, 37.805680, 55.794851, 37.808896)
        assertEquals(0.7255, distance, 0.020)
    }

    @Test
    fun distanceInKm_isNotCorrect(){
        val distance = CustomMath.distanceInKm(55.801121, 37.805680, 55.794851, 37.808896)
        assertNotEquals(0.4, distance, 0.020)
    }

    @Test
    fun checkIfPlayerIsNear_isCorrect_1(){
        val ourGeoPos = GeoPositionModel(id = 1, latitude = 55.801121, longitude = 37.805680, dead = false)
        val otherPlayerGeoPos = GeoPositionModel(id = 2, latitude = 55.801221, longitude = 37.805680, dead = false)

        assertEquals(true, CustomMath.checkIfPlayerIsNear(ourGeoPos, otherPlayerGeoPos))
    }

    @Test
    fun checkIfPlayerIsNear_isCorrect_2(){
        val ourGeoPos = GeoPositionModel(id = 1, latitude = 55.801121, longitude = 37.805780, dead = false)
        val otherPlayerGeoPos = GeoPositionModel(id = 2, latitude = 55.801121, longitude = 37.805680, dead = false)

        assertEquals(true, CustomMath.checkIfPlayerIsNear(ourGeoPos, otherPlayerGeoPos))
    }

    @Test
    fun checkIfPlayerIsNear_isNotCorrect(){
        val ourGeoPos = GeoPositionModel(id = 1, latitude = 55.801121, longitude = 37.805680, dead = false)
        val otherPlayerGeoPos = GeoPositionModel(id = 2, latitude = 55.794851, longitude = 37.80889, dead = false)

        assertEquals(false, CustomMath.checkIfPlayerIsNear(ourGeoPos, otherPlayerGeoPos))
    }
}