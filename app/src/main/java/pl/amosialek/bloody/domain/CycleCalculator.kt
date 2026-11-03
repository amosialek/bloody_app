package pl.amosialek.bloody.domain

import pl.amosialek.bloody.data.model.PeriodRecord
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs

enum class CyclePhase(val displayName: String, val description: String) {
    MENSTRUAL("Menstrual Phase", "Uterine lining shedding"),
    FOLLICULAR("Follicular Phase", "Estrogen rising, egg maturation"),
    OVULATION("Ovulation Phase", "Egg release, highest fertility"),
    LUTEAL("Luteal Phase", "Progesterone peak, pre-menstrual balance")
}

enum class FertilityLevel(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    PEAK("Peak")
}

data class CycleInfo(
    val currentCycleDay: Int,
    val daysUntilNextPeriod: Int,
    val currentPhase: CyclePhase,
    val fertilityLevel: FertilityLevel,
    val averageCycleLength: Int,
    val averagePeriodLength: Int,
    val isPeriodOngoing: Boolean,
    val ongoingDays: Long?,
    val nextPeriodStartDate: LocalDate,
    val nextPeriodEndDate: LocalDate,
    val ovulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val cycleRegularity: String,
    val recordedCyclesCount: Int
)

object CycleCalculator {

    const val DEFAULT_CYCLE_LENGTH = 29
    const val DEFAULT_PERIOD_LENGTH = 5

    /**
     * Compute comprehensive cycle metrics based on user's recorded periods.
     */
    fun calculateCycleInfo(
        periods: List<PeriodRecord>,
        today: LocalDate = LocalDate.now()
    ): CycleInfo {
        val sortedPeriods = periods.sortedByDescending { it.startDate }
        val mostRecent = sortedPeriods.firstOrNull()

        // 1. Calculate Average Cycle Length
        val cycleLengths = mutableListOf<Int>()
        for (i in 0 until sortedPeriods.size - 1) {
            val days = ChronoUnit.DAYS.between(sortedPeriods[i + 1].startDate, sortedPeriods[i].startDate).toInt()
            if (days in 18..45) {
                cycleLengths.add(days)
            }
        }
        val avgCycleLength = if (cycleLengths.isNotEmpty()) {
            cycleLengths.average().toInt().coerceIn(21, 40)
        } else {
            DEFAULT_CYCLE_LENGTH
        }

        // 2. Calculate Average Period Length
        val periodDurations = sortedPeriods.filter { it.endDate != null }.map { it.durationDays.toInt() }
        val avgPeriodLength = if (periodDurations.isNotEmpty()) {
            periodDurations.average().toInt().coerceIn(2, 10)
        } else {
            DEFAULT_PERIOD_LENGTH
        }

        // Check if there's an ongoing period
        val ongoingPeriod = sortedPeriods.firstOrNull { it.isOngoing }
        val isOngoing = ongoingPeriod != null

        // 3. Current Cycle Day & Key Anchor Date
        val lastCycleStartDate = mostRecent?.startDate ?: today.minusDays(23)
        val cycleDay = (ChronoUnit.DAYS.between(lastCycleStartDate, today) + 1).toInt().coerceAtLeast(1)

        // 4. Next Period Expectation
        val nextPeriodStart = if (isOngoing) {
            today.plusDays(avgCycleLength.toLong() - (cycleDay % avgCycleLength))
        } else {
            val expected = lastCycleStartDate.plusDays(avgCycleLength.toLong())
            if (expected.isBefore(today)) {
                // If past due, project into current cycle window
                lastCycleStartDate.plusDays((((cycleDay / avgCycleLength) + 1) * avgCycleLength).toLong())
            } else {
                expected
            }
        }
        val nextPeriodEnd = nextPeriodStart.plusDays((avgPeriodLength - 1).toLong())

        val daysLeft = ChronoUnit.DAYS.between(today, nextPeriodStart).toInt().coerceAtLeast(0)

        // 5. Ovulation & Fertile Window
        // Luteal phase is typically ~14 days before next period
        val ovulationDate = nextPeriodStart.minusDays(14)
        val fertileStart = ovulationDate.minusDays(4)
        val fertileEnd = ovulationDate.plusDays(1)

        // 6. Current Phase & Fertility
        val (phase, fertility) = when {
            isOngoing || (mostRecent != null && mostRecent.contains(today)) || cycleDay <= avgPeriodLength -> {
                Pair(CyclePhase.MENSTRUAL, FertilityLevel.LOW)
            }
            today.isEqual(ovulationDate) -> {
                Pair(CyclePhase.OVULATION, FertilityLevel.PEAK)
            }
            !today.isBefore(fertileStart) && !today.isAfter(fertileEnd) -> {
                Pair(CyclePhase.OVULATION, FertilityLevel.HIGH)
            }
            today.isBefore(ovulationDate) -> {
                Pair(CyclePhase.FOLLICULAR, if (today.isAfter(fertileStart.minusDays(2))) FertilityLevel.MEDIUM else FertilityLevel.LOW)
            }
            else -> {
                Pair(CyclePhase.LUTEAL, FertilityLevel.LOW)
            }
        }

        // 7. Regularity
        val regularity = when {
            cycleLengths.size < 2 -> "Regular"
            cycleLengths.maxOrNull()!! - cycleLengths.minOrNull()!! <= 3 -> "Regular"
            cycleLengths.maxOrNull()!! - cycleLengths.minOrNull()!! <= 6 -> "Mild Variation"
            else -> "Irregular"
        }

        return CycleInfo(
            currentCycleDay = cycleDay,
            daysUntilNextPeriod = daysLeft,
            currentPhase = phase,
            fertilityLevel = fertility,
            averageCycleLength = avgCycleLength,
            averagePeriodLength = avgPeriodLength,
            isPeriodOngoing = isOngoing,
            ongoingDays = ongoingPeriod?.durationDays,
            nextPeriodStartDate = nextPeriodStart,
            nextPeriodEndDate = nextPeriodEnd,
            ovulationDate = ovulationDate,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            cycleRegularity = regularity,
            recordedCyclesCount = sortedPeriods.size
        )
    }

    /**
     * Map each period id to its cycle length (days from its start to previous recorded start).
     */
    fun calculateCycleLengthsMap(periods: List<PeriodRecord>): Map<Long, Int> {
        val sorted = periods.sortedBy { it.startDate }
        val map = mutableMapOf<Long, Int>()
        for (i in 1 until sorted.size) {
            val curr = sorted[i]
            val prev = sorted[i - 1]
            val diff = ChronoUnit.DAYS.between(prev.startDate, curr.startDate).toInt()
            map[curr.id] = diff
        }
        return map
    }
}
