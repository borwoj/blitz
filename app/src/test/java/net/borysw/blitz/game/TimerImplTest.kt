package net.borysw.blitz.game

import net.borysw.blitz.game.engine.clock.timer.TimerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TimerImplTest {

    @Test
    @DisplayName("should set initial time")
    fun initialTime() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }

        assertEquals(1, testedObj.initialTime)
    }

    @Test
    @DisplayName("when advancing time, should decrement remaining time")
    fun advance() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }
        testedObj.advanceTime()

        assertEquals(0, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when advancing time while time is already over, should throw exception")
    fun advanceIllegal() {
        val testedObj = TimerImpl()
            .apply { initialTime = 0 }

        assertThrows(IllegalStateException::class.java) { testedObj.advanceTime() }
    }

    @Test
    @DisplayName("when created, remaining time should be equal to initial time")
    fun getRemainingTime() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when reset, remaining time should be equal to initial time and elapsed time should be 0")
    fun reset() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }
        testedObj.advanceTime()
        testedObj.reset()

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when remaining time is 0, should return true")
    fun isTimeOverYes() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }
        testedObj.advanceTime()

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when remaining time is not 0, should return false")
    fun isTimeOverNo() {
        val testedObj = TimerImpl()
            .apply { initialTime = 1 }

        assertFalse(testedObj.isTimeOver)
    }
}