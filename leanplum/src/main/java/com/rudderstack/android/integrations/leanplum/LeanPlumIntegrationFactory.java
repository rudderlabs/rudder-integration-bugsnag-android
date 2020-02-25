package com.rudderstack.android.integrations.leanplum;

import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.util.HashMap;
import java.util.Map;

public class LeanPlumIntegrationFactory extends RudderIntegration<Void> {
    private static final String LEANPLUM_KEY = "LeanPlum";
    private RudderClient rudderClient;

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
        this.rudderClient = client;
        Map<String, Object> destConfig = (Map<String, Object>) config;

    }

    private void processEvents(RudderMessage message) {
        String eventType = message.getType();
        String afEventName;
        Map<String, Object> afEventProps = new HashMap<>();
        if (eventType != null) {
            switch (eventType) {
                case MessageType.TRACK:
                    String eventName = message.getEventName();
                    if (eventName != null) {
                        Map<String, Object> property = message.getProperties();
                        if (property != null) {
                            switch (eventName) {
                                default:
                                    afEventName = eventName.toLowerCase().replace(" ", "_");
                            }
                        } else {
                            afEventName = eventName.toLowerCase().replace(" ", "_");
                        }

                    }
                    break;
                case MessageType.SCREEN:

                    break;
                case MessageType.IDENTIFY:
                    String userId = message.getUserId();

                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void dump(RudderMessage element) {
        if (element != null) {
            this.processEvents(element);
        }
    }
}
