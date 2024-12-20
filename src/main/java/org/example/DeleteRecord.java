package org.example;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;

public class DeleteRecord {

    public static void deleteRecord(String objectName, String recordId) {
        String url = TokenAndUrlSetter.getInstanceUrl() + "/services/data/v57.0/sobjects/" + objectName +"/"+ recordId;

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete delete = new HttpDelete(url);
            delete.setHeader("Authorization", "Bearer " + TokenAndUrlSetter.getAccessToken());
            delete.setHeader("Content-Type", "application/json");

            try(CloseableHttpResponse response = client.execute(delete)) {
                if (response.getCode() == 204) {
                    System.out.println("Record "+recordId+" deleted successfully.");
                } else {
                    throw new RuntimeException("Failed to delete record"+recordId+" "+response.getCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
