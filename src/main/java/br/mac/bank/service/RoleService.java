package br.mac.bank.service;

import br.mac.bank.model.Role;

public interface RoleService {

    Role saveRole(Role role);
    Role findByName(String name);
    void addToUser(String username, String rolename);
}
