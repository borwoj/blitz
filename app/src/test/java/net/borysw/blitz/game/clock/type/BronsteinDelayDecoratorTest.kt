package net.borysw.blitz.game.clock.type

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BronsteinDelayDecoratorTest {

    @Test
    fun setDelayAndIncrement() {
        val chessClock = mock<ChessClock>()
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.delayAndIncrement = 1

        verify(delayTimer1).initialTime = 1
        verify(delayTimer2).initialTime = 1
    }

    @Test
    fun initialTimeIllegal() {
        val chessClock = mock<ChessClock>()
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        assertThrows<IllegalStateException> { testedObj.initialTime = 1 }
    }

    @Test
    fun initialTime() {
        val chessClock = mock<ChessClock>()
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.delayAndIncrement = 1
        testedObj.initialTime = 1

        verify(chessClock).initialTime = 2
    }

    @Test
    fun getCurrentPlayer() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(null)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        assertNull(testedObj.currentPlayer)
    }

    @Test
    fun getRemainingTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(1)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        assertEquals(1, testedObj.remainingTimePlayer1)
    }

    @Test
    fun getRemainingTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer2).thenReturn(1)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        assertEquals(1, testedObj.remainingTimePlayer2)
    }

    @Test
    fun isTimeOver() {
        val chessClock = mock<ChessClock> {
            on(it.isTimeOver).thenReturn(true)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        assertTrue(testedObj.isTimeOver)
    }

    @Test
    fun isPaused() {
        val chessClock = mock<ChessClock> {
            on(it.isPaused).thenReturn(true)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        assertTrue(testedObj.isPaused)
    }

    @Test
    fun changeTurnFirstTime() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(1)
            on(it.remainingTimePlayer2).thenReturn(1)
            on(it.initialTime).thenReturn(1)
        }
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.changeTurn(Player1)

        verify(chessClock).changeTurn(Player1)
        verify(delayTimer1, never()).addTime(any())
        verify(delayTimer2, never()).addTime(any())
    }

    @Test
    fun changeTurnAddTimePlayer1() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(2)
            on(it.remainingTimePlayer2).thenReturn(1)
            on(it.initialTime).thenReturn(1)
            on(it.currentPlayer).thenReturn(Player2)
        }
        val delayTimer1 = mock<Timer> {
            on(it.remainingTime).thenReturn(0)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.delayAndIncrement = 10
        testedObj.changeTurn(Player1)

        verify(chessClock).changeTurn(Player1)
        verify(chessClock).addTimePlayer1(10)
        verify(delayTimer1).reset()
    }

    @Test
    fun changeTurnAddTimePlayer2() {
        val chessClock = mock<ChessClock> {
            on(it.remainingTimePlayer1).thenReturn(2)
            on(it.remainingTimePlayer2).thenReturn(1)
            on(it.initialTime).thenReturn(1)
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer>()
        val delayTimer2 = mock<Timer> {
            on(it.remainingTime).thenReturn(4)
        }
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.delayAndIncrement = 10
        testedObj.changeTurn(Player2)

        verify(chessClock).changeTurn(Player2)
        verify(chessClock).addTimePlayer2(6)
        verify(delayTimer2).reset()
    }

    @Test
    fun advanceTimeDelay() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(false)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.advanceTime()

        verify(delayTimer1).advanceTime()
        verify(chessClock).advanceTime()
        verify(delayTimer2, never()).advanceTime()
    }

    @Test
    fun advanceTimeDelayOver() {
        val chessClock = mock<ChessClock> {
            on(it.currentPlayer).thenReturn(Player1)
        }
        val delayTimer1 = mock<Timer> {
            on(it.isTimeOver).thenReturn(true)
        }
        val delayTimer2 = mock<Timer>()
        val testedObj = BronsteinDelayDecorator(chessClock, delayTimer1, delayTimer2)

        testedObj.advanceTime()

        verify(delayTimer1, never()).advanceTime()
        verify(chessClock).advanceTime()
        verify(delayTimer2, never()).advanceTime()
    }

    @Test
    fun addTimePlayer1() {
        val chessClock = mock<ChessClock>()
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        testedObj.addTimePlayer1(1)

        verify(chessClock).addTimePlayer1(1)
        verify(chessClock, never()).addTimePlayer2(any())
    }

    @Test
    fun addTimePlayer2() {
        val chessClock = mock<ChessClock>()
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        testedObj.addTimePlayer2(1)

        verify(chessClock).addTimePlayer2(1)
        verify(chessClock, never()).addTimePlayer1(any())
    }

    @Test
    fun pause() {
        val chessClock = mock<ChessClock>()
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        testedObj.pause()

        verify(chessClock).pause()
    }

    @Test
    fun reset() {
        val chessClock = mock<ChessClock>()
        val testedObj = BronsteinDelayDecorator(chessClock, mock(), mock())

        testedObj.reset()

        verify(chessClock).reset()
    }
}