package br.mac.bank.fixer;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class Latest {

    private Boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

}
