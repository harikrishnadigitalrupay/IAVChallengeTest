package com.example.iavtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iavtest.data.repository.GenerateStringRepository
import com.example.iavtest.screens.RandomStringScreen
import com.example.iavtest.viewmodel.GenerateStringViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = GenerateStringRepository(contentResolver)
        val viewModel = GenerateStringViewModel(repository)

        setContent {
            val navController = rememberNavController()  // Move this inside setContent
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    RandomStringScreen(viewModel, navController)
                }
            }

        }
    }
}

