package com.avrist.webhook;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class WebhookResourceTest {

    @Test
    void testWebhookHealth() {
        given()
                .when().get("/api/webhook/health")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.name", is("AI Webhook"));
    }

    @Test
    void testWebhookReceive() {
        String payload = """
                {"type":"test","source":"junit","data":{"key":"value"}}
                """;
        given()
                .contentType("application/json")
                .body(payload)
                .when().post("/api/webhook/receive")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("processed"))
                .body("data.id", notNullValue());
    }

    @Test
    void testApiInfo() {
        given()
                .when().get("/api/info")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.name", is("AI Webhook"));
    }
}
