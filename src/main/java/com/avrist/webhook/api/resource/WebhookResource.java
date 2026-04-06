package com.avrist.webhook.api.resource;

import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.dto.ApiResponse;
import com.avrist.webhook.service.chatrecord.ChatRecordService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "WEBHOOK", description = "Application Webhook")
public class WebhookResource {

    @Inject
    private ChatRecordService chatRecordService;

    @POST
    @Path("")
    @Operation(summary = "Devbot Webhook Entry")
    public Response telegramDevBotWebhook(TelegramChatRecordDto request) {
        chatRecordService.execute(request);
        return Response.ok(ApiResponse.ok("received")).build();
    }
}
