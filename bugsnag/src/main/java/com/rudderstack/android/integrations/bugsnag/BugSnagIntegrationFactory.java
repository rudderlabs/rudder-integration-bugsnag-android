package com.rudderstack.android.integrations.bugsnag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bugsnag.android.Client;
import com.bugsnag.android.Bugsnag;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.util.Map;

public class BugSnagIntegrationFactory extends RudderIntegration<Client> {
    private static final String BUGSNAG_KEY = "Bugsnag";

    public static RudderIntegration.Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig config) {
            return new BugSnagIntegrationFactory(settings, client);
        }

        @Override
        public String key() {
            return BUGSNAG_KEY;
        }
    };

    private BugSnagIntegrationFactory(@Nullable Object config, @NonNull RudderClient client) {
        Map<String, Object> configMap = (Map<String, Object>) config;

        if (configMap != null && client.getApplication() != null) {
            String apiKey = (String) configMap.get("apiKey");
            Bugsnag.start(client.getApplication(), apiKey);
        }
    }

    @Override
    public void reset() {
        Bugsnag.clearMetadata("User");
        Bugsnag.clearFeatureFlags();
    }

    @Override
    public void dump(@Nullable RudderMessage element) {
        try {
            if (element != null) {
                this.processEvents(element);
            }
        } catch (Exception e) {
            RudderLogger.logError(e);
        }
    }

    private void processEvents(@NonNull RudderMessage message) {
        String eventType = message.getType();
        if (eventType != null) {
            switch (eventType) {
                case MessageType.TRACK:
                    if (message.getEventName() != null) {
                        Bugsnag.leaveBreadcrumb(message.getEventName());
                    }
                    break;
                case MessageType.SCREEN:
                    if (message.getEventName() != null) {
                        Bugsnag.leaveBreadcrumb(String.format("Viewed %s Screen", message.getEventName()));
                    }
                    break;
                case MessageType.IDENTIFY:
                    Map<String, Object> traits = message.getTraits();
                    if (traits != null) {
                        String name = traits.containsKey("name") ? (String)traits.get("name") : null;
                        String email = traits.containsKey("email") ? (String)traits.get("email") : null;
                        Bugsnag.setUser(message.getUserId(), email, name);
                        for (Map.Entry<String, Object> entry : traits.entrySet()) {
                            Bugsnag.addMetadata("User", entry.getKey(), entry.getValue());
                        }
                    }
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    @Override
    public Client getUnderlyingInstance() {
        return Bugsnag.getClient();
    }
}
