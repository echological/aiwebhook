package com.avrist.webhook.api.resource;

import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.dto.ApiResponse;
import com.avrist.webhook.service.chatrecord.ChatRecordService;
import com.avrist.webhook.service.chatrecord.dto.ChatRecordRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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
    public Response telegramDevBotWebhook(
            TelegramChatRecordDto body,
            @HeaderParam("X-Telegram-Bot-Api-Secret-Token") String telegramApiKey) {
        chatRecordService.execute(ChatRecordRequest.builder()
                        .telegramApiKey(telegramApiKey)
                        .telegramChatRecordDto(body)
                .build());
        return Response.ok(ApiResponse.ok("received")).build();
    }
}
