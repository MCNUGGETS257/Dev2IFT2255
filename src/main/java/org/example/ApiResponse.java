package org.example;


public class ApiResponse {
    private final int statusCode;
    private final String message;
    private final String body;

    public ApiResponse(int statusCode, String message, String body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Status Code: " + statusCode + ", Message: " + message + ", Body: " + body;
    }
}
