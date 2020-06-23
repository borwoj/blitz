package net.borysw.blitz.game.clock.type

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class SimpleDelayDecoratorTest {

    @Test
    @DisplayName("when delay set, should set that value as initial time of both delay timers")
    fun setDelayAndIncrement() {
        val chessClock = mock<ChessClock>()
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.delay = 1

        assertEquals(1, testedObj.delay)
        verify(delayTimer1).initialTime = 1
        verify(delayTimer2).initialTime = 1
    }

    @Test
    @DisplayName("when initial time set, should set it on chess clock")
    fun setInitialTime() {
        val chessClock = mock<ChessClock>()
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        testedObj.initialTime = 1

        verify(chessClock).initialTime = 1
    }

    @Test
    @DisplayName("should return initial time of chess clock")
    fun getInitialTime() {
        val chessClock = mock<ChessClock> {
            on(it.initialTime).thenReturn(1)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertEquals(1, testedObj.initialTime)
    }

    @Test
    @DisplayName("should return initial time")
    fun getCurrentPlayer() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(null)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertNull(testedObj.currentPlayer)
    }

    @Test
    @DisplayName("should return remaining time of player 1")
    fun getRemainingTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(1)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertEquals(1, testedObj.remainingTimePlayer1)
    }

    @Test
    @DisplayName("should return remaining time of player 2")
    fun getRemainingTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer2).thenReturn(1)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertEquals(1, testedObj.remainingTimePlayer2)
    }

    @Test
    @DisplayName("should return is time over")
    fun isTimeOver() {
        val chessClock = mock<ChessClock> {
            on(it.isTimeOver).thenReturn(true)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    @DisplayName("should return pause status")
    fun isPaused() {
        val chessClock = mock<ChessClock> {
            on(it.isPaused).thenReturn(true)
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertTrue(testedObj.isPaused)
    }

    @Test
    @DisplayName("should return clock status")
    fun status() {
        val chessClock = mock<ChessClock> {
            on(it.status).thenReturn(ClockStatus(3, 2, 1, Player1))
        }
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        assertEquals(ClockStatus(3, 2, 1, Player1), testedObj.status)
    }

    @Test
    @DisplayName("when turn changed and current player is 1, should change turn and reset player 1 delay timer")
    fun changeTurnAddTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player2).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.changeTurn(Player1)

        verify(chessClock).changeTurn(Player1)
        verify(delayTimer1).reset()
        verify(delayTimer2, never()).reset()
    }

    @Test
    @DisplayName("when turn changed and current player is 2, should change turn and reset player 2 delay timer")
    fun changeTurnAddTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1).thenReturn(Player2)
        }
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.changeTurn(Player2)

        verify(chessClock).changeTurn(Player2)
        verify(delayTimer2).reset()
        verify(delayTimer1, never()).reset()
    }

    @Test
    @DisplayName("when turn changed to player 1 and current player is 1, should not change turn and perform any actions")
    fun changeSamePlayer() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.changeTurn(Player1)

        verify(chessClock, never()).changeTurn(any())
        verify(delayTimer1, never()).reset()
        verify(delayTimer2, never()).reset()
    }

    @Test
    @DisplayName("when time advanced and current player is 1 and delay time is not over, should advance player 1 delay time only")
    fun advanceTimeDelayPlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.advanceTime()

        verify(delayTimer1).advanceTime()
        verify(chessClock, never()).advanceTime()
        verify(delayTimer2, never()).advanceTime()
    }

    @Test
    @DisplayName("when time advanced and current player is 2 and delay time is not over, should advance player 2 delay time only")
    fun advanceTimeDelayPlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player2)
        }
        val delayTimer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.advanceTime()

        verify(delayTimer2).advanceTime()
        verify(chessClock, never()).advanceTime()
        verify(delayTimer1, never()).advanceTime()
    }

    @Test
    @DisplayName("when time advanced and delay time is over, should advance chess clock time only")
    fun advanceTimeDelayOver() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = SimpleDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.advanceTime()

        verify(delayTimer1, never()).advanceTime()
        verify(chessClock).advanceTime()
        verify(delayTimer2, never()).advanceTime()
    }

    @Test
    @DisplayName("should add time to player 1")
    fun addTimePlayer1() {
        val chessClock = mock<ChessClock>()
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        testedObj.addTimePlayer1(1)

        verify(chessClock).addTimePlayer1(1)
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    @DisplayName("should add time to player 2")
    fun addTimePlayer2() {
        val chessClock = mock<ChessClock>()
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        testedObj.addTimePlayer2(1)

        verify(chessClock).addTimePlayer2(1)
        verify(chessClock, never()).addTimePlayer1(any())
    }

    @Test
    @DisplayName("should pause chess clock")
    fun pause() {
        val chessClock = mock<ChessClock>()
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        testedObj.pause()

        verify(chessClock).pause()
    }

    @Test
    @DisplayName("should reset chess clock")
    fun reset() {
        val chessClock = mock<ChessClock>()
        val testedObj = SimpleDelayDecorator(chessClock, mock(), mock())

        testedObj.reset()

        verify(chessClock).reset()
    }
}