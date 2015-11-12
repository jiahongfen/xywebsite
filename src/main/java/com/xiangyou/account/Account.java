package com.xiangyou.account;

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
        @NamedQuery(name = Account.FIND_BY_PHONE_OR_EMAIL, query = "select a from Account a where a.phone = :phone or a.email = :email"),
        @NamedQuery(name = Account.FIND_BY_PHONE, query = "select a from Account a where a.phone = :phone"),
        @NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email") })
public class Account implements java.io.Serializable {

    public static final String FIND_BY_PHONE_OR_EMAIL = "Account.findByPhoneOrEmail";
    public static final String FIND_BY_PHONE = "Account.findByPhone";
    public static final String FIND_BY_EMAIL = "Account.findByEmail";

    @Id
    @GeneratedValue
    private Long id;

    // TODO Valid phone check
    @Column(unique = true)
    private String phone;

    // TODO Modify Email for user
    @Column(nullable = true)
    private String email;

    @JsonIgnore
    private String password;

    private String role = "ROLE_USER";

    protected Account() {
    }

    public Account(String phone, String email, String password, String role) {
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
