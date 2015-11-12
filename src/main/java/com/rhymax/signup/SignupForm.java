package com.rhymax.signup;

import org.hibernate.validator.constraints.NotBlank;

import com.rhymax.account.Account;

public class SignupForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
    @SuppressWarnings("unused")
    private static final String EMAIL_MESSAGE = "{email.message}";

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String number;

    private String email;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String password;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String mobileCode;

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

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public Account createAccount() {
        return new Account(getNumber(), getEmail(), getPassword(), "ROLE_USER");
    }
}
