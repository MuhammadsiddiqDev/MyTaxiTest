package com.datasite.mytaxitestapp.screen.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.camera.CameraPosition
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.datasite.mytaxitestapp.R
import com.datasite.mytaxitestapp.screen.bitmap.drawableToBitmap
import com.datasite.mytaxitestapp.screen.compose.bottomSheet.bottomSheet
import com.datasite.mytaxitestapp.screen.compose.myButton.iconButton
import com.datasite.mytaxitestapp.screen.compose.myTheme.MyApp
import com.datasite.mytaxitestapp.core.state.MapState
import com.datasite.mytaxitestapp.core.viewModel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode


class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this)

        mapView = MapView(this)
        mapView.onCreate(savedInstanceState)

        setContent {
            MyApp {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val state = viewModel.state.collectAsState().value

                    MapLibreView(mapView, state)

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}


@Composable
fun MapLibreView(mapView: MapView, state: MapState) {

    var clicked by remember { mutableStateOf(false) }
    var showBottomSheet1 = remember { mutableStateOf(false) }
    var cameraPosition by remember { mutableStateOf(state.cameraPosition) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<LatLng?>(null) }




    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    location = LatLng(loc.latitude, loc.longitude)
                    cameraPosition = CameraPosition.Builder()
                        .target(location)
                        .zoom(15.0)
                        .build()
                } else {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ConstraintLayout {

        val (map, menuButton, scoreButton, zoomIn, zoomOut, locationButton, bottomSheetButton, switchButton) = createRefs()

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(map) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            factory = { mapView },
            update = { mapView ->
                mapView.getMapAsync { mapLibreMap ->
                    mapLibreMap.setStyle(state.styleUrl) { style ->

                        // Ensure permissions are granted
                        if (hasLocationPermission) {
                            // Activate the LocationComponent
                            val locationComponent = mapLibreMap.locationComponent
                            locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions.builder(context, style).build()
                            )

                            // Enable the LocationComponent
                            locationComponent.isLocationComponentEnabled = true

                            // Set the camera mode
                            locationComponent.cameraMode = CameraMode.TRACKING
                        } else {
                            // Request permissions
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                1
                            )
                        }

                        // Set the initial camera position
                        mapLibreMap.cameraPosition = cameraPosition

                        // Convert Drawable to Bitmap with a fixed size
                        val bitmap = drawableToBitmap(context, R.drawable.car, 60, 60)

                        // Create an Icon from the Bitmap
                        val icon = IconFactory.getInstance(context).fromBitmap(bitmap)

                        // Create MarkerOptions
                        val markerOptions = MarkerOptions()
                            .position(LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
                            .icon(icon)

                        // Add the marker to the map
                        mapLibreMap.addMarker(markerOptions)
                    }
                }
            }
        )
        // Menu Button

            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier.constrainAs(menuButton) {
                end.linkTo(switchButton.start)
                start.linkTo(parent.start)
                top.linkTo(parent.top, 16.dp)
            }) {
                iconButton(icon = R.drawable.menu_icon, color = R.color.my_white, R.color.my_black) {}
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Busy / Active Buttons
            Row(
                modifier = Modifier
                    .constrainAs(switchButton) {
                        end.linkTo(scoreButton.start)
                        start.linkTo(menuButton.end)
                        top.linkTo(parent.top, 16.dp)
                    }
                    .background(
                        colorResource(id = R.color.my_white),
                        shape = RoundedCornerShape(14.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Busy Button
                Button(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (!clicked) colorResource(id = R.color.my_white) else colorResource(id = R.color.my_red)),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { clicked = true }
                ) {
                    Text(
                        text = stringResource(id = R.string.busy),
                        fontSize = 13.sp,
                        color = if (clicked) colorResource(id = R.color.my_white) else colorResource(id = R.color.my_black),
                        maxLines = 1,
                        fontWeight = if (clicked) FontWeight.Bold else FontWeight.W400
                    )
                }

                // Active Button
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (!clicked) colorResource(id = R.color.my_green) else colorResource(id = R.color.my_white)),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { clicked = false }
                ) {
                    Text(
                        text = stringResource(id = R.string.active),
                        fontSize = 13.sp,
                        color = if (!clicked) colorResource(id = R.color.my_black) else colorResource(id = R.color.my_black),
                        maxLines = 1,
                        fontWeight = if (clicked) FontWeight.W400 else FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Score Button
            Button(
                modifier = Modifier
                    .constrainAs(scoreButton) {
                        end.linkTo(parent.end)
                        start.linkTo(switchButton.end)
                        top.linkTo(parent.top, 16.dp)
                    }
                    .size(56.dp)
                    .border(
                        width = 4.dp,
                        color = colorResource(id = R.color.my_white),
                        shape = RoundedCornerShape(14.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.my_green)),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {  }
            ) {
                Text(
                    text = "95",
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.my_black),
                    maxLines = 1,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

        if (showBottomSheet1.value == false) {
            // Zoom In Button
            Box(modifier = Modifier.constrainAs(zoomIn) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
                iconButton(icon = R.drawable.plus_icon, color = R.color.my_transparent, iconColor = R.color.my_icon_color) {
                    mapView.getMapAsync { mapLibreMap ->
                        val currentCameraPosition = mapLibreMap.cameraPosition
                        val newCameraPosition = CameraPosition.Builder(currentCameraPosition)
                            .zoom(currentCameraPosition.zoom + 1.0)
                            .build()
                        mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
                    }
                }
            }


            Spacer(modifier = Modifier.size(16.dp))

            // Zoom Out Button
            Box(modifier = Modifier.constrainAs(zoomOut) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(zoomIn.bottom, 16.dp)
            }) {
                iconButton(icon = R.drawable.minus_icon, color = R.color.my_transparent, iconColor = R.color.my_icon_color) {
                    mapView.getMapAsync { mapLibreMap ->
                        val currentCameraPosition = mapLibreMap.cameraPosition
                        val newCameraPosition = CameraPosition.Builder(currentCameraPosition)
                            .zoom(currentCameraPosition.zoom - 1.0)
                            .build()
                        mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
                    }
                }
            }


            Spacer(modifier = Modifier.size(16.dp))

            // Location Button
            Box(modifier = Modifier.constrainAs(locationButton) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(zoomOut.bottom, 16.dp)
            }) {
                iconButton(icon = R.drawable.navigation_icon, color = R.color.my_transparent, iconColor = R.color.my_navigation_color) {

                    if (hasLocationPermission) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                            if (loc != null) {
                                location = LatLng(loc.latitude, loc.longitude)
                                cameraPosition = CameraPosition.Builder()
                                    .target(location)
                                    .zoom(15.0)
                                    .build()

                                // Update the map's camera position
                                mapView.getMapAsync { mapboxMap ->
                                    mapboxMap.cameraPosition = cameraPosition
                                }

                            } else {
                                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Bottom Sheet Button
            IconButton(
                modifier = Modifier
                    .border(
                        width = 4.dp,
                        color = colorResource(id = R.color.my_white),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .constrainAs(bottomSheetButton) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(map.top)
                        bottom.linkTo(map.bottom)
                    }
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        colorResource(id = R.color.my_gray),
                        shape = RoundedCornerShape(14.dp)
                    ),
                colors = IconButtonDefaults.iconButtonColors(contentColor = colorResource(id = R.color.my_icon_color)),
                onClick = { showBottomSheet1.value = true }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.chevrons_icon),
                    contentDescription = "menu"
                )
            }

        } else {
            if (showBottomSheet1.value) {
                showBottomSheet1 = bottomSheet(showBottomSheet = showBottomSheet1)
            }
        }
    }
}




