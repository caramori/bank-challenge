package br.mac.bank.service.impl;

import br.mac.bank.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsInDatabase(String table, String column, Object value) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT 1 ");
        query.append(" FROM ").append(table);
        query.append(" WHERE ").append(column).append(" = :value");

        Query nativeQuery = entityManager.createNativeQuery(query.toString());

        nativeQuery.setParameter("value", value);

        return nativeQuery.getResultList().size() > 0;
    }
}
