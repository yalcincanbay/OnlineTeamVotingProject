package com.example.deneme1teamsproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
//import com.example.deneme1teamsproject.ui.theme.TeamSelectorTheme // Eğer burayı kullanmak istiyorsanız, temayı oluşturmalısınız.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Burada varsayılan MaterialTheme'i kullanıyoruz
                Surface(color = MaterialTheme.colorScheme.background) {
                    MyAppNavigation()
                }
            }
        }
    }
}
