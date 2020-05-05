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

import java.util.Locale;
import java.util.Map;

public class BugSnagIntegrationFactory extends RudderIntegration<Client> {
    private static final String BUGSNAG_KEY = "Bugsnag";
    private Client bugSnagClient;

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
            bugSnagClient = new Client(client.getApplication(), apiKey);
            Bugsnag.init(client.getApplication(), apiKey);
        }
    }

    @Override
    public void reset() {
        bugSnagClient.clearBreadcrumbs();
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
                        bugSnagClient.leaveBreadcrumb(message.getEventName());
                        bugSnagClient.notify(new RuntimeException(message.getEventName()));
                    }
                    break;
                case MessageType.SCREEN:
                    if (message.getEventName() != null) {
                        bugSnagClient.leaveBreadcrumb(
                                String.format("Viewed %s Screen", message.getEventName())
                        );
                        bugSnagClient.notify(
                                new RuntimeException(String.format("Viewed %s Screen", message.getEventName()))
                        );
                    }
                    break;
                case MessageType.IDENTIFY:
                    Map<String, Object> traits = message.getTraits();
                    bugSnagClient.setUser(
                            message.getUserId(),
                            (String) traits.get("email"),
                            String.format(Locale.US, "%s %s", traits.get("firstname"), traits.get("lastname"))
                    );
                    for (Map.Entry<String, Object> entry : traits.entrySet()) {
                        bugSnagClient.addToTab("User", entry.getKey(), entry.getValue());
                    }
                    Bugsnag.notify(new RuntimeException((String) traits.get("name")));
                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }

    @Override
    public Client getUnderlyingInstance() {
        return bugSnagClient;
    }
}
