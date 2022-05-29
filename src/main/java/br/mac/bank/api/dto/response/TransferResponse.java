package br.mac.bank.api.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponse {

    private String id;
    private BigDecimal taxCollected;
    private BigDecimal CAD;

}
