package com.rudderstack.android.integrations.bugsnag;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bugsnag.android.Client;
import com.bugsnag.android.Bugsnag;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderContext;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.util.HashMap;
import java.util.Map;

public class BugSnagIntegrationFactory extends RudderIntegration<Void> {
    private static final String BUGSNAG_KEY = "Bugsnag";
    private boolean sendEvents = false;
    private Client bugSnagClient;

    public static RudderIntegration.Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(Object settings, RudderClient client, RudderConfig config) {
          //  Bugsnag.init(client.getApplication(),"0d238ef3b270109195e9b03d26d0b31c");
            return new BugSnagIntegrationFactory(settings, client, config);

        }

        @Override
        public String key() {
            return BUGSNAG_KEY;
        }
    };



  private BugSnagIntegrationFactory(@Nullable Object config, @NonNull RudderClient client, @NonNull RudderConfig rudderConfig) {



        Map<String, Object> configMap = (Map<String, Object>) config;


        if (configMap != null) {
          // bugSnagClient = new Client(client.getApplication(),(String) configMap.get("apiKey"));
        //    bugSnagClient = new Client(client.getApplication(),"0d238ef3b270109195e9b03d26d0b31c");
            Bugsnag.init(client.getApplication(),"0d238ef3b270109195e9b03d26d0b31c");


            }
    //  if (rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG) {

    //  }

        }
    @Override
    public void reset() {
        bugSnagClient.clearBreadcrumbs();
    }

    @Override
    public void dump(RudderMessage element) {
        try {
            if (this.sendEvents && element != null) {
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
                    String eventName = message.getEventName();
                    if (eventName != null) {
                        bugSnagClient.leaveBreadcrumb(eventName);
                        Bugsnag.leaveBreadcrumb(eventName);
                        Bugsnag.notify(new RuntimeException(eventName));
                    }
                    break;
                case MessageType.SCREEN:
                    ;
                    if (message.getEventName() != null) {
                        bugSnagClient.leaveBreadcrumb(String.format("Viewed %s Screen",message.getEventName()));
                        Bugsnag.leaveBreadcrumb(String.format("Viewed %s Screen",message.getEventName()));
                        Bugsnag.notify(new RuntimeException(String.format("Viewed %s Screen",message.getEventName())));
                    }
                    break;
                case MessageType.IDENTIFY:
                  Map <String,Object> traits = message.getTraits();

                    bugSnagClient.setUser(message.getUserId(),(String) traits.get("email"),(String) traits.get("name"));
                    Bugsnag.setUser(message.getUserId(),(String) traits.get("email"),(String) traits.get("name"));
                    for(Map.Entry<String, Object> entry : traits.entrySet()) {
                        bugSnagClient.addToTab("User",entry.getKey(),entry.getValue());
                        Bugsnag.addToTab("User",entry.getKey(),entry.getValue());
                    }
                    Bugsnag.notify(new RuntimeException((String) traits.get("name")));

                    break;
                default:
                    RudderLogger.logWarn("Message type is not supported");
            }
        }
    }



}
