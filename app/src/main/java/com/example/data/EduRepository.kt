package com.example.data

import kotlinx.coroutines.flow.Flow

class EduRepository(private val dao: EduDao) {
    // Settings
    val allSettings: Flow<List<AppSetting>> = dao.getAllSettingsFlow()
    suspend fun getSetting(key: String): String? = dao.getSettingByKey(key)?.value
    suspend fun saveSetting(key: String, value: String) = dao.insertSetting(AppSetting(key, value))

    // Students
    val allStudents: Flow<List<Student>> = dao.getAllStudentsFlow()
    fun getStudentById(id: Int): Flow<Student?> = dao.getStudentByIdFlow(id)
    suspend fun insertStudent(student: Student): Long = dao.insertStudent(student)
    suspend fun deleteStudent(id: Int) = dao.deleteStudentById(id)

    // Groups
    val allGroups: Flow<List<SchoolGroup>> = dao.getAllGroupsFlow()
    fun getGroupById(id: Int): Flow<SchoolGroup?> = dao.getGroupByIdFlow(id)
    suspend fun insertGroup(group: SchoolGroup): Long = dao.insertGroup(group)
    suspend fun deleteGroup(id: Int) = dao.deleteGroupById(id)

    // Teachers
    val allTeachers: Flow<List<Teacher>> = dao.getAllTeachersFlow()
    suspend fun insertTeacher(teacher: Teacher): Long = dao.insertTeacher(teacher)
    suspend fun deleteTeacher(id: Int) = dao.deleteTeacherById(id)

    // Attendance
    val allAttendance: Flow<List<Attendance>> = dao.getAllAttendanceFlow()
    fun getAttendanceForGroup(groupId: Int): Flow<List<Attendance>> = dao.getAttendanceForGroupFlow(groupId)
    fun getAttendanceForStudent(studentId: Int): Flow<List<Attendance>> = dao.getAttendanceForStudentFlow(studentId)
    suspend fun insertAttendance(attendance: Attendance): Long = dao.insertAttendance(attendance)
    suspend fun deleteAttendance(id: Int) = dao.deleteAttendanceById(id)

    // Payments
    val allPayments: Flow<List<Payment>> = dao.getAllPaymentsFlow()
    fun getPaymentsForStudent(studentId: Int): Flow<List<Payment>> = dao.getPaymentsForStudentFlow(studentId)
    suspend fun insertPayment(payment: Payment): Long = dao.insertPayment(payment)
    suspend fun deletePayment(id: Int) = dao.deletePaymentById(id)

    // Exams
    val allExams: Flow<List<Exam>> = dao.getAllExamsFlow()
    fun getExamsForGroup(groupId: Int): Flow<List<Exam>> = dao.getExamsForGroupFlow(groupId)
    suspend fun insertExam(exam: Exam): Long = dao.insertExam(exam)
    suspend fun deleteExam(id: Int) = dao.deleteExamById(id)

    // Grades
    fun getGradesForExam(examId: Int): Flow<List<Grade>> = dao.getGradesForExamFlow(examId)
    fun getGradesForStudent(studentId: Int): Flow<List<Grade>> = dao.getGradesForStudentFlow(studentId)
    suspend fun insertGrade(grade: Grade): Long = dao.insertGrade(grade)
    suspend fun deleteGrade(id: Int) = dao.deleteGradeById(id)

    // Documents
    val allDocuments: Flow<List<Document>> = dao.getAllDocumentsFlow()
    suspend fun insertDocument(document: Document): Long = dao.insertDocument(document)
    suspend fun deleteDocument(id: Int) = dao.deleteDocumentById(id)

    // Messages
    val allMessages: Flow<List<AppMessage>> = dao.getAllMessagesFlow()
    suspend fun insertMessage(message: AppMessage): Long = dao.insertMessage(message)
}
