package br.mac.bank.config;

import br.mac.bank.fixer.FixerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Bean
    public FixerClient getFixerClient(){
        return new FixerClient();
    }

    @Bean
    public DateUtils getDateUtils(){
        return new DateUtils();
    }

}
