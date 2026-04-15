package com.avrist.webhook.etc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookActionSchema {
    public Boolean error;
    @JsonProperty("error_cause")
    public String errorCause;
    public String action;
}
