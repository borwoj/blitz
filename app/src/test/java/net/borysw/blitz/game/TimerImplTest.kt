package net.borysw.blitz.game

import net.borysw.blitz.game.engine.clock.timer.TimerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// TODO display names
internal class TimerImplTest {
    private lateinit var testedObj: TimerImpl

    @BeforeEach
    fun setup() {
        testedObj = TimerImpl()
    }

    @Test
    fun getInitialTime() {
        testedObj.initialTime = 5

        assertEquals(5, testedObj.initialTime)
        assertEquals(5, testedObj.remainingTime)
    }

    @Test
    fun getRemainingTime() {
        testedObj.remainingTime = 5

        assertEquals(5, testedObj.remainingTime)
    }

    @Test
    fun isTimeOver() {
        testedObj.remainingTime = 0

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    fun isTimeOverNot() {
        testedObj.remainingTime = 5

        assertFalse(testedObj.isTimeOver)
    }

    @Test
    fun advanceTime() {
        testedObj.initialTime = 5

        testedObj.advanceTime()

        assertEquals(4, testedObj.remainingTime)
    }

    @Test
    fun addTime() {
        testedObj.remainingTime = 0

        testedObj.addTime(1)

        assertEquals(1, testedObj.remainingTime)
    }

    @Test
    fun reset() {
        testedObj.initialTime = 5
        testedObj.advanceTime()

        testedObj.reset()

        assertEquals(5, testedObj.remainingTime)
    }
}