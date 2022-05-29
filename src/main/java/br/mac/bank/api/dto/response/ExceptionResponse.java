package br.mac.bank.api.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ExceptionResponse {

    private String errorCode;
    private List<String> errors;


}
