package com.rudderstack.android.integrations.leanplum;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderContext;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.util.Map;

public class LeanPlumIntegrationFactory extends RudderIntegration<Void> {
    private static final String LEANPLUM_KEY = "Leanplum";

    public static RudderIntegration.Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig config) {
            return new LeanPlumIntegrationFactory(settings, client, config);
        }

        @Override
        public String key() {
            return LEANPLUM_KEY;
        }
    };

    private LeanPlumIntegrationFactory(Object config, RudderClient client, RudderConfig rudderConfig) {
        Leanplum.setApplicationContext(client.getApplication());
        if (client.getApplication() != null) {
            LeanplumActivityHelper.enableLifecycleCallbacks(client.getApplication());
        }

        Map<String, Object> configMap = (Map<String, Object>) config;
        if (configMap != null) {
            Boolean isDevelop = (Boolean) configMap.get("isDevelop");
            String appId = (String) configMap.get("applicationId");
            String clientKey = (String) configMap.get("clientKey");
            if (isDevelop != null && appId != null && clientKey != null) {
                if (isDevelop) {
                    Leanplum.setAppIdForDevelopmentMode(appId, clientKey);
                } else {
                    Leanplum.setAppIdForProductionMode(appId, clientKey);
                }
            }
            if (rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG) {
                Leanplum.enableVerboseLoggingInDevelopmentMode();
            }

            RudderContext context = client.getRudderContext();
            String userId = null;
            if (context != null && context.getTraits() != null) {
                Map<String, Object> traits = context.getTraits();
                if (traits.containsKey("userId")) {
                    userId = (String) traits.get("userId");
                } else if (traits.containsKey("id")) {
                    userId = (String) traits.get("id");
                }
            }
            if (userId != null) {
                Leanplum.start(client.getApplication(), userId);
            } else {
                Leanplum.start(client.getApplication());
            }
        }
    }

    private void processEvents(RudderMessage message) {
        String eventType = message.getType();
        if (eventType != null) {
            switch (eventType) {
                case MessageType.TRACK:
                case MessageType.SCREEN:
                    String eventName = message.getEventName();
                    if (eventName != null) {
                        Leanplum.track(eventName, message.getProperties());
                    }
                    break;
                case MessageType.IDENTIFY:
                    Leanplum.setUserId(message.getUserId());
                    Leanplum.setUserAttributes(message.getTraits());
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    @Override
    public void reset() {
        Leanplum.clearUserContent();
    }

    @Override
    public void dump(RudderMessage element) {
        if (element != null) {
            this.processEvents(element);
        }
    }
}
