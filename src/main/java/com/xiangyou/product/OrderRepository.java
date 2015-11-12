package com.xiangyou.product;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Ordar save(Ordar order) {
        entityManager.persist(order);
        return order;
    }

    public List<Ordar> queryAll() {
        try {
            return entityManager.createNamedQuery(Ordar.QUERY_ALL, Ordar.class).getResultList();
        } catch (PersistenceException e) {
            return null;
        }
    }

}
