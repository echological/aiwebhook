package com.avrist.webhook.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exception untuk validasi di layer service.
 * Simpan error umum + detail field yang gagal validasi.
 */
public class ServiceValidationException extends RuntimeException {

    private final String errorCode;
    private final Map<String, String> fieldErrors;

    public ServiceValidationException(String message) {
        this(message, "VALIDATION_ERROR", Collections.emptyMap());
    }

    public ServiceValidationException(String message, String errorCode) {
        this(message, errorCode, Collections.emptyMap());
    }

    public ServiceValidationException(String message, Map<String, String> fieldErrors) {
        this(message, "VALIDATION_ERROR", fieldErrors);
    }

    public ServiceValidationException(String message, String errorCode, Map<String, String> fieldErrors) {
        super(message);
        this.errorCode = errorCode;
        this.fieldErrors = fieldErrors == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public static ServiceValidationException of(String message) {
        return new ServiceValidationException(message);
    }

    public static ServiceValidationException withFieldError(String field, String reason) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(field, reason);
        return new ServiceValidationException("Validation failed", "VALIDATION_ERROR", errors);
    }

    public static ServiceValidationException withFieldErrors(Map<String, String> fieldErrors) {
        return new ServiceValidationException("Validation failed", "VALIDATION_ERROR", fieldErrors);
    }
}