package com.days.subscribers;

import com.days.receivers.EventReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class EventSubscriber {

    private ProjectSubscriptionName subscriptionName;
    private Subscriber subscriber;

    public EventSubscriber(ProjectSubscriptionName subscriptionName){
        this.subscriptionName = subscriptionName;
    }

    public void run(){

        // create a subscriber bound to the asynchronous message receiver
        subscriber = Subscriber.newBuilder(subscriptionName, new EventReceiver()).build();
        subscriber.startAsync().awaitRunning();
        // Allow the subscriber to run indefinitely unless an unrecoverable error occurs.
        subscriber.awaitTerminated();
    }
}