package com.xiangyou.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Ordar is Order, for "order" is the keyword in database.
 *
 * @author dingli
 *
 */
@Entity
@Table(name = "ordar")
@NamedQuery(name = Ordar.QUERY_ALL, query = "select a from Ordar a")
public class Ordar implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3334584632063492821L;

    public static final String QUERY_ALL = "Ordar.queryAll";

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String productId;

    @Column
    private String date;

    @Column
    private String username;

    @Column
    private String contactName;

    @Column
    private String phone;

    @Column
    private int adults;

    @Column
    private int kids;

    @Column
    private String comment;

    @Column
    private String tourists;

    public Ordar() {
    }

    public Ordar(String productId, String date, String contactName, String phone, int adults, int kids, String comment,
            String tourists) {
        this.productId = productId;
        this.date = date;
        this.contactName = contactName;
        this.phone = phone;
        this.adults = adults;
        this.kids = kids;
        this.comment = comment;
        this.tourists = tourists;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getKids() {
        return kids;
    }

    public void setKids(int kids) {
        this.kids = kids;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTourists() {
        return tourists;
    }

    public void setTourists(String tourists) {
        this.tourists = tourists;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
