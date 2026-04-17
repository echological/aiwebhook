package com.avrist.webhook.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRecordDto {

    public static final String COLLECTION = "trx_records";

    @JsonProperty("_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId id;
    public Boolean error;
    @JsonProperty("error_cause")
    public String errorCause;
    public String type;
    public String description;
    public Double amount;
    public String currency;
    public String category;
    public String account;
    public String fromUsername;
    public String chatText;
    public String ocrResult;
    public String caption;
    @JsonProperty("telegram_chat_records_id")
    private ObjectId chatRecordId;
    public LocalDateTime trxDate;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("app_version")
    private String appVersion;
}

