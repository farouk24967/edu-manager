package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSetting(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val parentPhone: String,
    val address: String,
    val school: String,
    val birthDate: String,
    val gender: String,
    val level: String, // e.g., 1AP, 4AM, 3AS
    val track: String = "", // e.g., Sciences, Math, etc.
    val photoUri: String = "",
    val comments: String = "",
    val enrolledGroupIds: String = ""
)

@Entity(tableName = "school_groups")
data class SchoolGroup(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val subject: String,
    val level: String,
    val capacity: Int,
    val fee: Double,
    val room: String,
    val dayOfWeek: String, // e.g., Monday, Friday
    val timeSlot: String,  // e.g., 10:00 - 12:00
    val teacherName: String,
    val isFormation: Boolean = false, // True for custom formations/trainings
    val duration: String = "",        // e.g., "3 months"
    val teacherId: Int = 0
)

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val subject: String,
    val salary: Double
)

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val groupId: Int,
    val date: String, // YYYY-MM-DD
    val status: String // PRESENT, ABSENT, LATE, EXCUSED
)

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val groupId: Int,
    val amount: Double,
    val date: String,
    val status: String, // PAID, PENDING
    val method: String  // CCP, BARIDIMOB, CASH, CARD
)

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // INTERROGATION, COMPOSITION, EXAMEN_BLANC, BAC_BLANC
    val groupId: Int,
    val date: String
)

@Entity(tableName = "grades")
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val examId: Int,
    val value: Double // Grade value (e.g. out of 20)
)

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // PDF, COURSE, EXERCISE, CORRECTION
    val groupId: Int,
    val filePath: String
)

@Entity(tableName = "messages")
data class AppMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipientName: String,
    val phone: String,
    val type: String, // SMS, EMAIL, NOTIFICATION
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val role: String, // "TUTOR" or "ADMIN"
    val fullName: String
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val title: String,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. 14:00 - 15:30
    val notes: String = ""
)
