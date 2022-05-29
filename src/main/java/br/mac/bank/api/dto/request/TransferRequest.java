package br.mac.bank.api.dto.request;

import br.mac.bank.api.validation.annotations.ExistsInDatabase;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class TransferRequest {

    @Positive
    private BigDecimal amount;
    @NotBlank
    @ExistsInDatabase(table = "currency", column = "symbol")
    private String currency;
    @Positive
    @ExistsInDatabase(table = "account", column = "id")
    private Long originAccount;
    @Positive
    @ExistsInDatabase(table = "account", column = "id")
    private Long destinationAccount;

    private String description;

}
