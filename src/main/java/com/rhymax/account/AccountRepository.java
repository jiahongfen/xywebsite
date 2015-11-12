package com.rhymax.account;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.persist(account);
        return account;
    }

    public Account findByNumberOrEmail(String numberOrEmail) {
        return findByNumberOrEmail(numberOrEmail, numberOrEmail);
    }

    public Account findByNumberOrEmail(String number, String email) {
        try {
            return entityManager.createNamedQuery(Account.FIND_BY_NUMBER_OR_EMAIL, Account.class)
                    .setParameter("number", number).setParameter("email", email).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public Account findByNumber(String number) {
        try {
            return entityManager.createQuery("select a from Account a where a.number = :number", Account.class)
                    .setParameter("number", number).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean isNumberExisted(String number) {
        return findByNumber(number) != null;
    }

    public Account findByEmail(String email) {
        try {
            return entityManager.createQuery("select a from Account a where a.email = :email", Account.class)
                    .setParameter("email", email).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean isEmailExisted(String email) {
        return findByEmail(email) != null;
    }

}
