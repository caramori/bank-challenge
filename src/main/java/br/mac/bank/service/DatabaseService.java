package br.mac.bank.service;


public interface DatabaseService {

    boolean existsInDatabase(String table, String column, Object value);
}
