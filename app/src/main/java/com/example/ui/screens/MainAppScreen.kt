package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.EduViewModel
import com.example.ui.components.*

@Composable
fun MainAppScreen(
    viewModel: EduViewModel,
    modifier: Modifier = Modifier
) {
    val userType by viewModel.userType.collectAsState()
    val students by viewModel.students.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val teachers by viewModel.teachers.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val messages by viewModel.messages.collectAsState()

    var activeTab by remember { mutableStateOf("Dashboard") }
    val gradientBg = Brush.verticalGradient(listOf(Color(0xFF090A0C), Color(0xFF111317)))

    // Navigation Tabs definition
    val navigationItems = listOf(
        NavigationTabItem("Dashboard", Icons.Default.GridView),
        NavigationTabItem("Élèves", Icons.Default.People),
        NavigationTabItem("Groupes", Icons.Default.Layers),
        NavigationTabItem("Planning", Icons.Default.CalendarToday),
        NavigationTabItem("Présences", Icons.Default.CheckCircle),
        NavigationTabItem("Paiements", Icons.Default.MonetizationOn),
        NavigationTabItem("Examens", Icons.Default.Assignment),
        NavigationTabItem("Documents", Icons.Default.FolderOpen),
        NavigationTabItem("Messages", Icons.AutoMirrored.Default.Message),
        NavigationTabItem("Assistant IA", Icons.Default.AutoAwesome),
        NavigationTabItem("Paramètres", Icons.Default.Settings)
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBg)
    ) {
        val isTablet = maxWidth > 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // --- RESPONSIVE SIDEBAR FOR TABLETS (Direct Child of Row) ---
            if (isTablet) {
                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF0F1012))
                        .padding(16.dp)
                ) {
                    // Header Brand
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6366F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Logo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "EduManager",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "SaaS AI Portal",
                                color = Color(0xFF6366F1),
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Selected Persona badge
                    val badgeLabel = when (userType) {
                        "TUTOR" -> "Prof. Particulier"
                        "ACADEMY" -> "Académie Soutien"
                        else -> "École Privée ERP"
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1F1C2C))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = badgeLabel,
                            color = Color(0xFFA5B4FC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Menu items
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(navigationItems) { item ->
                            val isSelected = activeTab == item.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF1E2125) else Color.Transparent)
                                    .clickable { activeTab = item.name }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    tint = if (isSelected) Color(0xFF6366F1) else Color(0xFF9CA3AF),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item.name,
                                    color = if (isSelected) Color.White else Color(0xFF9CA3AF),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Vertical Divider between sidebar and main content
                VerticalDivider(color = Color(0xFF1F2226), modifier = Modifier.width(1.dp).fillMaxHeight())
            }

            // --- MAIN CONTENT COLUMN (Direct Child of Row, weight(1f) fully supported) ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Top Header bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F1012))
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = activeTab,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Gestion scolaire intelligente et intuitive",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }

                    // Language and profile trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "FR 🇩🇿",
                            color = Color(0xFF9CA3AF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1F2226))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6366F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF1F2226), modifier = Modifier.height(1.dp).fillMaxWidth())

                // Sub-pane rendering based on active tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    when (activeTab) {
                        "Dashboard" -> DashboardPane(viewModel, userType, students, groups, payments, attendance)
                        "Élèves" -> StudentsPane(viewModel, students, groups)
                        "Groupes" -> GroupsPane(viewModel, groups, teachers, userType)
                        "Planning" -> PlanningPane(viewModel, groups)
                        "Présences" -> AttendancePane(viewModel, students, groups, attendance)
                        "Paiements" -> PaymentsPane(viewModel, students, groups, payments)
                        "Examens" -> ExamsPane(viewModel, students, groups, exams)
                        "Documents" -> DocumentsPane(viewModel, groups, documents)
                        "Messages" -> MessagesPane(viewModel, students, messages)
                        "Assistant IA" -> AssistantAiPane(viewModel)
                        "Paramètres" -> SettingsPane(viewModel, userType)
                    }
                }

                // --- MOBILE NAVIGATION FOOTER (Only shown on small screens) ---
                if (!isTablet) {
                    NavigationBar(
                        containerColor = Color(0xFF0F1012),
                        tonalElevation = 8.dp,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        val mobileTabs = listOf(
                            NavigationTabItem("Dashboard", Icons.Default.GridView),
                            NavigationTabItem("Élèves", Icons.Default.People),
                            NavigationTabItem("Planning", Icons.Default.CalendarToday),
                            NavigationTabItem("Présences", Icons.Default.CheckCircle),
                            NavigationTabItem("IA", Icons.Default.AutoAwesome)
                        )
                        mobileTabs.forEach { tab ->
                            val isSelected = activeTab == tab.name || (tab.name == "IA" && activeTab == "Assistant IA")
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    activeTab = if (tab.name == "IA") "Assistant IA" else tab.name
                                },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.name,
                                        tint = if (isSelected) Color(0xFF6366F1) else Color(0xFF9CA3AF)
                                    )
                                },
                                label = { Text(tab.name, fontSize = 10.sp, color = if (isSelected) Color.White else Color(0xFF9CA3AF)) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFF1E2125)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NavigationTabItem(val name: String, val icon: ImageVector)

// ==========================================
// PANELS IMPLEMENTATIONS
// ==========================================

// --- 1. DASHBOARD PANE ---
@Composable
fun DashboardPane(
    viewModel: EduViewModel,
    userType: String?,
    students: List<Student>,
    groups: List<SchoolGroup>,
    payments: List<Payment>,
    attendance: List<Attendance>
) {
    val receivedAmount = payments.filter { it.status == "PAID" }.sumOf { it.amount }
    val pendingAmount = payments.filter { it.status == "PENDING" }.sumOf { it.amount }
    val totalRevenue = receivedAmount + pendingAmount

    val presentCount = attendance.count { it.status == "PRESENT" }
    val totalAttendance = attendance.size
    val attendanceRate = if (totalAttendance == 0) 0.85f else (presentCount.toFloat() / totalAttendance)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Welcoming card
        PremiumCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundGradient = listOf(Color(0xFF1E1B4B), Color(0xFF0F1012)),
            borderGradient = listOf(Color(0xFF6366F1), Color(0xFF111317))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Statistiques Générales AI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Toutes vos données financières, inscriptions et assiduités scolaires synchronisées en temps réel.",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI",
                    tint = Color(0xFF818CF8),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dynamic ERP Metrics based on user type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                title = "Total Élèves",
                value = "${students.size}",
                changeText = "+12%",
                icon = Icons.Default.People,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Revenus (DZD)",
                value = "${receivedAmount.toInt()} DZD",
                changeText = "+24%",
                icon = Icons.Default.MonetizationOn,
                iconColor = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                title = "Groupes Actifs",
                value = "${groups.size}",
                changeText = "+4%",
                icon = Icons.Default.Layers,
                iconColor = Color(0xFFFBBF24),
                modifier = Modifier.weight(1f)
            )
            val fourthMetricTitle = if (userType == "SCHOOL") "Formations" else "Présences"
            val fourthMetricValue = if (userType == "SCHOOL") "${groups.count { it.isFormation }}" else "${(attendanceRate * 100).toInt()}%"
            MetricCard(
                title = fourthMetricTitle,
                value = fourthMetricValue,
                changeText = "+2%",
                icon = if (userType == "SCHOOL") Icons.Default.MenuBook else Icons.Default.CheckCircle,
                iconColor = Color(0xFFEC4899),
                modifier = Modifier.weight(1f)
            )
        }

        // Additional Private School ERP widget
        if (userType == "SCHOOL") {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Services ERP École & Cantine",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple("Transport", "2 Bus / 24 Élèves", Icons.Default.DirectionsBus),
                    Triple("Cantine", "Menu: Couscous", Icons.Default.Restaurant),
                    Triple("Bibliothèque", "12 Livres prêtés", Icons.Default.Book)
                ).forEach { item ->
                    PremiumCard(modifier = Modifier.weight(1f)) {
                        Icon(item.third, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(item.second, color = Color(0xFF9CA3AF), fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // High fidelity Charts
        Text(
            text = "Analyses de l'Établissement",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Évolution des revenus mensuels (DZD)",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            RevenueLineChart(
                dataPoints = listOf(12000f, 15000f, 18500f, 22000f, 31000f, receivedAmount.toFloat()),
                labels = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Inscriptions Mensuelles",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            RegistrationsBarChart(
                dataPoints = listOf(5, 8, 12, 10, 15, students.size),
                labels = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PremiumCard(modifier = Modifier.weight(1.1f)) {
                Text(
                    text = "Taux d'assiduité",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                AttendanceRadialRing(
                    percentage = attendanceRate,
                    title = "Assiduité générale",
                    subtitle = "$presentCount présences enregistrées"
                )
            }

            PaymentsProgressCard(
                received = receivedAmount,
                pending = pendingAmount,
                modifier = Modifier.weight(0.9f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- 2. STUDENTS PANE ---
@Composable
fun StudentsPane(
    viewModel: EduViewModel,
    students: List<Student>,
    groups: List<SchoolGroup>
) {
    var showAddForm by remember { mutableStateOf(false) }
    var showEditForm by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Form States (used for both Add and Edit)
    var fName by remember { mutableStateOf("") }
    var lName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Féminin") }
    var level by remember { mutableStateOf("3AS") }
    var track by remember { mutableStateOf("Sciences expérimentales") }
    var comments by remember { mutableStateOf("") }
    var selectedGroupIds by remember { mutableStateOf(setOf<Int>()) }

    // Helper to start Add form
    fun startAddForm() {
        fName = ""
        lName = ""
        phone = ""
        parentPhone = ""
        address = ""
        school = ""
        gender = "Féminin"
        level = "3AS"
        track = "Sciences expérimentales"
        comments = ""
        selectedGroupIds = emptySet()
        showAddForm = true
    }

    // Helper to start Edit form
    fun startEditForm(student: Student) {
        fName = student.firstName
        lName = student.lastName
        phone = student.phone
        parentPhone = student.parentPhone
        address = student.address
        school = student.school
        gender = student.gender
        level = student.level
        track = student.track
        comments = student.comments
        selectedGroupIds = student.enrolledGroupIds.split(",")
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        showEditForm = true
    }

    if (showAddForm || showEditForm) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showAddForm) "Inscrire un nouvel élève" else "Modifier le profil de l'élève",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = {
                    showAddForm = false
                    showEditForm = false
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = fName,
                    onValueChange = { fName = it },
                    label = { Text("Prénom", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().testTag("student_fname_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = lName,
                    onValueChange = { lName = it },
                    label = { Text("Nom", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().testTag("student_lname_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Téléphone élève", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = parentPhone,
                    onValueChange = { parentPhone = it },
                    label = { Text("Téléphone parent", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresse", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = school,
                    onValueChange = { school = it },
                    label = { Text("Établissement scolaire", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Gender Selection
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Genre: ", color = Color.White)
                    listOf("Féminin", "Masculin").forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gender == item,
                                onClick = { gender = item },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6366F1))
                            )
                            Text(item, color = Color.White, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Level Selection
                OutlinedTextField(
                    value = level,
                    onValueChange = { level = it },
                    label = { Text("Niveau (ex: 5AP, 4AM, 3AS, Pro)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = track,
                    onValueChange = { track = it },
                    label = { Text("Filière / Spécialité", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Commentaires / Remarques", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Enrolled Groups Multiselect
                Text("Inscrire aux cours / groupes :", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (groups.isEmpty()) {
                    Text("Aucun cours ou groupe disponible.", color = Color.Gray, fontSize = 12.sp)
                } else {
                    groups.forEach { group ->
                        val isChecked = selectedGroupIds.contains(group.id)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isChecked) Color(0xFF1E1B4B) else Color(0xFF181A1F))
                                .clickable {
                                    selectedGroupIds = if (isChecked) {
                                        selectedGroupIds - group.id
                                    } else {
                                        selectedGroupIds + group.id
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    selectedGroupIds = if (checked == true) {
                                        selectedGroupIds + group.id
                                    } else {
                                        selectedGroupIds - group.id
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6366F1))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(group.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("${group.subject} | ${group.dayOfWeek} ${group.timeSlot} | ${group.fee} DZD", color = Color.Gray, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (fName.isNotEmpty() && lName.isNotEmpty()) {
                            val studentToSave = Student(
                                id = if (showEditForm && selectedStudent != null) selectedStudent!!.id else 0,
                                firstName = fName,
                                lastName = lName,
                                phone = phone,
                                parentPhone = parentPhone,
                                address = address,
                                school = school,
                                birthDate = "2010-01-01",
                                gender = gender,
                                level = level,
                                track = track,
                                comments = comments,
                                enrolledGroupIds = selectedGroupIds.joinToString(",")
                            )
                            viewModel.addStudent(studentToSave)
                            
                            showAddForm = false
                            showEditForm = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_student_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text(
                        text = if (showAddForm) "Inscrire l'élève" else "Enregistrer les modifications",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else if (selectedStudent != null) {
        val studentId = selectedStudent!!.id
        val student = students.find { it.id == studentId } ?: selectedStudent!!
        val enrolledGroupIdsList = student.enrolledGroupIds.split(",")
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }
        val enrolledGroups = groups.filter { it.id in enrolledGroupIdsList }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Détail de l'élève", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { selectedStudent = null }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${student.firstName.take(1)}${student.lastName.take(1)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("${student.firstName} ${student.lastName}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Niveau: ${student.level} ${if (student.track.isNotEmpty()) "(${student.track})" else ""}", color = Color(0xFF9CA3AF), fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                listOf(
                    "Téléphone élève" to student.phone,
                    "Téléphone parent" to student.parentPhone,
                    "Adresse" to student.address,
                    "École" to student.school,
                    "Remarques" to student.comments
                ).forEach { (label, value) ->
                    if (value.isNotEmpty()) {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("$label : ", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(value, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFF1F2226))
                Spacer(modifier = Modifier.height(20.dp))

                // Display enrolled groups
                Text("Cours / Groupes d'Inscription :", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (enrolledGroups.isEmpty()) {
                    Text("Cet élève n'est inscrit à aucun cours pour le moment.", color = Color.Gray, fontSize = 13.sp)
                } else {
                    enrolledGroups.forEach { group ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF181A1F))
                                .border(1.dp, Color(0xFF1F2226), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(group.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${group.subject} — ${group.teacherName}", color = Color.Gray, fontSize = 11.sp)
                                Text("${group.dayOfWeek} | ${group.timeSlot}", color = Color(0xFF818CF8), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E1B4B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("${group.fee} DZD", color = Color(0xFFA5B4FC), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { startEditForm(student) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Modifier", color = Color.White)
                    }
                    Button(
                        onClick = {
                            viewModel.deleteStudent(student.id)
                            selectedStudent = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Désinscrire l'élève", color = Color.White)
                    }
                }
            }
        }
    } else {
        val filteredStudents = students.filter { student ->
            student.firstName.contains(searchQuery, ignoreCase = true) ||
            student.lastName.contains(searchQuery, ignoreCase = true) ||
            student.level.contains(searchQuery, ignoreCase = true) ||
            student.phone.contains(searchQuery)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gestion des Élèves", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { startAddForm() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_student_fab")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ajouter", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Rechercher un élève (nom, prénom, niveau...)", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Rechercher", tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (filteredStudents.isEmpty()) {
                val titleText = if (students.isEmpty()) "Aucun élève inscrit" else "Aucun résultat trouvé"
                val descText = if (students.isEmpty()) "Inscrivez des élèves pour commencer à planifier des cours, suivre les présences, et générer des factures." else "Essayez une autre recherche."
                EmptyStateView(
                    title = titleText,
                    description = descText,
                    icon = Icons.Default.People
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filteredStudents) { student ->
                        val enrolledGroupIdsList = student.enrolledGroupIds.split(",")
                            .filter { it.isNotEmpty() }
                            .mapNotNull { it.toIntOrNull() }
                        val enrolledGroups = groups.filter { it.id in enrolledGroupIdsList }

                        PremiumCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedStudent = student }
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF1F2226)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${student.firstName.take(1)}${student.lastName.take(1)}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("${student.firstName} ${student.lastName}", color = Color.White, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = "Niveau: ${student.level} | Tél: ${student.phone.ifEmpty { "N/A" }}",
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                                }

                                if (enrolledGroups.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Cours :", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                        enrolledGroups.take(3).forEach { group ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF1E1B4B))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(group.name, color = Color(0xFFA5B4FC), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        if (enrolledGroups.size > 3) {
                                            Text("+${enrolledGroups.size - 3}", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 3. GROUPS PANE ---
@Composable
fun GroupsPane(
    viewModel: EduViewModel,
    groups: List<SchoolGroup>,
    teachers: List<Teacher>,
    userType: String?
) {
    var showAddForm by remember { mutableStateOf(false) }

    // Form states
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var classLevel by remember { mutableStateOf("3AS") }
    var capacity by remember { mutableStateOf("15") }
    var fee by remember { mutableStateOf("4000") }
    var room by remember { mutableStateOf("Salle A") }
    var day by remember { mutableStateOf("Vendredi") }
    var timeSlot by remember { mutableStateOf("09:00 - 11:00") }
    var teacherName by remember { mutableStateOf("") }
    var isFormation by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf("") }

    if (showAddForm) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = if (isFormation) "Créer une formation personnalisée" else "Créer un groupe de cours"
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { showAddForm = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                if (userType == "SCHOOL") {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                        Text("Type de cours: ", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(12.dp))
                        listOf(false to "Groupe Standard", true to "Formation Pro").forEach { (valBool, labelStr) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = isFormation == valBool,
                                    onClick = { isFormation = valBool },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6366F1))
                                )
                                Text(labelStr, color = Color.White, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du groupe / de la formation", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().testTag("group_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Matière / Thème", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = classLevel,
                    onValueChange = { classLevel = it },
                    label = { Text("Niveau (ex: 3AS, Débutant, Professionnel)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Nombre de places (Capacité)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = fee,
                    onValueChange = { fee = it },
                    label = { Text("Tarif (DZD)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Salle", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it },
                    label = { Text("Jour de la semaine (ex: Vendredi, Samedi)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Heure / Créneau (ex: 10:00 - 12:00)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text("Professeur / Formateur", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )

                if (isFormation) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Durée de la formation (ex: 3 mois)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && subject.isNotEmpty()) {
                            viewModel.addGroup(
                                SchoolGroup(
                                    name = name,
                                    subject = subject,
                                    level = classLevel,
                                    capacity = capacity.toIntOrNull() ?: 15,
                                    fee = fee.toDoubleOrNull() ?: 3000.0,
                                    room = room,
                                    dayOfWeek = day,
                                    timeSlot = timeSlot,
                                    teacherName = teacherName.ifEmpty { "Non assigné" },
                                    isFormation = isFormation,
                                    duration = duration
                                )
                            )
                            showAddForm = false
                            // reset
                            name = ""
                            subject = ""
                            teacherName = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_group_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Créer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Groupes & Formations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { showAddForm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Créer", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Category selector if Academy or School ERP
            if (userType == "SCHOOL" || userType == "ACADEMY") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Soutien Scolaire" to false, "Formations Pro" to true).forEach { (label, isForm) ->
                        val count = groups.count { it.isFormation == isForm }
                        PremiumCard(
                            modifier = Modifier.weight(1f),
                            backgroundGradient = listOf(Color(0xFF1F2226), Color(0xFF16181B))
                        ) {
                            Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("$count créés", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            if (groups.isEmpty()) {
                EmptyStateView(
                    title = "Aucun groupe actif",
                    description = "Créez votre premier groupe d'élèves ou formation professionnelle pour débloquer le planning et le suivi.",
                    icon = Icons.Default.Layers
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(groups) { group ->
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(group.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        if (group.isFormation) {
                                            StatusBadge(text = "FORMATION PRO", status = "SUCCESS")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Matière: ${group.subject} | Prof: ${group.teacherName}", color = Color.Gray, fontSize = 12.sp)
                                    Text("Planning: ${group.dayOfWeek} (${group.timeSlot}) | Salle: ${group.room}", color = Color.Gray, fontSize = 12.sp)
                                    Text("Tarif: ${group.fee.toInt()} DZD / Capacité: ${group.capacity} places", color = Color(0xFF6366F1), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                IconButton(onClick = { viewModel.deleteGroup(group.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFEF4444))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 4. PLANNING PANE ---
@Composable
fun PlanningPane(viewModel: EduViewModel, groups: List<SchoolGroup>) {
    val students by viewModel.students.collectAsState()
    val appointments by viewModel.appointments.collectAsState()

    var selectedDay by remember { mutableStateOf(12) } // default active day
    var selectedMonth by remember { mutableStateOf(7) } // July
    var selectedYear by remember { mutableStateOf(2026) }

    var showAddDialog by remember { mutableStateOf(false) }

    // Helpers to compute days in month and start weekday
    val daysInMonth = when (selectedMonth) {
        2 -> if (selectedYear % 4 == 0) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    val calendarHelper = java.util.Calendar.getInstance().apply {
        set(java.util.Calendar.YEAR, selectedYear)
        set(java.util.Calendar.MONTH, selectedMonth - 1)
        set(java.util.Calendar.DAY_OF_MONTH, 1)
    }
    // Convert Sunday=1, Monday=2 to 0=Monday, ..., 6=Sunday
    val startDayOfWeek = (calendarHelper.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7

    val monthNames = listOf(
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    )

    val currentFormattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)

    // Filter appointments for the selected day
    val activeAppointments = appointments.filter { it.date == currentFormattedDate }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Gestion du Planning & RDV", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Planifiez des blocs de cours et rendez-vous", color = Color.Gray, fontSize = 12.sp)
            }

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("add_appointment_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Planifier", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CALENDAR CARD WIDGET ---
        PremiumCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                // Calendar Header (Month Switcher)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (selectedMonth == 1) {
                            selectedMonth = 12
                            selectedYear--
                        } else {
                            selectedMonth--
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Mois précédent", tint = Color.White)
                    }

                    Text(
                        text = "${monthNames[selectedMonth - 1]} $selectedYear",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    IconButton(onClick = {
                        if (selectedMonth == 12) {
                            selectedMonth = 1
                            selectedYear++
                        } else {
                            selectedMonth++
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Mois suivant", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Days of week header
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim").forEach { day ->
                        Text(
                            text = day,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Monthly Grid of days
                var currentDayCount = 1
                val totalSlots = daysInMonth + startDayOfWeek
                val rows = (totalSlots + 6) / 7

                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (col in 0 until 7) {
                            val slotIndex = row * 7 + col
                            if (slotIndex < startDayOfWeek || currentDayCount > daysInMonth) {
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                val dayNum = currentDayCount
                                val isSelected = dayNum == selectedDay
                                val slotDateStr = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, dayNum)
                                val hasAppointment = appointments.any { it.date == slotDateStr }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1.1f)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) Color(0xFF6366F1)
                                            else if (hasAppointment) Color(0xFF1E1B4B)
                                            else Color.Transparent
                                        )
                                        .clickable { selectedDay = dayNum }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayNum.toString(),
                                            color = if (isSelected) Color.White else Color.White,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 12.sp
                                        )
                                        if (hasAppointment && !isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 2.dp)
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF818CF8))
                                            )
                                        }
                                    }
                                }
                                currentDayCount++
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- APPOINTMENTS LIST FOR THE SELECTED DAY ---
        Text(
            text = "Rendez-vous du $selectedDay ${monthNames[selectedMonth - 1]}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (activeAppointments.isEmpty()) {
            EmptyStateView(
                title = "Aucun rendez-vous ce jour",
                description = "Planifiez un cours ou un entretien individuel pour ce jour en cliquant sur le bouton Planifier.",
                icon = Icons.Default.CalendarToday
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(activeAppointments) { appointment ->
                    val linkedStudent = students.find { it.id == appointment.studentId }
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1E1B4B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Color(0xFFA5B4FC))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(appointment.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = "Élève: ${linkedStudent?.firstName ?: ""} ${linkedStudent?.lastName ?: "Non assigné"}",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Créneau: ${appointment.timeSlot}",
                                        color = Color(0xFF818CF8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (appointment.notes.isNotEmpty()) {
                                        Text(
                                            text = "Note: ${appointment.notes}",
                                            color = Color.Gray,
                                            fontSize = 11.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = { viewModel.deleteAppointment(appointment.id) },
                                modifier = Modifier.testTag("delete_appointment_button_${appointment.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }
    }

    // --- ADD APPOINTMENT DIALOG ---
    if (showAddDialog) {
        var appTitle by remember { mutableStateOf("Soutien Scolaire Individuel") }
        var selectedStudentId by remember { mutableStateOf<Int?>(null) }
        var appTimeSlot by remember { mutableStateOf("14:00 - 15:30") }
        var appNotes by remember { mutableStateOf("") }
        var isDropdownExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    "Nouveau Bloc de Cours / RDV",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Date planifiée : $currentFormattedDate", color = Color(0xFF818CF8), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                    // Title Input
                    OutlinedTextField(
                        value = appTitle,
                        onValueChange = { appTitle = it },
                        label = { Text("Intitulé / Sujet") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    // Student Dropdown/Selector
                    Text("Sélectionner l'élève :", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    if (students.isEmpty()) {
                        Text("Aucun élève inscrit dans votre académie.", color = Color.Gray, fontSize = 12.sp)
                    } else {
                        val currentSelectedStudentName = students.find { it.id == selectedStudentId }?.let { "${it.firstName} ${it.lastName}" } ?: "Choisir un élève..."
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E2125))
                                .border(1.dp, Color(0xFF2E3035), RoundedCornerShape(8.dp))
                                .clickable { isDropdownExpanded = true }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(currentSelectedStudentName, color = Color.White, fontSize = 13.sp)
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                            }

                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier.background(Color(0xFF1E2125))
                            ) {
                                students.forEach { student ->
                                    DropdownMenuItem(
                                        text = { Text("${student.firstName} ${student.lastName}", color = Color.White) },
                                        onClick = {
                                            selectedStudentId = student.id
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Time Slot Input
                    OutlinedTextField(
                        value = appTimeSlot,
                        onValueChange = { appTimeSlot = it },
                        label = { Text("Créneau horaire (ex: 14:00 - 15:30)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    // Notes Input
                    OutlinedTextField(
                        value = appNotes,
                        onValueChange = { appNotes = it },
                        label = { Text("Notes / Détails") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetStudentId = selectedStudentId
                        if (targetStudentId != null && appTitle.isNotEmpty()) {
                            viewModel.addAppointment(
                                Appointment(
                                    studentId = targetStudentId,
                                    title = appTitle,
                                    date = currentFormattedDate,
                                    timeSlot = appTimeSlot,
                                    notes = appNotes
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    enabled = selectedStudentId != null && appTitle.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Valider", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Annuler", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF111317)
        )
    }
}

// --- 5. ATTENDANCE PANE ---
@Composable
fun AttendancePane(
    viewModel: EduViewModel,
    students: List<Student>,
    groups: List<SchoolGroup>,
    attendance: List<Attendance>
) {
    var selectedGroupForAttendance by remember { mutableStateOf<SchoolGroup?>(null) }
    val localDate = "2026-07-12" // current date mockup

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedGroupForAttendance == null) {
            Text("Sélectionnez un groupe pour marquer les présences", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (groups.isEmpty()) {
                EmptyStateView(
                    title = "Aucun groupe disponible",
                    description = "Créez d'abord un groupe scolaire dans l'onglet dédié.",
                    icon = Icons.Default.Layers
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(groups) { group ->
                        PremiumCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedGroupForAttendance = group }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(group.name, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Matière: ${group.subject} | ${group.dayOfWeek} ${group.timeSlot}", color = Color.Gray, fontSize = 12.sp)
                                }
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF6366F1))
                            }
                        }
                    }
                }
            }
        } else {
            val group = selectedGroupForAttendance!!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Présences : ${group.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { selectedGroupForAttendance = null }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            }
            Text("Date du jour : $localDate", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (students.isEmpty()) {
                EmptyStateView(
                    title = "Aucun élève inscrit",
                    description = "Veuillez inscrire des élèves pour faire l'appel.",
                    icon = Icons.Default.People
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(students) { student ->
                        // Find current attendance if marked
                        val currentAtt = attendance.find { it.studentId == student.id && it.groupId == group.id && it.date == localDate }

                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("${student.firstName} ${student.lastName}", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Statut actuel: ${currentAtt?.status ?: "Non marqué"}", color = Color.Gray, fontSize = 12.sp)
                                }

                                // Quick click buttons
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf(
                                        "PRESENT" to "P",
                                        "ABSENT" to "A",
                                        "LATE" to "R",
                                        "EXCUSED" to "J"
                                    ).forEach { (statusVal, letter) ->
                                        val isSelected = currentAtt?.status == statusVal
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                                                .clickable {
                                                    viewModel.addAttendance(
                                                        Attendance(
                                                            studentId = student.id,
                                                            groupId = group.id,
                                                            date = localDate,
                                                            status = statusVal
                                                        )
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = letter,
                                                color = if (isSelected) Color.White else Color.Gray,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 6. PAYMENTS PANE ---
@Composable
fun PaymentsPane(
    viewModel: EduViewModel,
    students: List<Student>,
    groups: List<SchoolGroup>,
    payments: List<Payment>
) {
    var showAddPaymentForm by remember { mutableStateOf(false) }

    // Form states
    var selectedStudentId by remember { mutableStateOf(0) }
    var selectedGroupId by remember { mutableStateOf(0) }
    var amount by remember { mutableStateOf("") }
    var payStatus by remember { mutableStateOf("PAID") }
    var payMethod by remember { mutableStateOf("BARIDIMOB") }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showAddPaymentForm) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enregistrer un règlement", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { showAddPaymentForm = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                // Mock selects or manual text for simplicity and perfect build
                Text("Élève ID (Entrez l'ID numérique):", color = Color.Gray, fontSize = 12.sp)
                OutlinedTextField(
                    value = if (selectedStudentId == 0) "" else selectedStudentId.toString(),
                    onValueChange = { selectedStudentId = it.toIntOrNull() ?: 0 },
                    modifier = Modifier.fillMaxWidth().testTag("payment_student_id_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Groupe ID (Entrez l'ID numérique):", color = Color.Gray, fontSize = 12.sp)
                OutlinedTextField(
                    value = if (selectedGroupId == 0) "" else selectedGroupId.toString(),
                    onValueChange = { selectedGroupId = it.toIntOrNull() ?: 0 },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Montant payé (DZD)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Payment Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Statut: ", color = Color.White)
                    listOf("PAID" to "Reçu", "PENDING" to "En attente").forEach { (statusVal, label) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = payStatus == statusVal,
                                onClick = { payStatus = statusVal },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6366F1))
                            )
                            Text(label, color = Color.White, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Payment Method
                Text("Moyen de paiement :", color = Color.White, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("CASH" to "Espèces", "BARIDIMOB" to "Baridi", "CCP" to "CCP", "CARD" to "Carte").forEach { (methodVal, label) ->
                        val isSelected = payMethod == methodVal
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                                .clickable { payMethod = methodVal }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (amount.isNotEmpty() && selectedStudentId > 0) {
                            viewModel.addPayment(
                                Payment(
                                    studentId = selectedStudentId,
                                    groupId = selectedGroupId,
                                    amount = amount.toDoubleOrNull() ?: 3000.0,
                                    date = "2026-07-12",
                                    status = payStatus,
                                    method = payMethod
                                )
                            )
                            showAddPaymentForm = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_payment_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Enregistrer le paiement", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Comptabilité & Règlements", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { showAddPaymentForm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Paiement", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick accounting banner
            val totalReceived = payments.filter { it.status == "PAID" }.sumOf { it.amount }

            PremiumCard(modifier = Modifier.fillMaxWidth(), backgroundGradient = listOf(Color(0xFF07241A), Color(0xFF0F1012))) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Revenus encaissés ce mois", color = Color.Gray, fontSize = 12.sp)
                        Text("${totalReceived.toInt()} DZD", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                    Button(
                        onClick = { /* Simulated report exports */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2226))
                    ) {
                        Icon(imageVector = Icons.Default.SimCardDownload, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Exporter (PDF/Excel)", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (payments.isEmpty()) {
                EmptyStateView(
                    title = "Aucun paiement enregistré",
                    description = "Gérez la facturation de vos élèves ici. Vous pouvez suivre l'historique de chaque règlement.",
                    icon = Icons.Default.MonetizationOn
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(payments) { payment ->
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Élève ID : #${payment.studentId}", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Moyen: ${payment.method} | Date: ${payment.date}", color = Color.Gray, fontSize = 12.sp)
                                    Text("${payment.amount.toInt()} DZD", color = Color(0xFF818CF8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                val badgeStatus = if (payment.status == "PAID") "SUCCESS" else "WARNING"
                                val badgeText = if (payment.status == "PAID") "PAYÉ" else "REQUIS"
                                StatusBadge(text = badgeText, status = badgeStatus)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 7. EXAMS PANE ---
@Composable
fun ExamsPane(
    viewModel: EduViewModel,
    students: List<Student>,
    groups: List<SchoolGroup>,
    exams: List<Exam>
) {
    var showAddExam by remember { mutableStateOf(false) }

    // Form
    var examName by remember { mutableStateOf("") }
    var examType by remember { mutableStateOf("BAC_BLANC") }
    var selectedGroupId by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showAddExam) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Créer une évaluation / examen", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { showAddExam = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = examName,
                    onValueChange = { examName = it },
                    label = { Text("Nom de l'examen / Contrôle", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().testTag("exam_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Type d'évaluation :", color = Color.White, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("INTERROGATION" to "Interro", "COMPOSITION" to "Compo", "EXAMEN_BLANC" to "Blanc", "BAC_BLANC" to "BAC").forEach { (typeVal, label) ->
                        val isSelected = examType == typeVal
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                                .clickable { examType = typeVal }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text("Groupe d'élèves ID (Entrez l'ID numérique):", color = Color.Gray, fontSize = 12.sp)
                OutlinedTextField(
                    value = if (selectedGroupId == 0) "" else selectedGroupId.toString(),
                    onValueChange = { selectedGroupId = it.toIntOrNull() ?: 0 },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (examName.isNotEmpty() && selectedGroupId > 0) {
                            viewModel.addExam(
                                Exam(
                                    name = examName,
                                    type = examType,
                                    groupId = selectedGroupId,
                                    date = "2026-07-12"
                                ),
                                emptyMap() // Mock grades empty
                            )
                            showAddExam = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_exam_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Créer l'examen", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gestion des Évaluations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { showAddExam = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Créer Évaluation", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (exams.isEmpty()) {
                EmptyStateView(
                    title = "Aucun examen créé",
                    description = "Créez des examens, enregistrez les notes scolaires, et suivez la progression globale et le classement de vos élèves.",
                    icon = Icons.Default.Assignment
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(exams) { exam ->
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(exam.name, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Type : ${exam.type} | Groupe ID: #${exam.groupId}", color = Color.Gray, fontSize = 12.sp)
                                    Text("Moyenne classe estimée: 14.5/20 | Taux de réussite: 90%", color = Color(0xFF6366F1), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                StatusBadge(text = "BAC BLANC", status = "SUCCESS")
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 8. DOCUMENTS PANE ---
@Composable
fun DocumentsPane(
    viewModel: EduViewModel,
    groups: List<SchoolGroup>,
    documents: List<Document>
) {
    var showAddDocForm by remember { mutableStateOf(false) }
    var docName by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("PDF") }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showAddDocForm) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Téléverser un document de cours", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { showAddDocForm = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = docName,
                    onValueChange = { docName = it },
                    label = { Text("Nom du fichier (ex: Résumé Algèbre.pdf)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().testTag("document_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Type de document :", color = Color.White, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("PDF", "COURSE", "EXERCISE", "CORRECTION").forEach { typeVal ->
                        val isSelected = docType == typeVal
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                                .clickable { docType = typeVal }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(typeVal, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (docName.isNotEmpty()) {
                            viewModel.addDocument(
                                Document(
                                    name = docName,
                                    type = docType,
                                    groupId = 1,
                                    filePath = "/documents/$docName"
                                )
                            )
                            showAddDocForm = false
                            docName = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_document_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Partager le document", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Médiathèque & Fichiers", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { showAddDocForm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Partager", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (documents.isEmpty()) {
                EmptyStateView(
                    title = "Aucun document partagé",
                    description = "Partagez des PDF, énoncés d'exercices, résumés de cours, ou corrigés-types avec vos élèves.",
                    icon = Icons.Default.FolderOpen
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(documents) { doc ->
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.AttachFile, contentDescription = null, tint = Color(0xFF6366F1))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(doc.name, color = Color.White, fontWeight = FontWeight.Bold)
                                        Text("Catégorie : ${doc.type}", color = Color.Gray, fontSize = 12.sp)
                                    }
                                }
                                IconButton(onClick = { viewModel.deleteDocument(doc.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFEF4444))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 9. MESSAGES PANE ---
@Composable
fun MessagesPane(
    viewModel: EduViewModel,
    students: List<Student>,
    messages: List<AppMessage>
) {
    var recipientName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var msgType by remember { mutableStateOf("SMS") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Canal de communication", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text("Envoyer une alerte / rappel de paiement", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = recipientName,
                onValueChange = { recipientName = it },
                label = { Text("Destinataire (ex: Parent de Sofiane)", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().testTag("msg_recipient_input"),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Numéro ou Email", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Message", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("SMS", "EMAIL", "NOTIFICATION").forEach { item ->
                    val isSelected = msgType == item
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                            .clickable { msgType = item }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (recipientName.isNotEmpty() && content.isNotEmpty()) {
                        viewModel.sendMessage(
                            AppMessage(
                                recipientName = recipientName,
                                phone = phone,
                                type = msgType,
                                content = content
                            )
                        )
                        recipientName = ""
                        phone = ""
                        content = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_message_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Text("Diffuser le message", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Historique d'envoi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        if (messages.isEmpty()) {
            Text("Aucun message envoyé récemment.", color = Color.Gray, fontSize = 12.sp)
        } else {
            messages.forEach { msg ->
                PremiumCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("À: ${msg.recipientName} (${msg.phone})", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(msg.content, color = Color.Gray, fontSize = 13.sp)
                        }
                        StatusBadge(text = msg.type, status = "NEUTRAL")
                    }
                }
            }
        }
    }
}

// --- 10. ASSISTANT IA PANE ---
@Composable
fun AssistantAiPane(viewModel: EduViewModel) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var customPrompt by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        PremiumCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundGradient = listOf(Color(0xFF1E1B4B), Color(0xFF0F1012)),
            borderGradient = listOf(Color(0xFF818CF8), Color(0xFF1E2125))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFA5B4FC))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("EduManager Assistant IA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Modèle Gemini 3.5 Flash activé.", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick prompts cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                Triple("BAC Blanc", "Générer un sujet d'examen BAC Blanc", "GENERATE_EXAM"),
                Triple("Soutien", "Trouver élèves en difficulté", "ANALYZE_STRUGGLING")
            ).forEach { (btnLabel, desc, actionKey) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16181B))
                        .border(1.dp, Color(0xFF2E3035), RoundedCornerShape(12.dp))
                        .clickable { viewModel.triggerAiFeature(actionKey, "Sciences") }
                        .padding(14.dp)
                ) {
                    Column {
                        Text(btnLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(desc, color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                Triple("Résumé", "Rédiger résumé de cours condensé", "GENERATE_LESSON_SUMMARY"),
                Triple("Revenus", "Estimer les prévisions de revenus", "PREDICT_REVENUES")
            ).forEach { (btnLabel, desc, actionKey) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16181B))
                        .border(1.dp, Color(0xFF2E3035), RoundedCornerShape(12.dp))
                        .clickable { viewModel.triggerAiFeature(actionKey, "Mathématiques") }
                        .padding(14.dp)
                ) {
                    Column {
                        Text(btnLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(desc, color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Response Render box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F1012))
                .border(1.dp, Color(0xFF1F2226), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            if (isAiLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF6366F1))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Gemini réfléchit...", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            } else if (aiResponse != null) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = aiResponse!!,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sélectionnez une commande ou saisissez votre demande.", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Custom prompt input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = customPrompt,
                onValueChange = { customPrompt = it },
                placeholder = { Text("Poser une question à l'IA...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_custom_input"),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (customPrompt.isNotEmpty()) {
                        viewModel.triggerAiFeature("CUSTOM_PROMPT", customPrompt)
                        customPrompt = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6366F1))
                    .testTag("send_ai_button")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Envoyer", tint = Color.White)
            }
        }
    }
}

// --- 11. SETTINGS PANE ---
@Composable
fun SettingsPane(viewModel: EduViewModel, userType: String?) {
    val language by viewModel.currentLanguage.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (currentUser?.role == "TUTOR") "Mon Profil Enseignant" else "Mon Profil Administrateur",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            listOf(
                "Nom complet" to (currentUser?.fullName ?: "Prof. Farouk Bouayed"),
                "Adresse e-mail" to (currentUser?.email ?: "farouk.bouayed@edumanager.ai"),
                "Adresse locale" to "Hydra, Alger, Algérie",
                "Type de compte" to if (userType == "TUTOR") "Professeur Particulier" else if (userType == "ACADEMY") "Académie de Soutien" else "École Privée ERP"
            ).forEach { (label, value) ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("$label : ", color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(value, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text("Préférences du Système", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Language Selector
            Text("Langue de l'interface", color = Color.Gray, fontSize = 13.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("FR" to "Français 🇫🇷", "AR" to "العربية 🇩🇿", "EN" to "English 🇬🇧").forEach { (code, label) ->
                    val isSelected = language == code
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF1E2125))
                            .clickable { viewModel.setLanguage(code) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dark Mode toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mode Sombre Premium", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Activer le thème sombre style Stripe/Linear", color = Color.Gray, fontSize = 11.sp)
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF6366F1))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.logoutUser() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button")
        ) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Se déconnecter de la session", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.resetApp() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("reset_app_button")
        ) {
            Text("Réinitialiser l'application", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
