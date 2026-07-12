package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EduViewModel
import com.example.ui.components.PremiumCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: EduViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val isAuthLoading by viewModel.isAuthLoading.collectAsState()
    val isFirebaseOnline by viewModel.isFirebaseOnline.collectAsState()

    var isLoginMode by remember { mutableStateOf(true) }

    // Form inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("TUTOR") } // "TUTOR" or "ADMIN"

    var passwordVisible by remember { mutableStateOf(false) }

    val darkBg = Brush.verticalGradient(listOf(Color(0xFF090A0C), Color(0xFF111317)))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBg)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // High-fidelity Branding Icon Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(Color(0xFF4F46E5), Color(0xFF312E81)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Sécurité",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = if (isLoginMode) "Connexion Sécurisée" else "Créer un Compte",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isLoginMode) "Accédez à votre espace EduManager" else "Inscrivez-vous en tant que tuteur ou administrateur",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Firebase Status Indicator Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isFirebaseOnline) Color(0xFF065F46) else Color(0xFF78350F))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isFirebaseOnline) Color(0xFF34D399) else Color(0xFFFBBF24))
                    )
                    Text(
                        text = if (isFirebaseOnline) "Base de données Firebase active" else "Mode Sécurisé Local (Hors-ligne)",
                        color = if (isFirebaseOnline) Color(0xFFA7F3D0) else Color(0xFFFDE68A),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form container Card
            PremiumCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    if (authError != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Error, contentDescription = "Erreur", tint = Color(0xFFFCA5A5))
                                Text(
                                    text = authError ?: "",
                                    color = Color(0xFFFEE2E2),
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.clearAuthError() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = Color(0xFFFCA5A5), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Registration Specific Field: Full Name
                    if (!isLoginMode) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nom complet") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_fullname_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF6366F1),
                                unfocusedBorderColor = Color(0xFF2E3035)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Adresse e-mail") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0xFF2E3035)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mot de passe") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0xFF2E3035)
                        )
                    )

                    // Registration Specific: Role Selector
                    if (!isLoginMode) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Sélectionnez votre rôle :",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Tutor Role Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selectedRole == "TUTOR") Color(0xFF1E1B4B) else Color(0xFF16181B))
                                    .border(
                                        1.dp,
                                        if (selectedRole == "TUTOR") Color(0xFF6366F1) else Color(0xFF2E3035),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedRole = "TUTOR" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = if (selectedRole == "TUTOR") Color(0xFF818CF8) else Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Enseignant", color = if (selectedRole == "TUTOR") Color.White else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            // Admin Role Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selectedRole == "ADMIN") Color(0xFF1E1B4B) else Color(0xFF16181B))
                                    .border(
                                        1.dp,
                                        if (selectedRole == "ADMIN") Color(0xFF6366F1) else Color(0xFF2E3035),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedRole = "ADMIN" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = if (selectedRole == "ADMIN") Color(0xFF818CF8) else Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Administrateur", color = if (selectedRole == "ADMIN") Color.White else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (isLoginMode) {
                                viewModel.loginUser(email, password)
                            } else {
                                viewModel.registerUser(email, password, fullName.ifEmpty { email.substringBefore("@") }, selectedRole)
                            }
                        },
                        enabled = !isAuthLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isAuthLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = if (isLoginMode) "Se Connecter" else "S'inscrire",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Switch Mode Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLoginMode) "Pas encore de compte ? " else "Vous avez déjà un compte ? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = if (isLoginMode) "Créer un compte" else "Se connecter",
                    color = Color(0xFF818CF8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            isLoginMode = !isLoginMode
                            viewModel.clearAuthError()
                        }
                        .testTag("auth_mode_toggle")
                )
            }
        }
    }
}
