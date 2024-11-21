package org.example;


import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpClientApi {
    private static final String BASE_URL = "https://donnees.montreal.ca/api/3/action/datastore_search";
    private final HttpClient client;

    public HttpClientApi() {
        this.client = HttpClient.newHttpClient();
    }

    public ApiResponse getData(String resourceId) {
        try {
            // Build URI with encoded query parameters
            String encodedResourceId = URLEncoder.encode(resourceId, StandardCharsets.UTF_8);

            URI uri = new URI(BASE_URL + "?resource_id=" + encodedResourceId);

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status and print the body if successful
            if (response.statusCode() == 200) {
                return new ApiResponse(response.statusCode(), "OK", response.body());
            } else {
                System.out.println("Error: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException | java.net.URISyntaxException e) {
            return new ApiResponse(500, "Internal Server Error", "Exception occurred: " + e.getMessage());
        }
    }
}

