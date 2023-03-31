package com.rudderstack.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.bugsnag.BugSnagIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "2NHVCL9JbmZGFA3RTNZf7hh0j7k"
        private const val DATA_PLANE_URL = "https://rudderstacz.dataplane.rudderstack.com"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                .withDataPlaneUrl(DATA_PLANE_URL)
                .withFactory(BugSnagIntegrationFactory.FACTORY)
                .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE)
                .build()
        )
    }
}