package com.example.hospitalmanagementsystemsam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hospitalmanagementsystemsam.ui.theme.screens.dashboard.DashboardScreen
import com.example.hospitalmanagementsystemsam.ui.theme.screens.login.LoginScreen
import com.example.hospitalmanagementsystemsam.ui.theme.screens.patient.AddPatientScreen
import com.example.hospitalmanagementsystemsam.ui.theme.screens.patient.PatientListScreen
import com.example.hospitalmanagementsystemsam.ui.theme.screens.patient.UpdatePatientScreen
import com.example.hospitalmanagementsystemsam.ui.theme.screens.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),
               startDestination: String = ROUTE_REGISTER){
    NavHost(navController = navController, startDestination = startDestination){
        composable(ROUTE_REGISTER) {RegisterScreen(navController)}
        composable(ROUTE_LOGIN){LoginScreen(navController)}
        composable(ROUTE_DASHBOARD){ DashboardScreen(navController)}
        composable(ROUTE_ADD_PATIENT){ AddPatientScreen(navController) }
        composable(ROUTE_PATIENT_LIST){ PatientListScreen(navController) }
       // composable(ROUTE_SPLASH){ SplashScreen(navController) }
        composable(ROUTE_UPDATE_PATIENT){ UpdatePatientScreen(navController, patientId = String())}

    }

}
