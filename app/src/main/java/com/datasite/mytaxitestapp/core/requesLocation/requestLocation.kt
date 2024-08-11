package com.datasite.mytaxitestapp.core.requesLocation

import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationServiceFactory


fun requestLocation(onLocationRequested: (location : Location?) -> Unit){
    val request = LocationProviderRequest.Builder().apply {
        interval(
            IntervalSettings.Builder()
                .interval(0L)
                .minimumInterval(0L)
                .maximumInterval(0L)
                .build()
        )
        accuracy(AccuracyLevel.HIGHEST)
        displacement(0F)

    }.build()

    val locationServise = LocationServiceFactory.getOrCreate()
    val expected = locationServise.getDeviceLocationProvider(request)
    val provider = expected.value
    provider?.getLastLocation{
        onLocationRequested(it)
    }

}