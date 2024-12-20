package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
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

public class BulkDelete {
    public static void bulkDelete(String objectName) {
        try {
            Scanner sc = new Scanner(System.in);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("allOrNone", true);
            ArrayNode compositeRequestArray = mapper.createArrayNode();

            boolean takeInput = true;
            int ref = 1;
            while(takeInput) {
                System.out.println("1. Add a record to the delete list\n2. Start deleting");
                int input = sc.nextInt();
                sc.nextLine();
                switch (input) {
                    case 1:
                        System.out.print("Enter a record ID: ");
                        String recordId = sc.nextLine();
                        ObjectNode deleteRequest = mapper.createObjectNode();
                        compositeRequestArray.add(deleteRequestBuilder(deleteRequest, objectName, recordId, String.valueOf(ref++)));
                        break;
                    case 2:
                        takeInput = false;
                    default:
                        System.out.println("Enter a valid input");
                }
            }
            rootNode.set("compositeRequest", compositeRequestArray);
            String payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            sendDeleteRequest(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode deleteRequestBuilder(ObjectNode deleteRequest, String objectName, String recordId, String ref) {
        deleteRequest.put("method", "DELETE");
        deleteRequest.put("url", "/services/data/v62.0/sobjects/"+objectName+"/"+recordId);
        deleteRequest.put("referenceId", ref);
        return deleteRequest;
    }

    public static void sendDeleteRequest(String payload) {
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
                    throw new RuntimeException("Failed to delete! "+response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
