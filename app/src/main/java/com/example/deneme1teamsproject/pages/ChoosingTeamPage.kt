package com.example.deneme1teamsproject.pages

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deneme1teamsproject.AuthViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Composable
fun ChoosingTeamPage(navController: NavController) {
    val authViewModel = AuthViewModel()
    val userId = authViewModel.getUserId()

    // Takımlar listesi
    val teams = List(10) { "Team ${it + 1}" }
    var selectedTeam by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Daha geniş padding, sade görünüm için
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Başlık
        Text(
            "Choose a Team",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Seçilen takım
        selectedTeam?.let {
            Text(
                text = "Selected Team: $it",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }

        // Takımların listelendiği LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Daha geniş boşluklar
        ) {
            items(teams.size) { index ->
                val team = teams[index]
                TeamItem(team, selectedTeam == team) {
                    selectedTeam = team
                    userId?.let {
                        authViewModel.selectTeam(it, team) // Takımı seçerken fan sayısını güncelle
                        navController.navigate("home") // Anasayfaya dön
                    } ?: run {
                        Log.e("ChoosingTeamPage", "userId is null")
                    }
                }
            }
        }
    }
}

@Composable
fun TeamItem(team: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevation(defaultElevation = 8.dp),  // Daha belirgin elevation
        shape = MaterialTheme.shapes.large, // Daha yuvarlak köşeler
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp) // Daha geniş iç padding
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Seçili takımlar için ikon
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun CardDefaults.elevation(defaultElevation: Dp): CardElevation {
    return cardElevation(defaultElevation = defaultElevation)
}
