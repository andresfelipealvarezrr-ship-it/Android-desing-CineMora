package com.example.cinemora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cinemora.ui.screen.CineMoraApp
import com.example.cinemora.ui.theme.CinemoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CinemoraTheme {
                CineMoraApp()
            }
        }
    }
}
