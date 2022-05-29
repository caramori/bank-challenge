package br.mac.bank.api.controller;

import br.mac.bank.api.dto.mapper.TransferMapper;
import br.mac.bank.api.dto.request.TransferRequest;
import br.mac.bank.api.dto.response.TransferResponse;
import br.mac.bank.model.Transfer;
import br.mac.bank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransferMapper transferMapper;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody @Valid TransferRequest transferReq,
                                                     @RequestHeader Map<String, String> headers) throws IOException {

        Transfer transfer = transferMapper.getTransferFromDto(transferReq, headers);

        transferService.processTransfer(transfer);

        TransferResponse result = new TransferResponse();
        result.setTaxCollected(transfer.getTaxCollected());
        result.setId(transfer.getUuid());
        result.setCAD(transfer.getCAD());

        return ResponseEntity.ok().body(result);
    }
}
