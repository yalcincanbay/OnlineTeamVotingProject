package com.example.deneme1teamsproject.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deneme1teamsproject.AuthViewModel

@Composable
fun HomePage(navController: NavController) {
    val authViewModel = AuthViewModel()
    val userId = authViewModel.getUserId()
    val selectedTeam = remember { mutableStateOf("None") }
    val availableTeams = remember { mutableStateOf<List<String>>(emptyList()) }
    val fansCount = remember { mutableStateOf(mapOf<String, Int>()) }

    // Firebase'den verileri çekme
    LaunchedEffect(userId) {
        userId?.let {
            authViewModel.getDatabaseReference().child("users").child(it).child("team")
                .get().addOnSuccessListener { snapshot ->
                    selectedTeam.value = snapshot.value as? String ?: "None"
                }
            authViewModel.getDatabaseReference().child("teams").get().addOnSuccessListener { snapshot ->
                val teams = snapshot.children.mapNotNull { it.key }
                availableTeams.value = teams
            }
        }
    }

    LaunchedEffect(availableTeams.value) {
        availableTeams.value.forEach { team ->
            authViewModel.getFansCount(team) { count ->
                fansCount.value = fansCount.value + (team to count)
            }
        }
    }

    val maxFansCount = fansCount.value.values.maxOrNull() ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Daha hafif bir gri arka plan
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logout butonu
        IconButton(
            onClick = {
                authViewModel.firebaseAuth.signOut()
                navController.navigate("login")
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                tint = Color.Gray // Apple kırmızı tonu
            )
        }

        // Başlık
        Text(
            text = "Which Team Do You Support?",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFF007AFF), // Apple mavi tonu
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Seçilen Takım
        Text(
            text = "Selected Team: ${selectedTeam.value}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF1C1C1E)
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Takım Değiştirme Butonu
        Button(
            onClick = { navController.navigate("choosing_team") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C759)) // Apple yeşil tonu
        ) {
            Text(
                text = "Change Team",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Takımların Taraftar Sayıları
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val sortedTeams = fansCount.value.toList()
                .sortedByDescending { it.second }
                .map { it.first }

            items(sortedTeams) { team ->
                val fanCount = fansCount.value[team] ?: 0
                val barWidth = if (maxFansCount > 0) (fanCount.toFloat() / maxFansCount.toFloat()) * 0.8f else 0f

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Takım adı
                    Text(
                        text = team,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF1C1C1E) // Apple siyah tonu
                        )
                    )

                    // Taraftar sayısı
                    Text(
                        text = "$fanCount Fans",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8E8E93))
                    )

                    // Çubuk
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(200.dp * barWidth)
                            .background(Color(0xFF007AFF), RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}
