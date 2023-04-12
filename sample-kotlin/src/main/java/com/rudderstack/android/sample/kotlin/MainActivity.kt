package com.rudderstack.android.sample.kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rudderlabs.android.sample.kotlin.R
import com.rudderstack.android.sdk.core.RudderProperty
import com.rudderstack.android.sdk.core.RudderTraits
import com.rudderstack.android.sample.kotlin.MainApplication.Companion.rudderClient
import com.bugsnag.android.Bugsnag
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonTap(view: View) {
        when (view.id) {
            R.id.btn ->
                rudderClient.identify("test_userid_android")
            R.id.btn2 ->
                rudderClient.identify(
                    "test_userid_android",
                    RudderTraits()
                        .putBirthday(Date(631172471000))
                        .putAddress(RudderTraits.Address()
                            .putCity("Palo Alto")
                            .putCountry("USA"))
                        .putFirstName("First Name")
                        .putLastName("Last Name")
                        .putName("Rudder-Bugsnag Android")
                        .putGender("Male")
                        .putPhone("0123456789")
                        .putEmail("test@gmail.com")
                        .put("key-1", "value-1")
                        .put("key-2", 1234),
                    null
                )

            R.id.btn3 ->
                rudderClient.track("New Track event", RudderProperty()
                    .putValue("key_1", "value_1")
                    .putValue("key_2", "value_2"))
            R.id.btn4 ->
                rudderClient.screen("Home", RudderProperty()
                    .putValue("key_1", "value_1")
                    .putValue("key_2", "value_2"))
            R.id.btn5 ->
                Bugsnag.notify(RuntimeException("Test error"))
        }
    }
}
