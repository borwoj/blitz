package net.borysw.blitz.game

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.engine.clock.timer.Timer
import net.borysw.blitz.game.engine.clock.type.ChessClockImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ChessClockImplTest {

    @Test
    @DisplayName("when initial time set, should set initial time of clock and timers")
    fun getInitialTime() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.initialTime = 5

        verify(timer1).initialTime = 5
        verify(timer2).initialTime = 5
        assertEquals(5, testedObj.initialTime)
    }

    @Test
    @DisplayName("should return remaining time of timer 1")
    fun getRemainingTimePlayer1() {
        val timer1 = mock<Timer> {
            on(it.remainingTime).thenReturn(5)
        }
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(5, testedObj.remainingTimePlayer1)
    }

    @Test
    @DisplayName("should return remaining time of timer 2")
    fun getRemainingTimePlayer2() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer> {
            on(it.remainingTime).thenReturn(5)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(5, testedObj.remainingTimePlayer2)
    }

    @Test
    @DisplayName("should have no player set initially")
    fun getCurrentPlayerInitial() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(null, testedObj.currentPlayer)
    }

    @Test
    @DisplayName("should change turn to player 1")
    fun getCurrentPlayer1() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.Player1)

        assertEquals(ChessClock.Player.Player1, testedObj.currentPlayer)
    }

    @Test
    @DisplayName("should change turn to player 2")
    fun getCurrentPlayer2() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.Player2)

        assertEquals(ChessClock.Player.Player2, testedObj.currentPlayer)
    }

    @Test
    @DisplayName("when timers are not over, time should not be over")
    fun isTimeOverNone() {
        val timer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val timer2 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertFalse(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when timer 1 is over, clock time should be over")
    fun isTimeOver1() {
        val timer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val timer2 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when timer 2 is over, clock time should be over")
    fun isTimeOver2() {
        val timer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val timer2 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when both timers are over, clock time should be over")
    fun isTimeOverBoth() {
        val timer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val timer2 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("when time added to player 1, should add time to timer 1")
    fun addTimePlayer1() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.addTimePlayer1(1)

        verify(timer1).addTime(1)
        verify(timer2, never()).addTime(any())
    }

    @Test
    @DisplayName("when time added to player 2, should add time to timer 2")
    fun addTimePlayer2() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.addTimePlayer2(1)

        verify(timer2).addTime(1)
        verify(timer1, never()).addTime(any())
    }

    @Test
    @DisplayName("when initial time not set and advancing time, should throw exception")
    fun advanceTimeIllegal() {
        val testedObj = ChessClockImpl(mock(), mock())

        assertThrows<IllegalStateException> { testedObj.advanceTime() }
    }

    @Test
    @DisplayName("when advancing time and player 1 turn, should advance time of timer 1")
    fun advanceTimeFirst() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.Player1)
        testedObj.advanceTime()

        verify(timer1).advanceTime()
        verify(timer2, never()).advanceTime()
    }

    @Test
    @DisplayName("when advancing time and player 2 turn, should advance time of timer 2")
    fun advanceTimeSecond() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.Player2)
        testedObj.advanceTime()

        verify(timer1, never()).advanceTime()
        verify(timer2).advanceTime()
    }

    @Test
    @DisplayName("when paused, no player should be set")
    fun onPaused() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.Player2)
        testedObj.pause()

        assertNull(testedObj.currentPlayer)
    }

    @Test
    @DisplayName("when reset, should reset both timers")
    fun reset() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.reset()

        verify(timer1).reset()
        verify(timer2).reset()
    }
}