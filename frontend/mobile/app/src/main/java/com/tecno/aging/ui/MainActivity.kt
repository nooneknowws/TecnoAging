package com.tecno.aging.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecno.aging.ui.navigation.AppNavGraph
import com.tecno.aging.ui.theme.TecnoAgingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecnoAgingTheme {
                AppNavGraph();
            }
        }
    }
  }



