package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.Map;

public class Authentication {
    public static void authenticate(String clientId, String clientSecret, String userName, String password) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(Credentials.LOGIN_URL);
            String payload = "grant_type=password&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&username=" + userName +
                    "&password=" + password;
            System.out.println(payload);
            post.setEntity(new StringEntity(payload));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            try(CloseableHttpResponse response = client.execute(post)) {
                if (response.getCode() == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> result = mapper.readValue(response.getEntity().getContent(), Map.class);

                    TokenAndUrlSetter.setAccessToken(result.get("access_token"));
                    TokenAndUrlSetter.setInstanceUrl(result.get("instance_url"));

                    System.out.println("Authentication successful!");
                } else {
                    System.out.println("Authentication failed. Try again!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
