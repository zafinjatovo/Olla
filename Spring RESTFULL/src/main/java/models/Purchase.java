/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author Tokiniaina
 */
public class Purchase 
{
    Offer offer;
    int typepurchase;
    LocalDateTime date;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public int getTypepurchase() {
        return typepurchase;
    }

    public void setTypepurchase(int typepurchase) {
        this.typepurchase = typepurchase;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Purchase() {
    }

    public Purchase(Offer offer, int typepurchase, LocalDateTime date) {
        this.offer = offer;
        this.typepurchase = typepurchase;
        this.date = date;
    }
    
    
}
