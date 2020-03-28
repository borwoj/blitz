package net.borysw.blitz.game.presentation

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.app.clock.ChessClockImpl
import net.borysw.blitz.app.clock.timer.Timer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ChessClockImplTest {

    @Test
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
    fun getRemainingTimePlayer1() {
        val timer1 = mock<Timer> {
            on(it.remainingTime).thenReturn(5)
        }
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(5, testedObj.remainingTimePlayer1)
    }

    @Test
    fun getRemainingTimePlayer2() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer> {
            on(it.remainingTime).thenReturn(5)
        }
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(5, testedObj.remainingTimePlayer2)
    }

    @Test
    fun getCurrentPlayer() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        assertEquals(null, testedObj.currentPlayer)

        testedObj.changeTurn(ChessClock.Player.FIRST)

        assertEquals(ChessClock.Player.FIRST, testedObj.currentPlayer)

        testedObj.changeTurn(ChessClock.Player.SECOND)

        assertEquals(ChessClock.Player.SECOND, testedObj.currentPlayer)
    }

    @Test
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
    fun isTimeOverOne() {
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
    fun advanceTimeIllegal() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        assertThrows<IllegalStateException> { testedObj.advanceTime() }
    }

    @Test
    fun advanceTimeFirst() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.FIRST)
        testedObj.advanceTime()

        verify(timer1).advanceTime()
        verify(timer2, never()).advanceTime()
    }

    @Test
    fun advanceTimeSecond() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.SECOND)
        testedObj.advanceTime()

        verify(timer1, never()).advanceTime()
        verify(timer2).advanceTime()
    }

    @Test
    fun onPaused() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.changeTurn(ChessClock.Player.SECOND)
        testedObj.onPaused()

        assertNull(testedObj.currentPlayer)
    }

    @Test
    fun reset() {
        val timer1 = mock<Timer>()
        val timer2 = mock<Timer>()
        val testedObj = ChessClockImpl(timer1, timer2)

        testedObj.reset()

        verify(timer1).reset()
        verify(timer2).reset()
    }
}