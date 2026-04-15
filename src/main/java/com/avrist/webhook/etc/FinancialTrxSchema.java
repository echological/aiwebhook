package com.avrist.webhook.etc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialTrxSchema {

    public List<FinancialTrxSchemaData> data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinancialTrxSchemaData {
        public Boolean error;
        @JsonProperty("error_cause")
        public String errorCause;
        public String type;
        public String description;
        public Double amount;
        public LocalDateTime trxDate;
        public String currency;
        public String category;
        public String account;
    }


}
