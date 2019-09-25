package com.days;

import com.days.publishers.EventPublisher;
import com.google.pubsub.v1.ProjectTopicName;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static spark.Spark.*;

public class MainPublisher {
    private static final String PROJECT_ID = System.getenv("GCP_PROJECT");
    private static final String TOPIC_ID = System.getenv("TOPIC_ID");

    public static void main(String[] args) {
        post("/publish", (req, res) -> {

            ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);

            EventPublisher pub = new EventPublisher(topicName);

            try {

                pub.publisher(10000);

            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return "OK";

        });
    }
}
