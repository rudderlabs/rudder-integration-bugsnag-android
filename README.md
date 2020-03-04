[ ![Download](https://api.bintray.com/packages/rudderstack/rudderstack/leanplum/images/download.svg?version=0.1.0-beta.3) ](https://bintray.com/rudderstack/rudderstack/leanplum/0.1.0-beta.3/link)

# What is Rudder?

**Short answer:** 
Rudder is an open-source Segment alternative written in Go, built for the enterprise.

**Long answer:** 
Rudder is a platform for collecting, storing and routing customer event data to dozens of tools. Rudder is open-source, can run in your cloud environment (AWS, GCP, Azure or even your data-centre) and provides a powerful transformation framework to process your event data on the fly.

Released under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Getting Started with LeanPlum Integration of Android SDK
1. Add [LeanPlum](https://www.leanplum.com) as a destination in the [Dashboard](https://app.rudderlabs.com/) and define `applicationId`, and `clientKey`. If you turn on the Development Environment flag, make sure to put your development key in `clientKey`. We suggest to create two different destinations for Production and Development. If you want to send events only through the `device-mode`, turn on the `Use native SDK to send user generated events` flag.

2. Add these lines to your ```app/build.gradle```
```
repositories {
    maven { url "https://dl.bintray.com/rudderstack/rudderstack" }
}
```
3. Add the dependency under ```dependencies```
```
// Rudder core sdk and leanplum extension
implementation 'com.rudderstack.android.sdk:core:1.0.1-beta.1'
implementation 'com.rudderstack.android.integration:leanplum:0.1.0-beta.3'

// leanplum core sdk
implementation 'com.leanplum:leanplum-core:5.3.3'

// gson
implementation 'com.google.code.gson:gson:2.8.6'
```

## Initialize ```RudderClient```
```
val rudderClient: RudderClient = RudderClient.getInstance(
    this,
    <WRITE_KEY>,
    RudderConfig.Builder()
        .withEndPointUri(<END_POINT_URI>)
        .withFactory(LeanPlumIntegrationFactory.FACTORY)
        .build()
)
```

## Send Events
Follow the steps from [Rudder Android SDK](https://github.com/rudderlabs/rudder-sdk-android)

## Contact Us
If you come across any issues while configuring or using RudderStack, please feel free to [contact us](https://rudderstack.com/contact/) or start a conversation on our [Discord](https://discordapp.com/invite/xNEdEGw) channel. We will be happy to help you.
