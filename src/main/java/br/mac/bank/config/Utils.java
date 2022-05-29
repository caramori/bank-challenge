package br.mac.bank.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Component
public class Utils {

    @Autowired
    private Environment env;

    public String getVariable(String system, String dev){
        String url = env.getProperty(system);
        if (url == null || url.isBlank()){
            url = env.getProperty(dev);
        }
        return url;
    }

    public Algorithm getAlgorithm(){
        return Algorithm.HMAC256(getVariable("jwt.dev.secret", "jwt.secret").getBytes());
    }

    public String getBearerFromHeader(String header){
        return header.substring(Constants.BEARER_START.length());
    }

    public String getUsernameFromHeader(Map<String, String> header){
        String authHeader = header.get(HttpHeaders.AUTHORIZATION);
        if (authHeader == null){
            //spring send headers lowercase when @Headers is used
            authHeader = header.get(HttpHeaders.AUTHORIZATION.toLowerCase());
        }
        String bearer = getBearerFromHeader(authHeader);
        return getUsername(getAlgorithm(), bearer);
    }

    public String getUsername(Algorithm algorithm, String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public String getToken(HttpServletRequest request, String username, Algorithm algorithm,
                           List<String> roles, Long minutesExpire) {

        JWTCreator.Builder builder = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + (minutesExpire * 60 * 1000)))
                .withIssuer(request.getRequestURL().toString());

        if (roles != null){
            builder.withClaim("roles", roles);
        }

        return builder.sign(algorithm);
    }
}
