package br.mac.bank.fixer;

import br.mac.bank.config.Utils;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;


/**
 * Client for fixer.io
 * TODO create separate project as a component
 */
@Component
public class FixerClient {

    private String url;
    private String accessKey;

    @Autowired
    private Utils utils;

    private static FixerClient instance;

    public FixerClient(){
    }

    @PostConstruct
    public void setVariables(){
        this.url = utils.getVariable("fixer.url", "fixer.dev.url");
        this.accessKey = utils.getVariable("fixer.access_key", "fixer.dev.access_key");
    }

    private WebClient buildClient(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000));

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public BigDecimal convertCurrency(BigDecimal amount, String fromSymbol, String toSymbol){

        Map<String, String> params = Map.of("access_key", accessKey);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .query("access_key={access_key}").build().expand(params);

        Mono<Latest> latestResult =
        this.buildClient().get()
                .uri(uriComponents.toUri().toString())
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Latest.class);

        Latest latest = latestResult.blockOptional().get();

        BigDecimal sourceToEUR = amount.divide(latest.getRates().get(fromSymbol), 10, RoundingMode.HALF_EVEN);
        BigDecimal targetToEUR = sourceToEUR.multiply(latest.getRates().get(toSymbol));

        return targetToEUR;
    }
}
