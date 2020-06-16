package net.borysw.blitz.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalytics @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) :
    Analytics {
    override fun logEvent(name: String, bundle: Bundle?) {
        firebaseAnalytics.logEvent(name, bundle)
    }
}