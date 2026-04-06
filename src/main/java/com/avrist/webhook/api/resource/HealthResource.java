package com.avrist.webhook.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.avrist.webhook.dto.ApiResponse;
import com.avrist.webhook.config.AppConfig;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "API", description = "General API endpoints")
public class HealthResource {

    @Inject
    AppConfig appConfig;

    @GET
    @Path("/info")
    @Operation(summary = "App info")
    public Response info() {
        return Response.ok(ApiResponse.ok(
            new AppInfo(appConfig.getAppName(), appConfig.getAppVersion(), appConfig.getEnv())
        )).build();
    }

    record AppInfo(String name, String version, String env) {}
}
