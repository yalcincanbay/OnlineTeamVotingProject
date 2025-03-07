package com.example.deneme1teamsproject.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deneme1teamsproject.AuthViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginPage(navController: NavController) {
    val authViewModel = AuthViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),  // Düz beyaz arka plan
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Başlık metni
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),  // Yumuşak köşeler
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF2F2F2),  // Pastel gri arka plan
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        // Şifre TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(8.dp),  // Yumuşak köşeler
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        // Giriş düğmesi
        Button(
            onClick = {
                authViewModel.signIn(
                    email,
                    password,
                    onSuccess = {
                        authViewModel.getUserId()?.let { userId ->
                            authViewModel.checkUserTeam(userId) { hasTeam ->
                                if (hasTeam) {
                                    navController.navigate("home")
                                } else {
                                    navController.navigate("choosing_team")
                                }
                            }
                        }
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(context, "Login failed: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),  // Yumuşak köşeler
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))  // Apple mavi rengi
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        // Kayıt olma bağlantısı
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text(
                text = "Don't have an account? Sign up",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
