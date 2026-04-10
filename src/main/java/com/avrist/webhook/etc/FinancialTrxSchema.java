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
public class FinancialTrxSchema {
    public Boolean error;
    @JsonProperty("error_cause")
    public String errorCause;
    public String type;
    public String description;
    public Double amount;
    public String currency;
    public String category;
    public String account;
}
