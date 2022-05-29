package br.mac.bank.service;

import br.mac.bank.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    User getUser(String username);
    List<User>getUsers();

}
