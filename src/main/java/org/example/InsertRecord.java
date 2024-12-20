package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InsertRecord {
    public static void insertRecord(String objectName) {
        Scanner sc = new Scanner(System.in);
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/sobjects/" + objectName;
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            System.out.print("Enter book name: ");
            payload.put("book_name__c", sc.nextLine());
            String jsonPayload = mapper.writeValueAsString(payload);

            post.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonPayload));

            try(CloseableHttpResponse response = client.execute(post)) {
                if (response.getCode() == 201) {
                    Map<String, String> result = mapper.readValue(response.getEntity().getContent(), Map.class);
                    System.out.println(result);
                }
                else {
                    throw new RuntimeException("Failed to insert "+response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertRecord(String objectName, String bookName) {
        Scanner sc = new Scanner(System.in);
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/sobjects/" + objectName;
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            payload.put("book_name__c", bookName);
            String jsonPayload = mapper.writeValueAsString(payload);

            post.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonPayload));

            try(CloseableHttpResponse response = client.execute(post)) {
                if (response.getCode() == 201) {
                    Map<String, String> result = mapper.readValue(response.getEntity().getContent(), Map.class);
                    System.out.println(result);
                }
                else {
                    throw new RuntimeException("Failed to insert "+response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
