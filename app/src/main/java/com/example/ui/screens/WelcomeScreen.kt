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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EduViewModel
import com.example.ui.components.PremiumCard

@Composable
fun WelcomeScreen(
    viewModel: EduViewModel,
    modifier: Modifier = Modifier
) {
    val userType by viewModel.userType.collectAsState()
    val onboardingStep by viewModel.onboardingStep.collectAsState()
    val teachingType by viewModel.teachingType.collectAsState()
    val selectedLevel by viewModel.selectedLevel.collectAsState()
    val selectedTracks by viewModel.selectedTracks.collectAsState()

    val darkBg = Brush.verticalGradient(listOf(Color(0xFF090A0C), Color(0xFF111317)))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBg)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = onboardingStep,
            transitionSpec = {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            },
            label = "OnboardingStep"
        ) { step ->
            when (step) {
                0 -> {
                    // --- SELECT ESTABLISHMENT TYPE ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Bienvenue sur EduManager AI",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-1).sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Choisissez le type de votre établissement afin de personnaliser votre espace.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        // Card 1: Private Tutor
                        OnboardingOptionCard(
                            title = "👨‍🏫 Professeur particulier",
                            description = "Je donne des cours de soutien scolaire en individuel ou petits groupes.",
                            onClick = { viewModel.selectUserType("TUTOR") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card 2: Academy
                        OnboardingOptionCard(
                            title = "🏫 Académie de soutien",
                            description = "Je gère une académie avec plusieurs professeurs, plusieurs salles et groupes d'élèves.",
                            onClick = { viewModel.selectUserType("ACADEMY") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card 3: School
                        OnboardingOptionCard(
                            title = "🎓 École privée",
                            description = "Je dirige un centre de formation, une école privée ou un ERP scolaire complet.",
                            onClick = { viewModel.selectUserType("SCHOOL") }
                        )
                    }
                }

                1 -> {
                    // --- TUTOR ONBOARDING: TEACHING TYPE ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Quel type d'enseignement proposez-vous ?",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        OnboardingOptionCard(
                            title = "🌍 Tous les niveaux",
                            description = "J'enseigne à des élèves de différents âges et niveaux d'études.",
                            onClick = { viewModel.setTeachingType("ALL") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OnboardingOptionCard(
                            title = "🎯 Un seul niveau spécifique",
                            description = "Je me concentre sur un pallier scolaire spécifique (Primaire, Collège ou Lycée).",
                            onClick = { viewModel.setTeachingType("SINGLE") }
                        )
                    }
                }

                2 -> {
                    // --- TUTOR ONBOARDING: SINGLE LEVEL SELECT ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Sélectionnez votre niveau d'intervention",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        OnboardingOptionCard(
                            title = "🎒 Primaire",
                            description = "Classes de 1AP à 5AP (Éducation de base).",
                            onClick = { viewModel.setSelectedLevel("PRIMAIRE") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OnboardingOptionCard(
                            title = "📚 CEM (Moyen)",
                            description = "Classes de 1AM à 4AM (Préparation au BEM).",
                            onClick = { viewModel.setSelectedLevel("CEM") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OnboardingOptionCard(
                            title = "🎓 Lycée (Secondaire)",
                            description = "Classes de 1AS à 3AS (Préparation au BAC).",
                            onClick = { viewModel.setSelectedLevel("LYCEE") }
                        )
                    }
                }

                3 -> {
                    // --- TUTOR ONBOARDING: HIGH SCHOOL TRACKS SELECT ---
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Filières enseignées au Lycée",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sélectionnez une ou plusieurs filières pour filtrer vos futurs groupes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        val tracks = listOf(
                            "Sciences expérimentales",
                            "Mathématiques",
                            "Techniques Mathématiques",
                            "Gestion Économie",
                            "Lettres Philosophie",
                            "Langues étrangères"
                        )

                        tracks.forEach { track ->
                            val isSelected = selectedTracks.contains(track)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF1E1B4B) else Color(0xFF16181B))
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFF6366F1) else Color(0xFF2E3035),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.toggleTrack(track) }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.toggleTrack(track) },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6366F1))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = track, color = Color.White, fontSize = 15.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.finishTutorOnboarding() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("finish_onboarding_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Terminer la configuration", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingOptionCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9CA3AF)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Suivant",
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
