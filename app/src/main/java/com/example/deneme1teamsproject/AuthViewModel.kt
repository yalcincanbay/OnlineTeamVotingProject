package com.example.deneme1teamsproject

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun getDatabaseReference() = database

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                    onSuccess()
                } else {
                    _authState.value = AuthState.Failure
                    onFailure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                    onSuccess()
                } else {
                    _authState.value = AuthState.Failure
                    onFailure(task.exception ?: Exception("Authentication failed"))
                }
            }
    }

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun selectTeam(userId: String, newTeam: String) {
        // Kullanıcının mevcut takımını alıp taraftar sayısını azaltıyoruz
        val currentTeamRef = database.child("users").child(userId).child("team")
        currentTeamRef.get().addOnSuccessListener { snapshot ->
            val currentTeam = snapshot.getValue(String::class.java)
            currentTeam?.let {
                // Eski takımın taraftar sayısını azaltıyoruz
                updateFanCount(it, delta = -1)
            }
            // Yeni takımı kaydediyoruz
            currentTeamRef.setValue(newTeam)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Yeni takımın taraftar sayısını artırıyoruz
                        updateFanCount(newTeam, delta = 1)
                    } else {
                        // Hata durumunu ele alabilirsiniz
                    }
                }
        }
    }

    // Kullanıcının takımını kontrol etme
    fun checkUserTeam(userId: String, onSuccess: (Boolean) -> Unit) {
        database.child("users").child(userId).child("team").get()
            .addOnSuccessListener { snapshot ->
                val team = snapshot.getValue(String::class.java)
                onSuccess(team != null)  // Eğer takım varsa true, yoksa false döner
            }
            .addOnFailureListener {
                onSuccess(false)  // Hata durumunda false döner
            }
    }

    // Takımın taraftar sayısını almak için
    fun getFansCount(team: String, onSuccess: (Int) -> Unit) {
        database.child("teams").child(team).child("fans").get()
            .addOnSuccessListener { snapshot ->
                val fansCount = snapshot.getValue(Int::class.java) ?: 0
                onSuccess(fansCount)
            }
            .addOnFailureListener {
                onSuccess(0)
            }
    }

    // Taraftar sayısını güncelleme fonksiyonu
    fun updateFanCount(team: String, delta: Int) {
        val teamRef = database.child("teams").child(team).child("fans")
        teamRef.get().addOnSuccessListener { snapshot ->
            val currentFans = snapshot.getValue(Int::class.java) ?: 0
            val newCount = currentFans + delta
            teamRef.setValue(newCount)
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object Failure : AuthState()
}
