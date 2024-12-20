package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetObjectData {

    public static String getCustomObject(String objectName, String recordId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/sobjects/" + objectName + "/" + recordId;
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            get.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(get)) {
                if (response.getCode() == 200) {
                    CustomObjectGetAndSet objectGetAndSet = new CustomObjectGetAndSet();
                    mapObject(objectGetAndSet, response);

                    return "Id: " + objectGetAndSet.getId()
                            + "\nOwnerId: " + objectGetAndSet.getOwnerId()
                            + "\nName: " + objectGetAndSet.getName()
                            + "\nIs Deleted: " + objectGetAndSet.isDeleted()
                            + "\nCreated By Id: " + objectGetAndSet.getCreatedById()
                            + "\nCreated Date: " + objectGetAndSet.getCreatedDate();
                } else {
                    throw new RuntimeException("Failed to fetch object: " + response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getAllRecords() {
        String SOQLQuery = "SELECT Id, Name, book_name__c FROM Book__c";
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/query?q=" + URLEncoder.encode(SOQLQuery, StandardCharsets.UTF_8);

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            get.setHeader("Content-Type", "application/json");

            try(CloseableHttpResponse response = client.execute(get)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mapObject(CustomObjectGetAndSet objectGetAndSet, CloseableHttpResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
            System.out.println("Result: "+result);
            objectGetAndSet.setId((String) result.get("Id"));
            objectGetAndSet.setOwnerId((String) result.get("OwnerId"));
            objectGetAndSet.setDeleted((Boolean) result.get("IsDeleted"));
            objectGetAndSet.setName((String) result.get("Name"));
            objectGetAndSet.setCreatedById((String) result.get("CreatedById"));
            objectGetAndSet.setCreatedDate((String) result.get("CreatedDate"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

