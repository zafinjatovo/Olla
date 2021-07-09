/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.bson.Document;
import org.springframework.data.annotation.Id;

/**
 *
 * @author Tokiniaina
 */

public class Contact 
{
    @Id
    private String id;
    private String idContactOwner;
    private String name;
    private String firstname;
    private String phonenumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdContactOwner() {
        return idContactOwner;
    }

    public void setIdContactOwner(String idContactOwner) {
        this.idContactOwner = idContactOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Contact(String name, String firstname, String phonenumber) {
        this.name = name;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
    }

    public Contact(String id, String idContactOwner, String name, String firstname, String phonenumber) {
        this.id = id;
        this.idContactOwner = idContactOwner;
        this.name = name;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
    }
    
    @Override
    public String toString() {
        return "Contact{" + "id=" + id + ", name=" + name + ", firstname=" + firstname + ", phonenumber=" + phonenumber + '}';
    }
    
    public Document toDocument()
    {
        Document contact = new Document("idContactOwner", this.getIdContactOwner())
                            .append("name", this.getName())
                            .append("firstname", this.getFirstname())
                            .append("phonenumber", this.getPhonenumber());
        return contact;
    }
    
}
