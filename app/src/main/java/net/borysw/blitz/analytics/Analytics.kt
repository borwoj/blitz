package net.borysw.blitz.analytics

import android.os.Bundle

interface Analytics {
    fun logEvent(name: String, bundle: Bundle? = Bundle.EMPTY)
}