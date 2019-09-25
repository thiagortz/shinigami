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


    public EventPublisher(ProjectTopicName topicName) {
        this.topicName = topicName;
    }

    public void publisher(int messageCount) throws IOException, ExecutionException, InterruptedException {

        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            LOGGER.info("publishing the total of " + messageCount + " messages");

            for (int i = 0; i < messageCount; i++) {
                String message = "{\"OperationType\": \"Authorization\", \"MtiCode\": 000, \"ProcessingCode\": \"00\"," +
                        " \"OperationStatus\": \"Used\", \"MessageAmount\": 564.48, " +
                        "\"OriginalMessageAmount\": 564.48, \"AccountType\": \"Debit\", \"TransactionDate\": \"000\", " +
                        "\"LocalDate\": \"000\", \"Currency\": 000, \"PosEntryMode\": \"IntegratedCircuit\"," +
                        " \"ResponseCode\": \"00\", \"AuthorizationCode\": \"430292\", \"IsSms\": 0, \"CardAcceptorTerminalCode\": \"0000\"," +
                        " \"InitiatorTransactionKey\": \"0e847d2b-59dc\", \"ConfirmationDate\": null, \"IssuerCountryCode\": 76, " +
                        "\"OperationKey\": \"d57dbdac\", \"TransactionKey\": \"69fca4a3\"," +
                        " \"MaskedPan\": \"be488238-cbff-4964-a233-01addb8c500d\", \"Bin\": \"dffb8691\", " +
                        "\"CardBrandName\": \"MasterCard\", \"ExpirationDate\": \"2203\", \"CardToken\": \"000\", " +
                        "\"InstallmentType\": \"Merchant\", \"NumberOfInstallments\": 6, \"CardAcceptorCode\": \"28398def\"," +
                        " \"CardAcceptorName\": \"ddeba4ec\", \"MccCode\": 742, \"CityName\": \"Bauru\"," +
                        " \"StateName\": \"S\\u00e3o Paulo\", \"CountryCode\": \"BR\", \"AcquiringInstitutionCode\": \"014788\"," +
                        " \"ForwardingInstitutionCode\": \"014788\", \"PaymentSchemeBusinessModel\": \"FullAcquirer\", " +
                        "\"MerchantKey\": \"c79cf47c\", \"MerchantTaxId\": \"9409122c\"," +
                        " \"PointOfInteractionKey\": \"713e4026\", " +
                        "\"OrderIdentification\": null, \"SettlementDate\": 000}\n" + " Count: " + i;

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