package net.borysw.blitz.game.engine

import net.borysw.blitz.game.engine.userActions.UserAction.SitAtTable
import net.borysw.blitz.game.engine.userActions.UserActionsImpl
import org.junit.jupiter.api.Test

internal class UserActionsImplTest {

    @Test
    fun getUserActions() {
        val testedObj =
            UserActionsImpl()
        val testObserver = testedObj.userActions.test()

        testedObj.onUserAction(SitAtTable)

        testObserver.assertValue(SitAtTable).assertNotComplete()
    }
}