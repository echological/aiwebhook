package com.avrist.webhook.etc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinancialTrxSchema {
    public Boolean error;
    @JsonProperty("error_cause")
    public String errorCause; // nullable
    public String type;       // expense | income
    public String description;
    public Double amount;
    public String currency;
    public String category;
    public String account;    // nullable
}
