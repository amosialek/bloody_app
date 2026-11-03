package pl.amosialek.bloody.data.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PeriodRecord(
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val flowIntensity: FlowIntensity = FlowIntensity.MEDIUM,
    val notes: String = "",
    val symptoms: List<String> = emptyList()
) {
    val isOngoing: Boolean
        get() = endDate == null

    val durationDays: Long
        get() = if (endDate != null) {
            ChronoUnit.DAYS.between(startDate, endDate) + 1
        } else {
            val today = LocalDate.now()
            if (today.isBefore(startDate)) 1 else ChronoUnit.DAYS.between(startDate, today) + 1
        }

    fun contains(date: LocalDate): Boolean {
        return if (endDate != null) {
            !date.isBefore(startDate) && !date.isAfter(endDate)
        } else {
            !date.isBefore(startDate) && !date.isAfter(LocalDate.now())
        }
    }
}
