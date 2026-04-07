package com.avrist.webhook.api.exception;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.dto.ApiResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import org.bson.Document;
import org.jboss.logging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private AppConfig appConfig;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable throwable) {
        if (throwable instanceof ServiceValidationException validationEx) {
            LOG.debugf("Validation error: %s", validationEx.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(validationEx.getMessage(), validationEx.getErrorCode()))
                    .build();
        }

        if (throwable instanceof IllegalArgumentException illegalArgEx) {
            LOG.debugf("Invalid argument: %s", illegalArgEx.getMessage());
            logErrorToMongo(throwable, Response.Status.BAD_REQUEST.getStatusCode(), "BAD_REQUEST");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(illegalArgEx.getMessage(), "BAD_REQUEST"))
                    .build();
        }

        if (throwable instanceof NotFoundException notFoundEx) {
            LOG.debugf("Resource not found: %s", notFoundEx.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Resource not found", "NOT_FOUND"))
                    .build();
        }

        LOG.error("Unhandled exception", throwable);
        logErrorToMongo(throwable, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "INTERNAL_ERROR");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error", "INTERNAL_ERROR"))
                .build();
    }

    private void logErrorToMongo(Throwable throwable, int statusCode, String errorCode) {
        if (!appConfig.isErrorLogMongoEnabled()) {
            return;
        }

        String path = uriInfo != null ? uriInfo.getPath() : null;
        String rootCauseMessage = getRootCauseMessage(throwable);

        Document errorDoc = new Document()
                .append("timestamp", Instant.now().toString())
                .append("status", statusCode)
                .append("errorCode", errorCode)
                .append("path", path)
                .append("exceptionClass", throwable.getClass().getName())
                .append("message", throwable.getMessage())
                .append("rootCause", rootCauseMessage)
                .append("stackTrace", getStackTraceAsString(throwable));

        try {
            mongo.collection(appConfig.getErrorLogMongoCollection()).insertOne(errorDoc);
        } catch (Exception e) {
            LOG.warnf("Failed writing error log to MongoDB: %s", e.getMessage());
        }
    }

    private static String getRootCauseMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current.getMessage();
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
