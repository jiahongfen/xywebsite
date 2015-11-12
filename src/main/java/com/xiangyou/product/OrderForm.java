package com.xiangyou.product;

import org.hibernate.validator.constraints.NotBlank;

public class OrderForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String contactName;

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String number;

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String adults;

    private String kids;

    private String comment;

    public Ordar createOrder() {
        return new Ordar(getContactName(), getNumber(), toInt(getAdults()), toInt(getKids()), getComment());
    }

    public int toInt(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // Ignore
        }
        return i;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
