package net.borysw.blitz.game

import net.borysw.blitz.app.clock.timer.Timer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TimerTest {

    @Test
    @DisplayName("should set initial time")
    fun initialTime() {
        val testedObj = Timer()
            .apply { initialTime = 1 }

        assertEquals(1, testedObj.initialTime)
    }

    @Test
    @DisplayName("when advancing time, should decrement remaining time")
    fun advance() {
        val testedObj = Timer()
            .apply { initialTime = 1 }
        testedObj.advance()

        assertEquals(0, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when advancing time by value, should subtract this value from remaining time")
    fun advanceBy() {
        val testedObj = Timer()
            .apply { initialTime = 3 }
        testedObj.advanceBy(2)

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when advancing time while time is already over, should throw exception")
    fun advanceIllegal() {
        val testedObj = Timer()
            .apply { initialTime = 0 }

        assertThrows(IllegalStateException::class.java) { testedObj.advance() }
    }

    @Test
    @DisplayName("when advancing time by value and this value is greater than remaining time, should throw exception")
    fun advanceByIllegal() {
        val testedObj = Timer()
            .apply { initialTime = 0 }

        assertThrows(IllegalStateException::class.java) { testedObj.advanceBy(1) }
    }

    @Test
    @DisplayName("when advancing time, should increment elapsed time")
    fun elapsedTime() {
        val testedObj = Timer()
            .apply { initialTime = 1 }
        testedObj.advance()

        assertEquals(1, testedObj.elapsedTime)
    }

    @Test
    @DisplayName("when advancing time by value, should add this value to elapsed time")
    fun elapsedTimeAdvanceBy() {
        val testedObj = Timer()
            .apply { initialTime = 2 }
        testedObj.advanceBy(2)

        assertEquals(2, testedObj.elapsedTime)
    }

    @Test
    @DisplayName("when created, remaining time should be equal to initial time")
    fun getRemainingTime() {
        val testedObj = Timer()
            .apply { initialTime = 1 }

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when reset, remaining time should be equal to initial time and elapsed time should be 0")
    fun reset() {
        val testedObj = Timer()
            .apply { initialTime = 1 }
        testedObj.advance()
        testedObj.reset()

        assertEquals(1, testedObj.remainingTime)
        assertEquals(0, testedObj.elapsedTime)
    }

    @Test
    @DisplayName("should calculate progress as a relation of elapsed time to sum of elapsed time and remaining time")
    fun progress() {
        val testedObj = Timer()
            .apply { initialTime = 3 }
        testedObj.advanceBy(2)
        testedObj.addTime(1)

        assertEquals(0.5f, testedObj.progress)
    }

    @Test
    @DisplayName("when adding time, should increase remaining time but not initial time")
    fun addTime() {
        val testedObj = Timer()
            .apply { initialTime = 1 }
        testedObj.addTime(2)

        assertEquals(3, testedObj.remainingTime)
        assertEquals(1, testedObj.initialTime)
    }

    @Test
    @DisplayName("when remaining time is 0, should return true")
    fun isTimeOverYes() {
        val testedObj = Timer()
            .apply { initialTime = 1 }
        testedObj.advance()

        assertTrue(testedObj.isTimeOver())
    }

    @Test
    @DisplayName("when remaining time is not 0, should return false")
    fun isTimeOverNo() {
        val testedObj = Timer()
            .apply { initialTime = 1 }

        assertFalse(testedObj.isTimeOver())
    }
}