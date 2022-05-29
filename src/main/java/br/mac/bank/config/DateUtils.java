package br.mac.bank.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateUtils {

    public Date today(){
        return new Date();
    }
}
