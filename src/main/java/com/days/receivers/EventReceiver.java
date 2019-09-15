package com.days.receivers;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReceiver implements MessageReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(EventReceiver.class);

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {

        LOGGER.info("Message Id: " + message.getMessageId() + " Data: " + message.getData().toStringUtf8());

        consumer.ack();
    }
}
