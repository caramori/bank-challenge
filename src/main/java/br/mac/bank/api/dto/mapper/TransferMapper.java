package br.mac.bank.api.dto.mapper;

import br.mac.bank.api.dto.request.TransferRequest;
import br.mac.bank.api.dto.response.TransferResponse;
import br.mac.bank.config.Utils;
import br.mac.bank.model.Transfer;
import br.mac.bank.service.AccountService;
import br.mac.bank.service.CurrencyService;
import br.mac.bank.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Service
public class TransferMapper {

    private final CurrencyService currencyService;
    private final AccountService accountService;
    private final UserService userService;
    private final Utils utils;

    public Transfer getTransferFromDto(TransferRequest request, Map<String, String> headers){

        Transfer transfer = new Transfer();

        transfer.setAmount(request.getAmount());
        transfer.setDescription(request.getDescription());

        transfer.setCurrency(currencyService.findBySymbol(request.getCurrency()));
        transfer.setOrigin(accountService.findById(request.getOriginAccount()));
        transfer.setDestination(accountService.findById(request.getDestinationAccount()));

        String username = utils.getUsernameFromHeader(headers);
        transfer.setUser(userService.getUser(username));

        return transfer;
    }

    public TransferResponse getTransferResponseFromTransfer(Transfer transfer){

        TransferResponse response = new TransferResponse();
        response.setCAD(transfer.getCAD());
        response.setId(transfer.getUuid());
        response.setTaxCollected(transfer.getTaxCollected());

        return response;
    }

}
