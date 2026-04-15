package com.avrist.webhook.data.adapter;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.bson.Document;
import org.jboss.logging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

@ApplicationScoped
public class ErrorLogAdapter {

    private static final Logger LOG = Logger.getLogger(ErrorLogAdapter.class);

    @Inject
    private MongoDataConnectionFactory mongo;

    @Context
    private UriInfo uriInfo;

    @Inject
    private AppConfig appConfig;

    public void logErrorToMongo(Throwable throwable, int statusCode, String errorCode) {
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
