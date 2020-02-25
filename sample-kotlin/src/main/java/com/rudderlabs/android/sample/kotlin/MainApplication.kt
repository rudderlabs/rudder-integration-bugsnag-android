package com.rudderlabs.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.appsflyer.RudderAppsFlyerIntegration
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "1TSRSskqa15PG7F89tkwEbl5Td8"
        private const val END_POINT_URI = "https://7cfa36c2.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                .withEndPointUri(END_POINT_URI)
                .withFactory(RudderAppsFlyerIntegration.FACTORY)
                .withLogLevel(RudderLogger.RudderLogLevel.DEBUG)
                .build()
        )
    }
}