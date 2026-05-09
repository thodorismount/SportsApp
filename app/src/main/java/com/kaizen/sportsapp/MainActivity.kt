package com.kaizen.sportsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kaizen.sportsapp.presentation.ui.screens.SportsScreen
import com.kaizen.sportsapp.presentation.ui.theme.SportsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsAppTheme {
                SportsScreen()
            }
        }
    }
}
