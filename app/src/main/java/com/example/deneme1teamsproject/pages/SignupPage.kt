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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.deneme1teamsproject.AuthViewModel
import com.example.deneme1teamsproject.AuthState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignupPage(navController: NavController) {
    val authViewModel = AuthViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),  // Düz beyaz arka plan
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Başlık
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it.lowercase() },
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
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),  // Yumuşak köşeler
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        // Şifre onay TextField
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),  // Yumuşak köşeler
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        // Şifre uyumsuzluğu uyarısı
        if (password != confirmPassword) {
            Text(
                text = "Passwords do not match",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Butonu
        Button(
            onClick = {
                if (password == confirmPassword) {
                    authViewModel.signUp(
                        email,
                        password,
                        onSuccess = {
                            navController.navigate("login")
                            Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            Toast.makeText(context, "Signup failed: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = authState != AuthState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)), // Mavi buton rengi
            shape = RoundedCornerShape(12.dp),  // Yumuşak köşeler
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign Up",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zaten hesabı olanlar için giriş bağlantısı
        TextButton(onClick = { navController.navigate("login") }) {
            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
