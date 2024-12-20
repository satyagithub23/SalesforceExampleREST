package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class BulkInsert {

    public static void bulkInsert(String objectName) {
        Scanner sc = new Scanner(System.in);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode compositeRequestArray = root.putArray("compositeRequest");

        boolean takeInput = true;
        int ref = 1;

        while (takeInput) {
            System.out.println("1. Add a book\n2. Start inserting");
            int input = sc.nextInt();
            sc.nextLine(); // Consume newline
            switch (input) {
                case 1:
                    System.out.print("Enter book name: ");
                    String bookName = sc.nextLine();
                    insertRequestBuilder(objectMapper, compositeRequestArray, objectName, bookName, String.valueOf(ref++));
                    break;
                case 2:
                    takeInput = false;
                    break;
                default:
                    System.out.println("Enter a valid input.");
            }
        }

        System.out.println("Generated Payload:");
        try {
            String jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            System.out.println("\nAre you sure you want to insert these records? (1. Yes / 2. No)");
            int confirmation = sc.nextInt();
            sc.nextLine(); // Consume newline
            if (confirmation == 1) {
                System.out.println("Records are being inserted...");
                sendInsertRequest(jsonPayload);
            } else {
                System.out.println("Records not inserted.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating payload JSON", e);
        }
    }

    public static void insertRequestBuilder(ObjectMapper objectMapper, ArrayNode compositeRequestArray, String objectName, String bookName, String ref) {
        try {
            ObjectNode insertRequest = compositeRequestArray.addObject();
            insertRequest.put("method", "POST");
            insertRequest.put("url", "/services/data/v62.0/sobjects/" + objectName);
            insertRequest.put("referenceId", ref);

            ObjectNode body = insertRequest.putObject("body");
            body.put("book_name__c", bookName);
        } catch (Exception e) {
            throw new RuntimeException("Error building insert request", e);
        }
    }

    public static void sendInsertRequest(String payload) {
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v62.0/composite";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            post.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payload));

            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getCode() == 200) {
                    Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
                    System.out.println(result);
                } else {
                    throw new RuntimeException("Failed to insert! "+response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
