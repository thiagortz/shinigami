package com.days;

import com.days.publishers.EventPublisher;
import com.google.pubsub.v1.ProjectTopicName;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static spark.Spark.*;

public class MainPublisher {
    private static final String PROJECT_ID = System.getenv("GCP_PROJECT");
    private static final String TOPIC_ID = System.getenv("TOPIC_ID");

    public static void main(String[] args) {
        post("/publish", "application/json", MainPublisher::handle);
    }

    private static Object handle(Request req, Response res) {
        try {
            int total = 10000;

            ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);

            EventPublisher pub = new EventPublisher(topicName);

            String numberEvents = req.queryParams("numberEvents");

            if (numberEvents != null)
                total = Integer.parseInt(numberEvents);

            pub.publisher(total);

            return "OK";

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "NOT OK";
        }
    }
}
