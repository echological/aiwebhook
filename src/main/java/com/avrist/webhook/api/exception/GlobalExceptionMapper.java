package com.avrist.webhook.api.exception;

import com.avrist.webhook.data.adapter.ErrorLogAdapter;
import com.avrist.webhook.dto.ApiResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Inject
    private ErrorLogAdapter errorLogAdapter;

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
            errorLogAdapter.logErrorToMongo(throwable, Response.Status.BAD_REQUEST.getStatusCode(), "BAD_REQUEST");
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
        errorLogAdapter.logErrorToMongo(throwable, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "INTERNAL_ERROR");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error", "INTERNAL_ERROR"))
                .build();
    }

}
