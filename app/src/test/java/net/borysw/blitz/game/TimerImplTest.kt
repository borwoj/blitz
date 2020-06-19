package net.borysw.blitz.game

import net.borysw.blitz.game.clock.timer.TimerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TimerImplTest {
    private lateinit var testedObj: TimerImpl

    @BeforeEach
    fun setup() {
        testedObj = TimerImpl()
    }

    @Test
    @DisplayName("when initial time set, initial time and remaining time should be equal")
    fun getInitialTime() {
        testedObj.initialTime = 1

        assertEquals(1, testedObj.initialTime)
        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("should set remaining time")
    fun getRemainingTime() {
        testedObj.remainingTime = 1

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when remaining time is 0, time should be over")
    fun isTimeOver() {
        testedObj.remainingTime = 0

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when remaining time is not 0, time should not be over")
    fun isTimeOverNot() {
        testedObj.remainingTime = 1

        assertFalse(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when time advanced, should decrement remaining time")
    fun advanceTime() {
        testedObj.initialTime = 1

        testedObj.advanceTime()

        assertEquals(0, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when time added, should increment remaining time")
    fun addTime() {
        testedObj.initialTime = 1

        testedObj.addTime(1)

        assertEquals(2, testedObj.remainingTime)
    }

    @Test
    @DisplayName("when reset, remaining time should be equal to initial time")
    fun reset() {
        testedObj.initialTime = 1
        testedObj.advanceTime()

        testedObj.reset()

        assertEquals(1, testedObj.remainingTime)
    }
}