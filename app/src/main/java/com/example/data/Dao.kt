package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EduDao {
    // --- Settings ---
    @Query("SELECT * FROM app_settings")
    fun getAllSettingsFlow(): Flow<List<AppSetting>>

    @Query("SELECT * FROM app_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingByKey(key: String): AppSetting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: AppSetting)

    // --- Students ---
    @Query("SELECT * FROM students ORDER BY lastName ASC, firstName ASC")
    fun getAllStudentsFlow(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    fun getStudentByIdFlow(id: Int): Flow<Student?>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Int): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudentById(id: Int)

    // --- Groups ---
    @Query("SELECT * FROM school_groups ORDER BY name ASC")
    fun getAllGroupsFlow(): Flow<List<SchoolGroup>>

    @Query("SELECT * FROM school_groups WHERE id = :id LIMIT 1")
    fun getGroupByIdFlow(id: Int): Flow<SchoolGroup?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: SchoolGroup): Long

    @Query("DELETE FROM school_groups WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    // --- Teachers ---
    @Query("SELECT * FROM teachers ORDER BY name ASC")
    fun getAllTeachersFlow(): Flow<List<Teacher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher): Long

    @Query("DELETE FROM teachers WHERE id = :id")
    suspend fun deleteTeacherById(id: Int)

    // --- Attendance ---
    @Query("SELECT * FROM attendance ORDER BY date DESC")
    fun getAllAttendanceFlow(): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE groupId = :groupId")
    fun getAttendanceForGroupFlow(groupId: Int): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId")
    fun getAttendanceForStudentFlow(studentId: Int): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance): Long

    @Query("DELETE FROM attendance WHERE id = :id")
    suspend fun deleteAttendanceById(id: Int)

    // --- Payments ---
    @Query("SELECT * FROM payments ORDER BY date DESC")
    fun getAllPaymentsFlow(): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE studentId = :studentId")
    fun getPaymentsForStudentFlow(studentId: Int): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long

    @Query("DELETE FROM payments WHERE id = :id")
    suspend fun deletePaymentById(id: Int)

    // --- Exams ---
    @Query("SELECT * FROM exams ORDER BY date DESC")
    fun getAllExamsFlow(): Flow<List<Exam>>

    @Query("SELECT * FROM exams WHERE groupId = :groupId")
    fun getExamsForGroupFlow(groupId: Int): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam): Long

    @Query("DELETE FROM exams WHERE id = :id")
    suspend fun deleteExamById(id: Int)

    // --- Grades ---
    @Query("SELECT * FROM grades WHERE examId = :examId")
    fun getGradesForExamFlow(examId: Int): Flow<List<Grade>>

    @Query("SELECT * FROM grades WHERE studentId = :studentId")
    fun getGradesForStudentFlow(studentId: Int): Flow<List<Grade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: Grade): Long

    @Query("DELETE FROM grades WHERE id = :id")
    suspend fun deleteGradeById(id: Int)

    // --- Documents ---
    @Query("SELECT * FROM documents ORDER BY name ASC")
    fun getAllDocumentsFlow(): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document): Long

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteDocumentById(id: Int)

    // --- Messages ---
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessagesFlow(): Flow<List<AppMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AppMessage): Long
}
