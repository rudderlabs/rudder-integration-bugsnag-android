package com.rudderlabs.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.leanplum.LeanPlumIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1XTOWiGgGaI4ImxlZAfF7TquLw7"
        private const val END_POINT_URI = "https://fee75804.ngrok.io"
        private const val CONFIG_PLANE_URL = "https://api.dev.rudderlabs.com"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                .withEndPointUri(END_POINT_URI)
                .withConfigPlaneUrl(CONFIG_PLANE_URL)
                .withFactory(LeanPlumIntegrationFactory.FACTORY)
                .withLogLevel(RudderLogger.RudderLogLevel.VERBOSE)
                .build()
        )
    }
}