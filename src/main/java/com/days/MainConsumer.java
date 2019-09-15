package com.days;

import com.days.subscribers.EventSubscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainConsumer {

    private static final String PROJECT_ID = System.getenv("GCP_PROJECT");
    private static final String SUBSCRIPTION_ID = System.getenv("SUBSCRIPTION_ID");
    private static Logger LOGGER = LoggerFactory.getLogger(MainConsumer.class);


    public static void main(String[] args) {
        LOGGER.info("Start consumer");

        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, SUBSCRIPTION_ID);
        EventSubscriber sub = new EventSubscriber(subscriptionName);

        sub.run();

    }
}
