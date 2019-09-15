package com.days.publishers;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventPublisher {

    private ProjectTopicName topicName;
    private Publisher publisher;
    private static Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);


    public  EventPublisher(ProjectTopicName topicName){
        this.topicName = topicName;
    }

    public void publisher(int messageCount) throws IOException, ExecutionException, InterruptedException {

        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            LOGGER.info("publishing the total of " + messageCount +" messages");

            for (int i = 0; i < messageCount; i++) {
                String message = "message-" + i;

                // convert message to bytes
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                        .setData(data)
                        .build();

                // Schedule a message to be published. Messages are automatically batched.
                ApiFuture<String> future = publisher.publish(pubsubMessage);
                futures.add(future);
            }
        } finally {
            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                System.out.println(messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
            }
        }

    }
}