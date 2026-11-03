package pl.amosialek.bloody.data.model

import java.time.LocalDate

data class DailySymptomLog(
    val date: LocalDate,
    val mood: String = "Normal Mood",
    val energy: String = "Mild Energy",
    val flow: String = "Light Flow",
    val symptoms: Set<String> = setOf("Normal Mood", "Mild Energy", "Light Flow"),
    val notes: String = ""
)
