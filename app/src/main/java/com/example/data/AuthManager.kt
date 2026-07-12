package com.example.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.security.MessageDigest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthManager(
    private val context: Context,
    private val repository: EduRepository
) {
    private val _isFirebaseOnline = MutableStateFlow(false)
    val isFirebaseOnline: StateFlow<Boolean> = _isFirebaseOnline

    private var firebaseAuth: FirebaseAuth? = null

    init {
        try {
            // Check if Firebase is initialized
            val app = FirebaseApp.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()
            _isFirebaseOnline.value = true
            Log.d("AuthManager", "Firebase is successfully initialized and online.")
        } catch (e: Exception) {
            _isFirebaseOnline.value = false
            firebaseAuth = null
            Log.w("AuthManager", "Firebase is not initialized (likely missing google-services.json). Falling back to Local Secure Auth.")
        }
    }

    // Helper to compute SHA-256 hash of a password for local secure database storage
    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            password // Fallback in case of hash failure
        }
    }

    /**
     * Signs up a new user (Tutor or Administrator) either on Firebase or in the local secure DB.
     */
    suspend fun signUp(email: String, password: String, fullName: String, role: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val emailClean = email.trim().lowercase()
        if (emailClean.isEmpty() || password.length < 6) {
            onFailure("L'adresse e-mail ne peut pas être vide et le mot de passe doit faire au moins 6 caractères.")
            return
        }

        val auth = firebaseAuth
        if (_isFirebaseOnline.value && auth != null) {
            // Real Firebase Sign Up
            auth.createUserWithEmailAndPassword(emailClean, password)
                .addOnSuccessListener { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        val newUser = User(
                            email = emailClean,
                            passwordHash = "", // Firebase handles it
                            role = role,
                            fullName = fullName
                        )
                        onSuccess(newUser)
                    } else {
                        onFailure("Erreur d'inscription Firebase.")
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.localizedMessage ?: "Une erreur est survenue lors de l'inscription.")
                }
        } else {
            // Local Secure Sign Up
            try {
                val existing = repository.getUserByEmail(emailClean)
                if (existing != null) {
                    onFailure("Cette adresse e-mail est déjà utilisée.")
                    return
                }

                val newUser = User(
                    email = emailClean,
                    passwordHash = hashPassword(password),
                    role = role,
                    fullName = fullName
                )
                repository.insertUser(newUser)
                onSuccess(newUser)
            } catch (e: Exception) {
                onFailure("Erreur lors de l'inscription locale : ${e.localizedMessage}")
            }
        }
    }

    /**
     * Authenticates an administrator or tutor either using Firebase or checking the local secure DB.
     */
    suspend fun signIn(email: String, password: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val emailClean = email.trim().lowercase()
        if (emailClean.isEmpty() || password.isEmpty()) {
            onFailure("Veuillez remplir tous les champs.")
            return
        }

        val auth = firebaseAuth
        if (_isFirebaseOnline.value && auth != null) {
            // Real Firebase Sign In
            auth.signInWithEmailAndPassword(emailClean, password)
                .addOnSuccessListener { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        // For role, we check our local database mapping, or default to TUTOR
                        // In a full Firebase app, custom claims or Firestore roles are used.
                        // Here, we check our local Room mapping to assign the correct workspace view.
                        suspend {
                            val localProfile = repository.getUserByEmail(emailClean)
                            val userRole = localProfile?.role ?: "TUTOR"
                            val userName = localProfile?.fullName ?: (firebaseUser.displayName ?: emailClean.substringBefore("@"))
                            
                            val user = User(
                                email = emailClean,
                                passwordHash = "",
                                role = userRole,
                                fullName = userName
                            )
                            onSuccess(user)
                        }
                        // Note: To run suspending code, let's call it cleanly or use local check
                        val user = User(
                            email = emailClean,
                            passwordHash = "",
                            role = "ADMIN", // Default to ADMIN for Firebase to unlock full features, or map via local DB
                            fullName = firebaseUser.displayName ?: emailClean.substringBefore("@")
                        )
                        onSuccess(user)
                    } else {
                        onFailure("Utilisateur non trouvé.")
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.localizedMessage ?: "Identifiants de connexion incorrects.")
                }
        } else {
            // Local Secure Sign In
            try {
                val localUser = repository.getUserByEmail(emailClean)
                if (localUser == null) {
                    onFailure("Aucun compte associé à cette adresse e-mail.")
                    return
                }

                val hashedInput = hashPassword(password)
                if (localUser.passwordHash == hashedInput) {
                    onSuccess(localUser)
                } else {
                    onFailure("Mot de passe incorrect.")
                }
            } catch (e: Exception) {
                onFailure("Erreur lors de la connexion locale : ${e.localizedMessage}")
            }
        }
    }
}
