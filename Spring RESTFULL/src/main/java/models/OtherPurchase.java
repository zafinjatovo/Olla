/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author Tokiniaina
 */
public class OtherPurchase 
{
    int idOfferType;
    String offerType;

    double value;
    Timestamp datepurchase;
    Timestamp dateexpiration;

    public int getIdOfferType() {
        return idOfferType;
    }

    public void setIdOfferType(int idOfferType) {
        this.idOfferType = idOfferType;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp getDatepurchase() {
        return datepurchase;
    }

    public void setDatepurchase(Timestamp datepurchase) {
        this.datepurchase = datepurchase;
    }

    public Timestamp getDateexpiration() {
        return dateexpiration;
    }

    public void setDateexpiration(Timestamp dateexpiration) {
        this.dateexpiration = dateexpiration;
    }

    public OtherPurchase() {
    }

    public OtherPurchase(int idOfferType, String offerType, double value, Timestamp datepurchase, Timestamp dateexpiration) {
        this.idOfferType = idOfferType;
        this.offerType = offerType;
        this.value = value;
        this.datepurchase = datepurchase;
        this.dateexpiration = dateexpiration;
    }

    public boolean HasIntersectionDate(Timestamp date)
    {
        if(this.dateexpiration.compareTo(date) >= 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
}
