package pl.amosialek.bloody

import pl.amosialek.bloody.data.model.FlowIntensity
import pl.amosialek.bloody.data.model.PeriodRecord
import pl.amosialek.bloody.domain.CycleCalculator
import pl.amosialek.bloody.domain.CyclePhase
import pl.amosialek.bloody.domain.FertilityLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CycleCalculatorTest {

    @Test
    fun testOngoingPeriodCalculation() {
        val today = LocalDate.now()
        val ongoingPeriod = PeriodRecord(
            id = 1,
            startDate = today.minusDays(2),
            endDate = null,
            flowIntensity = FlowIntensity.HEAVY
        )

        assertTrue(ongoingPeriod.isOngoing)
        assertTrue(ongoingPeriod.contains(today))
        assertTrue(ongoingPeriod.contains(today.minusDays(1)))
        assertFalse(ongoingPeriod.contains(today.plusDays(1)))

        val info = CycleCalculator.calculateCycleInfo(listOf(ongoingPeriod), today)
        assertTrue(info.isPeriodOngoing)
        assertEquals(CyclePhase.MENSTRUAL, info.currentPhase)
        assertEquals(FertilityLevel.LOW, info.fertilityLevel)
        assertEquals(3L, info.ongoingDays)
    }

    @Test
    fun testCycleLengthAndPredictions() {
        val today = LocalDate.of(2026, 10, 26)

        // Cycle 1: started 23 days ago, 5 days duration (today is Cycle Day 24, matching Stitch mockup!)
        val p1 = PeriodRecord(
            id = 3,
            startDate = today.minusDays(23),
            endDate = today.minusDays(19),
            flowIntensity = FlowIntensity.MEDIUM
        )
        // Cycle 2: started 29 days before Cycle 1
        val p2 = PeriodRecord(
            id = 2,
            startDate = p1.startDate.minusDays(29),
            endDate = p1.startDate.minusDays(25),
            flowIntensity = FlowIntensity.MEDIUM
        )
        // Cycle 3: started 28 days before Cycle 2
        val p3 = PeriodRecord(
            id = 1,
            startDate = p2.startDate.minusDays(28),
            endDate = p2.startDate.minusDays(24),
            flowIntensity = FlowIntensity.LIGHT
        )

        val periods = listOf(p1, p2, p3)
        val info = CycleCalculator.calculateCycleInfo(periods, today)

        assertEquals(24, info.currentCycleDay)
        assertEquals(5, info.daysUntilNextPeriod)
        assertEquals(CyclePhase.LUTEAL, info.currentPhase)
        assertEquals(FertilityLevel.LOW, info.fertilityLevel)
        assertEquals(28, info.averageCycleLength) // (29 + 28) / 2 = 28.5 -> 28
        assertEquals(5, info.averagePeriodLength)
        assertFalse(info.isPeriodOngoing)

        val lengthsMap = CycleCalculator.calculateCycleLengthsMap(periods)
        assertEquals(28, lengthsMap[p2.id])
        assertEquals(29, lengthsMap[p1.id])
    }

    @Test
    fun testPeriodRecordDuration() {
        val completed = PeriodRecord(
            id = 1,
            startDate = LocalDate.of(2026, 9, 15),
            endDate = LocalDate.of(2026, 9, 19),
            flowIntensity = FlowIntensity.MEDIUM
        )
        assertEquals(5L, completed.durationDays)
        assertFalse(completed.isOngoing)
    }
}
