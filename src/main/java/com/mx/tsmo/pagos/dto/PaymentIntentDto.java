package com.mx.tsmo.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class PaymentIntentDto {

    public enum Currency {
        MXN
    }

    private String description;
    private int amount;
    private Currency currency;

}
