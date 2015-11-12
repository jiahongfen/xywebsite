package com.rhymax.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = Account.FIND_BY_NUMBER_OR_EMAIL, query = "select a from Account a where a.number = :number or a.email = :email"),
        @NamedQuery(name = Account.FIND_BY_NUMBER, query = "select a from Account a where a.number = :number"),
        @NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email") })
public class Account implements java.io.Serializable {

    public static final String FIND_BY_NUMBER_OR_EMAIL = "Account.findByNumberOrEmail";
    public static final String FIND_BY_NUMBER = "Account.findByNumber";
    public static final String FIND_BY_EMAIL = "Account.findByEmail";

    @Id
    @GeneratedValue
    private Long id;

    // TODO Valid number check
    @Column(unique = true)
    private String number;

    // TODO Modify Email for user
    @Column(nullable = true)
    private String email;

    @JsonIgnore
    private String password;

    private String role = "ROLE_USER";

    protected Account() {
    }

    public Account(String number, String email, String password, String role) {
        this.number = number;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
