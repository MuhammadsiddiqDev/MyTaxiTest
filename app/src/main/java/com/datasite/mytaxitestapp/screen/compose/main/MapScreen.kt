package com.datasite.mytaxitestapp.screen.compose.main

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.datasite.mytaxitestapp.R
import com.datasite.mytaxitestapp.core.room.AppDatabase
import com.datasite.mytaxitestapp.core.room.UserLocation
import com.datasite.mytaxitestapp.core.requesLocation.requestLocation
import com.datasite.mytaxitestapp.screen.bitmap.drawableToBitmap
import com.datasite.mytaxitestapp.screen.compose.bottomSheet.bottomSheet
import com.datasite.mytaxitestapp.screen.compose.myButton.iconButton
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.DefaultSettingsProvider
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.GenericStyle
import kotlinx.coroutines.launch

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen() {


    /*


   Oilaviy ishlar bilan band bo'lib code yozishni Payshanba
   kuni abeddan kegin boshladim va Juma kuni kechgacha
   yozdim, lekin vaqt qo'shib berilganini Yakshanba bilibman,
   shu sabbdan bugun ham kechga yozdim.
   Lekin qo'limdan kelganicha va ulgurganimcha yozdim...
   MVI ga vaqtim yetmadi va unchalik zo'r ham bilmayman,
   shu sababdan qo'limdan kelganicha yozdim.
   Bularni baxona o'rnida yozmadim shunchaki tushuntirish xolos



*/

    val context = LocalContext.current

    var clicked by remember { mutableStateOf(false) }
    var showBottomSheet1 = remember { mutableStateOf(false) }

    var zoomLevel by remember { mutableStateOf(14.0) }

    val icon = drawableToBitmap(context,R.drawable.car,32,32)

    var mapViewportState = rememberMapViewportState(
        init = {
            setCameraOptions {
                zoom(zoomLevel)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(0.0)
                bearing(0.0)
            }
        })


    val database = remember { AppDatabase.getDatabase(context) }
    val userLocationDao = database.userLocationDao()

    val locations by userLocationDao.getAllLocations().collectAsState(initial = emptyList())

    val insertLocationScope = rememberCoroutineScope()
    val exampleLocation = UserLocation(latitude = 37.7749, longitude = -122.4194)

    LaunchedEffect(Unit) {
        insertLocationScope.launch {
            userLocationDao.insertLocation(exampleLocation)
        }
    }

    LocationList(locations)

    val coroutineScope = rememberCoroutineScope()

    val locationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

        if (it.any{permision -> permision.value}){

            requestLocation {location ->
                location?.let {
                    mapViewportState.flyTo(
                        cameraOptions = CameraOptions.Builder().
                        apply {
                            center(Point.fromLngLat(it.longitude,it.latitude))
                                .zoom(15.0)


                        }.build()
                    )
                }
            }

        }else{
            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit){
        if(PermissionsManager.areLocationPermissionsGranted(context)){

            requestLocation {location ->
                location?.let {
                    mapViewportState.flyTo(
                        cameraOptions = CameraOptions.Builder().
                        apply {
                            center(Point.fromLngLat(it.longitude,it.latitude))
                                .zoom(15.0)

                        }.build()
                    )
                }
            }



        }else{
            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
            locationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }


    val locationComponentSettings = DefaultSettingsProvider
        .defaultLocationComponentSettings(LocalDensity.current.density)
        .toBuilder().apply{
    enabled = true
    puckBearingEnabled = true
        }.build()

    val isDarkTheme = isSystemInDarkTheme()

    ConstraintLayout {
        val (map, menuButton, scoreButton, zoomIn, zoomOut, locationButton, bottomSheetButton, switchButton) = createRefs()

        MapboxMap(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(map) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },

            locationComponentSettings = locationComponentSettings,

            style = {
                if (isDarkTheme){GenericStyle(style = Style.DARK)}

                else{ GenericStyle(style = Style.STANDARD) }
            },
            mapViewportState = mapViewportState,

        )
        Spacer(modifier = Modifier.size(16.dp))

        Box(modifier = Modifier.constrainAs(menuButton) {
            end.linkTo(switchButton.start)
            start.linkTo(parent.start)
            top.linkTo(parent.top, 16.dp)
        }) {
            iconButton(icon = R.drawable.menu_icon, color = MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.onPrimary) {}
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
                    MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(14.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Busy Button
            Button(
                modifier = Modifier
                    .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (!clicked) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.inversePrimary),
                shape = RoundedCornerShape(10.dp),
                onClick = { clicked = true }
            ) {
                Text(
                    text = stringResource(id = R.string.busy),
                    fontSize = 13.sp,
                    color = if (clicked) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    fontWeight = if (clicked) FontWeight.Bold else FontWeight.W400,
                            fontFamily = FontFamily(
                            Font(R.font.lato_regular)
                            )
                )
            }

            // Active Button
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .padding(end = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (!clicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary),
                shape = RoundedCornerShape(10.dp),
                onClick = { clicked = false }
            ) {
                Text(
                    text = stringResource(id = R.string.active),
                    fontSize = 13.sp,
                    color = if (!clicked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    fontWeight = if (clicked) FontWeight.W400 else FontWeight.Bold,
                    fontFamily = FontFamily(
                        Font(R.font.lato_regular)
                    )
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
                    color = MaterialTheme.colorScheme.onSecondary,
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
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.lato_regular)
                )
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
                iconButton(icon = R.drawable.plus_icon, color = MaterialTheme.colorScheme.errorContainer, iconColor = MaterialTheme.colorScheme.tertiary) {

                    zoomLevel = (zoomLevel + 1).coerceAtMost(20.0)  // Max zoom level
                    mapViewportState.setCameraOptions {
                        zoom(zoomLevel)
                    }
                }
            }


            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier.constrainAs(zoomOut) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(zoomIn.bottom, 16.dp)
            }) {
                iconButton(icon = R.drawable.minus_icon, color = MaterialTheme.colorScheme.errorContainer, iconColor = MaterialTheme.colorScheme.tertiary) {

                    zoomLevel = (zoomLevel - 1).coerceAtLeast(0.0)  // Min zoom level
                    mapViewportState.setCameraOptions {
                        zoom(zoomLevel)
                    }

                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            // Location Button
            Box(modifier = Modifier.constrainAs(locationButton) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(zoomOut.bottom, 16.dp)
            }) {

                iconButton(icon = R.drawable.navigation_icon,  color = MaterialTheme.colorScheme.errorContainer, iconColor = MaterialTheme.colorScheme.secondary) {

                        if(PermissionsManager.areLocationPermissionsGranted(context)){

                            requestLocation {location ->
                                location?.let { loc->
                                    mapViewportState.flyTo(
                                        cameraOptions = CameraOptions.Builder().
                                        apply {
                                            center(Point.fromLngLat(loc.longitude,loc.latitude))
                                                .zoom(15.0)
                                        }.build()

                                    )
                                    coroutineScope.launch {
                                        userLocationDao.insertLocation(
                                            UserLocation(
                                                latitude = loc.latitude,
                                                longitude = loc.longitude
                                            )
                                        )
                                    }
                                }
                            }

                        }else{
                            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                            locationLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            )
                        }
                }
            }

            IconButton(
                modifier = Modifier
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onSecondary,
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
                        MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(14.dp)
                    ),
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
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

@Composable
fun LocationList(locations: List<UserLocation>) {
    Column {
        for (location in locations) {
            Text("Latitude: ${location.latitude}, Longitude: ${location.longitude}")

//            Log.d("Room","Latitude: ${location.latitude}, Longitude: ${location.longitude}")
        }
    }
}



