package com.xiangyou.signup;

import org.hibernate.validator.constraints.NotBlank;

import com.xiangyou.account.Account;

public class SignupForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
    @SuppressWarnings("unused")
    private static final String EMAIL_MESSAGE = "{email.message}";

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String phone;

    private String email;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String password;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String phoneCode;

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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Account createAccount() {
        return new Account(getPhone(), getEmail(), getPassword(), "ROLE_USER");
    }
}
