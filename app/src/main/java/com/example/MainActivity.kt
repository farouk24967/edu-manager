package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.EduViewModel
import com.example.ui.screens.MainAppScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: EduViewModel = viewModel()
        val onboardingStep by viewModel.onboardingStep.collectAsState()

        if (onboardingStep < 4) {
          WelcomeScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        } else {
          MainAppScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
      }
    }
  }
}
