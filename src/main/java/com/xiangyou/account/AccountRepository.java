package com.xiangyou.account;

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

    public Account findByPhoneOrEmail(String phoneOrEmail) {
        return findByPhoneOrEmail(phoneOrEmail, phoneOrEmail);
    }

    public Account findByPhoneOrEmail(String phone, String email) {
        try {
            return entityManager.createNamedQuery(Account.FIND_BY_PHONE_OR_EMAIL, Account.class)
                    .setParameter("phone", phone).setParameter("email", email).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public Account findByPhone(String phone) {
        try {
            return entityManager.createQuery("select a from Account a where a.phone = :phone", Account.class)
                    .setParameter("phone", phone).getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean isPhoneExisted(String phone) {
        return findByPhone(phone) != null;
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
