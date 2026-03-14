package org.techsolution.webhook.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.techsolution.webhook.config.AppConfig;
import org.techsolution.webhook.dto.ApiResponse;

@Path("/webhook")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "WEBHOOK", description = "General Application Webhook")
public class WebhookResource {

    @Inject
    AppConfig appConfig;

    @POST
    @Path("/telegram/dev-bot")
    @Operation(summary = "Devbot Webhook Entry")
    public Response info() {
        return Response.ok(ApiResponse.ok("building...")).build();
    }
}
