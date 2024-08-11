package com.datasite.mytaxitestapp.screen.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.datasite.mytaxitestapp.R
import com.datasite.mytaxitestapp.screen.compose.myTheme.MyApp
import com.datasite.mytaxitestapp.core.viewModel.MapViewModel
import com.datasite.mytaxitestapp.screen.compose.main.MapScreen
import com.mapbox.common.MapboxOptions

class MainActivity : ComponentActivity() {

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

    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapboxOptions.accessToken = R.string.key.toString()

        setContent {
            MyApp {
//                val state = viewModel.state.collectAsState().value

                MapScreen()
            }
        }
    }

}