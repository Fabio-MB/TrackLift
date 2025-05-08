package com.example.tracklift_asa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tracklift_asa.ui.AppNavGraph
import com.example.tracklift_asa.ui.theme.TrackliftasaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackliftasaTheme {
                AppNavGraph()
            }
        }
    }
}