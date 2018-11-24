package net.borysw.blitz.game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TimerTest {

  @Test
  fun decrementTime() {
    val testedObj = Timer(1)
    testedObj.decrementTime()
    assertEquals(0, testedObj.getTimeLeft())
  }

  @Test
  fun getTimeLeft() {
    val testedObj = Timer(1)
    assertEquals(1, testedObj.getTimeLeft())
  }

  @Test
  fun addTime() {
    val testedObj = Timer(1)
    testedObj.addTime(2)
    assertEquals(3, testedObj.getTimeLeft())
  }

  @Test
  fun reset() {
    val testedObj = Timer(1)
    testedObj.decrementTime()
    testedObj.reset()
    assertEquals(1, testedObj.getTimeLeft())
  }

  @Test
  fun isTimeOver() {
    val testedObj = Timer(1)
    assertFalse(testedObj.isTimeOver())
    testedObj.decrementTime()
    assertTrue(testedObj.isTimeOver())
  }
}