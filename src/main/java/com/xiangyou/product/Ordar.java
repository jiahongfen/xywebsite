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
    private String contactName;

    @Column
    private String number;

    @Column
    private int adults;

    @Column
    private int kids;

    @Column
    private String comment;

    public Ordar() {
    }

    public Ordar(String contactName, String number, int adults, int kids, String comment) {
        this.contactName = contactName;
        this.number = number;
        this.adults = adults;
        this.kids = kids;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
