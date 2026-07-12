package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiClient
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EduViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = EduRepository(database.eduDao())

    // --- Core State Variables ---
    val allSettings = repository.allSettings.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val students = repository.allStudents.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val groups = repository.allGroups.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val teachers = repository.allTeachers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val attendance = repository.allAttendance.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val payments = repository.allPayments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val exams = repository.allExams.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val documents = repository.allDocuments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val messages = repository.allMessages.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Secure Auth & Session States ---
    val authManager = AuthManager(application, repository)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    val isFirebaseOnline = authManager.isFirebaseOnline

    // --- Appointments State Flow ---
    val appointments = repository.allAppointments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- UI/Onboarding State Flows ---
    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType.asStateFlow()

    private val _onboardingStep = MutableStateFlow(0) // 0 = not started/selection, 1 = Teaching Type, 2 = Level, 3 = Tracks, 4 = Complete
    val onboardingStep: StateFlow<Int> = _onboardingStep.asStateFlow()

    private val _teachingType = MutableStateFlow("") // ALL, SINGLE
    val teachingType: StateFlow<String> = _teachingType.asStateFlow()

    private val _selectedLevel = MutableStateFlow("") // PRIMAIRE, CEM, LYCEE
    val selectedLevel: StateFlow<String> = _selectedLevel.asStateFlow()

    private val _selectedTracks = MutableStateFlow<Set<String>>(emptySet())
    val selectedTracks: StateFlow<Set<String>> = _selectedTracks.asStateFlow()

    private val _currentLanguage = MutableStateFlow("FR")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true) // Premium dark by default!
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // --- AI Integration State ---
    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        viewModelScope.launch {
            // Read settings from DB
            allSettings.collect { settings ->
                val type = settings.find { it.key == "user_type" }?.value
                _userType.value = type

                val lang = settings.find { it.key == "language" }?.value ?: "FR"
                _currentLanguage.value = lang

                val theme = settings.find { it.key == "is_dark_mode" }?.value ?: "true"
                _isDarkMode.value = theme.toBoolean()

                val teachType = settings.find { it.key == "teaching_type" }?.value ?: ""
                _teachingType.value = teachType

                val level = settings.find { it.key == "selected_level" }?.value ?: ""
                _selectedLevel.value = level

                val tracksStr = settings.find { it.key == "selected_tracks" }?.value ?: ""
                _selectedTracks.value = if (tracksStr.isEmpty()) emptySet() else tracksStr.split(",").toSet()

                val step = settings.find { it.key == "onboarding_step" }?.value?.toIntOrNull() ?: 0
                _onboardingStep.value = step

                // If DB is totally empty, pre-populate with nice sample data
                if (settings.isEmpty()) {
                    prepopulateSampleData()
                }
            }
        }
    }

    // --- Configuration actions ---
    fun selectUserType(type: String) {
        viewModelScope.launch {
            repository.saveSetting("user_type", type)
            _userType.value = type
            if (type == "TUTOR") {
                _onboardingStep.value = 1
                repository.saveSetting("onboarding_step", "1")
            } else {
                _onboardingStep.value = 4
                repository.saveSetting("onboarding_step", "4")
            }
        }
    }

    fun setTeachingType(type: String) {
        viewModelScope.launch {
            _teachingType.value = type
            repository.saveSetting("teaching_type", type)
            if (type == "ALL") {
                _onboardingStep.value = 4
                repository.saveSetting("onboarding_step", "4")
            } else {
                _onboardingStep.value = 2
                repository.saveSetting("onboarding_step", "2")
            }
        }
    }

    fun setSelectedLevel(level: String) {
        viewModelScope.launch {
            _selectedLevel.value = level
            repository.saveSetting("selected_level", level)
            if (level == "LYCEE") {
                _onboardingStep.value = 3
                repository.saveSetting("onboarding_step", "3")
            } else {
                _onboardingStep.value = 4
                repository.saveSetting("onboarding_step", "4")
            }
        }
    }

    fun toggleTrack(track: String) {
        val current = _selectedTracks.value.toMutableSet()
        if (current.contains(track)) {
            current.remove(track)
        } else {
            current.add(track)
        }
        viewModelScope.launch {
            _selectedTracks.value = current
            repository.saveSetting("selected_tracks", current.joinToString(","))
        }
    }

    fun finishTutorOnboarding() {
        viewModelScope.launch {
            _onboardingStep.value = 4
            repository.saveSetting("onboarding_step", "4")
        }
    }

    fun resetApp() {
        viewModelScope.launch {
            _userType.value = null
            _onboardingStep.value = 0
            _teachingType.value = ""
            _selectedLevel.value = ""
            _selectedTracks.value = emptySet()
            
            // Clear settings in DB
            repository.saveSetting("user_type", "")
            repository.saveSetting("onboarding_step", "0")
            repository.saveSetting("teaching_type", "")
            repository.saveSetting("selected_level", "")
            repository.saveSetting("selected_tracks", "")
        }
    }

    // --- DB Mutations ---
    fun addStudent(student: Student) {
        viewModelScope.launch { repository.insertStudent(student) }
    }

    fun deleteStudent(id: Int) {
        viewModelScope.launch { repository.deleteStudent(id) }
    }

    fun addGroup(group: SchoolGroup) {
        viewModelScope.launch { repository.insertGroup(group) }
    }

    fun deleteGroup(id: Int) {
        viewModelScope.launch { repository.deleteGroup(id) }
    }

    fun addTeacher(teacher: Teacher) {
        viewModelScope.launch { repository.insertTeacher(teacher) }
    }

    fun deleteTeacher(id: Int) {
        viewModelScope.launch { repository.deleteTeacher(id) }
    }

    fun addAttendance(attendance: Attendance) {
        viewModelScope.launch { repository.insertAttendance(attendance) }
    }

    fun addPayment(payment: Payment) {
        viewModelScope.launch { repository.insertPayment(payment) }
    }

    fun addExam(exam: Exam, studentGrades: Map<Int, Double>) {
        viewModelScope.launch {
            val examId = repository.insertExam(exam).toInt()
            studentGrades.forEach { (studentId, gradeVal) ->
                repository.insertGrade(Grade(studentId = studentId, examId = examId, value = gradeVal))
            }
        }
    }

    fun addDocument(document: Document) {
        viewModelScope.launch { repository.insertDocument(document) }
    }

    fun deleteDocument(id: Int) {
        viewModelScope.launch { repository.deleteDocument(id) }
    }

    fun sendMessage(message: AppMessage) {
        viewModelScope.launch { repository.insertMessage(message) }
    }

    // --- Authentication Actions ---
    fun registerUser(email: String, passwordHash: String, fullName: String, role: String) {
        _isAuthLoading.value = true
        _authError.value = null
        viewModelScope.launch {
            authManager.signUp(email, passwordHash, fullName, role,
                onSuccess = { user ->
                    _currentUser.value = user
                    _isAuthLoading.value = false
                    // Update user type setting based on role
                    viewModelScope.launch {
                        repository.saveSetting("user_type", user.role)
                        _userType.value = user.role
                    }
                },
                onFailure = { error ->
                    _authError.value = error
                    _isAuthLoading.value = false
                }
            )
        }
    }

    fun loginUser(email: String, passwordHash: String) {
        _isAuthLoading.value = true
        _authError.value = null
        viewModelScope.launch {
            authManager.signIn(email, passwordHash,
                onSuccess = { user ->
                    _currentUser.value = user
                    _isAuthLoading.value = false
                    // Update user type setting based on role
                    viewModelScope.launch {
                        repository.saveSetting("user_type", user.role)
                        _userType.value = user.role
                    }
                },
                onFailure = { error ->
                    _authError.value = error
                    _isAuthLoading.value = false
                }
            )
        }
    }

    fun logoutUser() {
        _currentUser.value = null
        _authError.value = null
    }

    fun clearAuthError() {
        _authError.value = null
    }

    // --- Appointments Operations ---
    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.insertAppointment(appointment)
        }
    }

    fun deleteAppointment(id: Int) {
        viewModelScope.launch {
            repository.deleteAppointment(id)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            _currentLanguage.value = lang
            repository.saveSetting("language", lang)
        }
    }

    fun setDarkMode(dark: Boolean) {
        viewModelScope.launch {
            _isDarkMode.value = dark
            repository.saveSetting("is_dark_mode", dark.toString())
        }
    }

    // --- AI Assistant Prompt Engines ---
    fun triggerAiFeature(actionType: String, param: String = "") {
        _isAiLoading.value = true
        _aiResponse.value = null

        viewModelScope.launch {
            val prompt = when (actionType) {
                "GENERATE_EXAM" -> """
                    Génère un sujet d'examen de niveau ${param.ifEmpty { "Lycée" }} pour le cours '${param.ifEmpty { "Sciences" }}'. 
                    Inclus des exercices pratiques, un barème clair sur 20 points, et des conseils de correction. 
                    Mets en forme avec des titres en gras, des espacements propres, et un style professionnel.
                """.trimIndent()
                
                "GENERATE_LESSON_SUMMARY" -> """
                    Rédige un résumé de cours premium ultra-condensé et structuré sur le thème de '${param.ifEmpty { "Algèbre Linéaire" }}'.
                    Utilise une pédagogie moderne (Notion style), avec des définitions claires, les formules importantes mises en valeur, et un exemple d'application.
                """.trimIndent()

                "ANALYZE_STRUGGLING" -> {
                    val studentsStr = students.value.take(5).joinToString { "${it.firstName} ${it.lastName} (${it.level})" }
                    """
                        Voici une liste de mes élèves : $studentsStr. 
                        Simule une analyse des performances et de l'assiduité basée sur un historique factuel. 
                        Détecte les 2 élèves les plus susceptibles d'être en difficulté, explique les causes probables (absentéisme, notes en baisse), et propose un plan de soutien personnalisé pour chacun.
                    """.trimIndent()
                }

                "PREDICT_REVENUES" -> {
                    val currentRevenue = payments.value.sumOf { it.amount }
                    val activeStudents = students.value.size
                    val activeGroups = groups.value.size
                    """
                        Sur la base des données actuelles :
                        - Revenus perçus : $currentRevenue DZD
                        - Nombre d'élèves inscrits : $activeStudents
                        - Nombre de groupes actifs : $activeGroups
                        Estime les prévisions de revenus pour le mois prochain et le trimestre en cours. 
                        Propose des leviers stratégiques d'optimisation financière (remplissage des créneaux, relances de paiements, ouvertures de formations).
                    """.trimIndent()
                }

                "CUSTOM_PROMPT" -> param

                else -> "Bonjour, comment puis-je vous aider aujourd'hui ?"
            }

            val systemInstruction = """
                Tu es l'assistant d'administration scolaire intelligent d'EduManager AI. 
                Ton objectif est d'aider les enseignants, les directeurs d'académies et d'écoles à optimiser leur travail.
                Réponds de façon structurée, élégante, avec un ton professionnel et encourageant. Utilise des emojis légers pour la lisibilité.
                Reste concis mais apporte une valeur ajoutée maximale. Traduis si nécessaire selon la langue sélectionnée.
            """.trimIndent()

            val response = GeminiClient.askGemini(prompt, systemInstruction)
            _aiResponse.value = response
            _isAiLoading.value = false
        }
    }

    // --- Pre-population of beautiful mock SaaS data ---
    private suspend fun prepopulateSampleData() {
        // Core Setup
        repository.saveSetting("is_dark_mode", "true")
        repository.saveSetting("language", "FR")

        // Prepopulate students
        val sampleStudents = listOf(
            Student(firstName = "Sofiane", lastName = "Amrane", phone = "0555123456", parentPhone = "0661123456", address = "Alger Centre", school = "Lycée El Idrissi", birthDate = "2008-05-14", gender = "Masculin", level = "3AS", track = "Sciences expérimentales", comments = "Très motivé, prépare activement le BAC.", enrolledGroupIds = "1"),
            Student(firstName = "Lyna", lastName = "Belkacem", phone = "0555789012", parentPhone = "0661789012", address = "Didouche Mourad", school = "Lycée Descartes", birthDate = "2009-02-21", gender = "Féminin", level = "2AS", track = "Mathématiques", comments = "Excellente en physique, a besoin de renforcement en algèbre.", enrolledGroupIds = "1"),
            Student(firstName = "Yacine", lastName = "Dahmani", phone = "0550456789", parentPhone = "0660456789", address = "Kouba", school = "CEM Moufdi Zakaria", birthDate = "2011-10-05", gender = "Masculin", level = "4AM", comments = "Se prépare pour le BEM. Elève appliqué.", enrolledGroupIds = "2"),
            Student(firstName = "Meriem", lastName = "Haddad", phone = "0552334455", parentPhone = "0662334455", address = "Hydra", school = "Ecole Primaire Ibn Sina", birthDate = "2015-07-30", gender = "Féminin", level = "5AP", comments = "Très créative, aime le calcul mental.", enrolledGroupIds = "3")
        )
        sampleStudents.forEach { repository.insertStudent(it) }

        // Prepopulate school groups (classes/formations)
        val sampleGroups = listOf(
            SchoolGroup(name = "BAC Sciences - Matin", subject = "Sciences Naturelles", level = "3AS", capacity = 15, fee = 4000.0, room = "Salle A1", dayOfWeek = "Vendredi", timeSlot = "08:30 - 10:30", teacherName = "Prof. Mansouri", isFormation = false),
            SchoolGroup(name = "BEM Physique/Chimie", subject = "Physique", level = "4AM", capacity = 12, fee = 3000.0, room = "Salle B3", dayOfWeek = "Samedi", timeSlot = "14:00 - 16:00", teacherName = "Prof. Haddad", isFormation = false),
            SchoolGroup(name = "Formation Web Dev Fullstack", subject = "Next.js & Supabase", level = "Professionnel", capacity = 20, fee = 15000.0, room = "Lab Informatique 1", dayOfWeek = "Mardi", timeSlot = "18:00 - 21:00", teacherName = "Formateur Bouayed", isFormation = true, duration = "3 mois"),
            SchoolGroup(name = "Formation Anglais Business", subject = "Anglais Professionnel", level = "Intermédiaire", capacity = 10, fee = 8500.0, room = "Salle C2", dayOfWeek = "Jeudi", timeSlot = "17:30 - 19:30", teacherName = "Mme. Taylor", isFormation = true, duration = "6 semaines")
        )
        sampleGroups.forEach { repository.insertGroup(it) }

        // Prepopulate teachers
        val sampleTeachers = listOf(
            Teacher(name = "Prof. Mansouri", phone = "0770112233", subject = "Sciences", salary = 45000.0),
            Teacher(name = "Prof. Haddad", phone = "0770445566", subject = "Physique/Chimie", salary = 38000.0),
            Teacher(name = "Formateur Bouayed", phone = "0770778899", subject = "Informatique", salary = 60000.0)
        )
        sampleTeachers.forEach { repository.insertTeacher(it) }

        // Prepopulate payments
        val samplePayments = listOf(
            Payment(studentId = 1, groupId = 1, amount = 4000.0, date = "2026-07-01", status = "PAID", method = "BARIDIMOB"),
            Payment(studentId = 2, groupId = 1, amount = 4000.0, date = "2026-07-02", status = "PAID", method = "CASH"),
            Payment(studentId = 3, groupId = 2, amount = 3000.0, date = "2026-07-04", status = "PENDING", method = "CCP"),
            Payment(studentId = 4, groupId = 3, amount = 15000.0, date = "2026-07-05", status = "PAID", method = "CARD")
        )
        samplePayments.forEach { repository.insertPayment(it) }

        // Prepopulate attendance
        val sampleAttendance = listOf(
            Attendance(studentId = 1, groupId = 1, date = "2026-07-03", status = "PRESENT"),
            Attendance(studentId = 2, groupId = 1, date = "2026-07-03", status = "PRESENT"),
            Attendance(studentId = 3, groupId = 2, date = "2026-07-04", status = "PRESENT"),
            Attendance(studentId = 4, groupId = 3, date = "2026-07-05", status = "PRESENT")
        )
        sampleAttendance.forEach { repository.insertAttendance(it) }
        
        // Prepopulate a document
        repository.insertDocument(Document(name = "Sujet BAC Sciences 2025 corrigé.pdf", type = "PDF", groupId = 1, filePath = "/documents/bac_2025.pdf"))
    }
}
