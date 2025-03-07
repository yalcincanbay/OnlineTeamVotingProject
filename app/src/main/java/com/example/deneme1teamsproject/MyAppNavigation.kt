package com.example.deneme1teamsproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deneme1teamsproject.pages.HomePage
import com.example.deneme1teamsproject.pages.LoginPage
import com.example.deneme1teamsproject.pages.SignupPage
import com.example.deneme1teamsproject.pages.ChoosingTeamPage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyAppNavigation() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val navController = rememberNavController()

    // Firebase oturum kontrolü burada yapılmalı
    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginPage(navController) }
        composable("signup") { SignupPage(navController) }
        composable("choosing_team") { ChoosingTeamPage(navController) }
        composable("home") { HomePage(navController) }
    }
}
