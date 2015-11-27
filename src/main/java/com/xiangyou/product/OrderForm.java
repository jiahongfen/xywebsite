package com.xiangyou.product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangyou.account.Tourist;

public class OrderForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String contactName;

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String phone;

    @NotBlank(message = OrderForm.NOT_BLANK_MESSAGE)
    private String adults;

    private String kids;

    private String comment;

    private String contract;

    private List<Tourist> tourists = new ArrayList<Tourist>();

    public Ordar createOrder() throws JsonProcessingException {
        return new Ordar(getContactName(), getPhone(), toInt(getAdults()), toInt(getKids()), getComment(),
                getTouristsInfo());
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    public void setTourists(List<Tourist> tourists) {
        this.tourists = tourists;
    }

    public String getTouristsInfo() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getTourists());
    }
}
