package br.mac.bank.api.controller;

import br.mac.bank.api.dto.request.RoleUserRequest;
import br.mac.bank.config.Constants;
import br.mac.bank.config.Utils;
import br.mac.bank.model.Role;
import br.mac.bank.model.User;
import br.mac.bank.service.RoleService;
import br.mac.bank.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final Utils utils;


    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
        return ResponseEntity.created(uri).body(roleService.saveRole(role));
    }


    @PostMapping("/role/add/user")
    public ResponseEntity<Role> addRoleToUser(@RequestBody RoleUserRequest roleUser){
        roleService.addToUser(roleUser.getUsername(), roleUser.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constants.BEARER_START)){
            try {
                String refreshToken = utils.getBearerFromHeader(authHeader);

                Algorithm algorithm = utils.getAlgorithm();
                User user = userService.getUser(utils.getUsername(algorithm, refreshToken));

                List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

                String accessToken = utils.getToken(request, user.getUsername(), algorithm, roles,
                        Constants.EXPIRATION_ACCESS_MINUTES);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception ex){

                response.setHeader("error", ex.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("error_msg", ex.getMessage());

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
        } else {
            throw new RuntimeException("token missing");
        }
    }

}
