package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UpdateRecord {
    public static void updateRecord(String objectName, String recordId) {
        Scanner sc = new Scanner(System.in);
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/sobjects/" + objectName + "/" + recordId;
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch patch = new HttpPatch(url);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            payload.put("book_name__c", sc.nextLine());
            String jsonPayload = mapper.writeValueAsString(payload);
            System.out.println(jsonPayload);

            patch.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            patch.setHeader("Content-Type", "application/json");
            patch.setEntity(new StringEntity(jsonPayload));

            try(CloseableHttpResponse response = client.execute(patch)) {
                if (response.getCode() == 204) {
                    System.out.println("Updated value successfully.");
                } else {
                    throw new RuntimeException("Failed to update "+response.getCode());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
