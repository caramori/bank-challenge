package br.mac.bank.filters;

import br.mac.bank.config.Constants;
import br.mac.bank.config.Utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private Utils envUtils;

    public CustomAuthorizationFilter(Utils envUtils){
        this.envUtils = envUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") ||
                request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request, response);
        } else {
             String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
             if (authHeader != null && authHeader.startsWith(Constants.BEARER_START)){
                 try {
                     String token = envUtils.getBearerFromHeader(authHeader);

                     Algorithm algorithm = envUtils.getAlgorithm();
                     JWTVerifier verifier = JWT.require(algorithm).build();
                     DecodedJWT decodedJWT = verifier.verify(token);
                     String username = decodedJWT.getSubject();

                     String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                     Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                     for (String role : roles){
                         authorities.add(new SimpleGrantedAuthority(role));
                     }

                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                     SecurityContextHolder.getContext().setAuthentication(authToken);
                     filterChain.doFilter(request, response);

                 } catch (Exception ex){

                     response.setHeader("error", ex.getMessage());

                     Map<String, String> tokens = new HashMap<>();
                     tokens.put("error_msg", ex.getMessage());

                     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                     response.setStatus(HttpStatus.UNAUTHORIZED.value());

                     new ObjectMapper().writeValue(response.getOutputStream(), tokens);

                 }
             } else {
                 filterChain.doFilter(request, response);
             }
        }
    }
}
