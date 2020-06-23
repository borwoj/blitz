package net.borysw.blitz.game.clock.type

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class FischerDecoratorTest {

    @Test
    @DisplayName("should set increment value")
    fun setDelayAndIncrement() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.incrementBy = 1

        assertEquals(1, testedObj.incrementBy)
    }

    @Test
    @DisplayName("when initial time set but increment value not set, should throw exception")
    fun initialTimeIllegal() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        assertThrows<IllegalStateException> { testedObj.initialTime = 1 }
    }

    @Test
    @DisplayName("when initial time set, should set initial time of chess clock to sum of increment value and initial time")
    fun setInitialTime() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.incrementBy = 1
        testedObj.initialTime = 1

        verify(chessClock).initialTime = 2
    }

    @Test
    @DisplayName("should return initial time of chess clock")
    fun getInitialTime() {
        val chessClock = mock<ChessClock> {
            on(it.initialTime).thenReturn(1)
        }
        val testedObj = FischerDecorator(chessClock)

        assertEquals(1, testedObj.initialTime)
    }

    @Test
    @DisplayName("should return initial time")
    fun getCurrentPlayer() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(null)
        }
        val testedObj = FischerDecorator(chessClock)

        assertNull(testedObj.currentPlayer)
    }

    @Test
    @DisplayName("should return remaining time of player 1")
    fun getRemainingTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(1)
        }
        val testedObj = FischerDecorator(chessClock)

        assertEquals(1, testedObj.remainingTimePlayer1)
    }

    @Test
    @DisplayName("should return remaining time of player 2")
    fun getRemainingTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer2).thenReturn(1)
        }
        val testedObj = FischerDecorator(chessClock)

        assertEquals(1, testedObj.remainingTimePlayer2)
    }

    @Test
    @DisplayName("should return is time over")
    fun isTimeOver() {
        val chessClock = mock<ChessClock> {
            on(it.isTimeOver).thenReturn(true)
        }
        val testedObj = FischerDecorator(chessClock)

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("should return pause status")
    fun isPaused() {
        val chessClock = mock<ChessClock> {
            on(it.isPaused).thenReturn(true)
        }
        val testedObj = FischerDecorator(chessClock)

        assertTrue(testedObj.isPaused)
    }

    @Test
    @DisplayName("should return clock status")
    fun status() {
        val chessClock = mock<ChessClock> {
            on(it.status).thenReturn(ClockStatus(3, 2, 1, Player1))
        }
        val testedObj = FischerDecorator(chessClock)

        assertEquals(ClockStatus(3, 2, 1, Player1), testedObj.status)
    }

    @Test
    @DisplayName("when turn changed for the first time, should change turn and not add any time")
    fun changeTurnFirstTime() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(1)
            on(it.remainingTimePlayer2).thenReturn(1)
            on(it.initialTime).thenReturn(1)
        }
        val testedObj = FischerDecorator(chessClock)

        testedObj.changeTurn(Player1)

        verify(chessClock).changeTurn(Player1)
        verify(chessClock, never()).addTimePlayer1(any())
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    @DisplayName("when turn changed and current player is 1, should change turn and add time to player 2")
    fun changeTurnAddTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player2).thenReturn(Player1)
        }
        val testedObj = FischerDecorator(chessClock)

        testedObj.incrementBy = 1
        testedObj.changeTurn(Player1)

        verify(chessClock).changeTurn(Player1)
        verify(chessClock).addTimePlayer2(1)
        verify(chessClock, never()).addTimePlayer1(any())
    }

    @Test
    @DisplayName("when turn changed and current player is 2, should change turn and add time to player 1")
    fun changeTurnAddTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1).thenReturn(Player2)
        }
        val testedObj = FischerDecorator(chessClock)

        testedObj.incrementBy = 1
        testedObj.changeTurn(Player2)

        verify(chessClock).changeTurn(Player2)
        verify(chessClock).addTimePlayer1(1)
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    @DisplayName("when turn changed to player 1 and current player is 1, should not change turn and perform any actions")
    fun changeSamePlayer() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val testedObj = FischerDecorator(chessClock)

        testedObj.changeTurn(Player1)

        verify(chessClock, never()).changeTurn(any())
        verify(chessClock, never()).addTimePlayer1(any())
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    @DisplayName("when time advanced, should advance chess clock time")
    fun advanceTime() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.advanceTime()

        verify(chessClock).advanceTime()
    }

    @Test
    @DisplayName("should add time to player 1")
    fun addTimePlayer1() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.addTimePlayer1(1)

        verify(chessClock).addTimePlayer1(1)
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    @DisplayName("should add time to player 2")
    fun addTimePlayer2() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.addTimePlayer2(1)

        verify(chessClock).addTimePlayer2(1)
        verify(chessClock, never()).addTimePlayer1(any())
    }

    @Test
    @DisplayName("should pause chess clock")
    fun pause() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.pause()

        verify(chessClock).pause()
    }

    @Test
    @DisplayName("should reset chess clock")
    fun reset() {
        val chessClock = mock<ChessClock>()
        val testedObj = FischerDecorator(chessClock)

        testedObj.reset()

        verify(chessClock).reset()
    }
}