/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tokiniaina
 */
public class CallPurchase 
{
    int idOffer;
    String offerName;
    double value;
    double consumptionUnit;
    double otherConsumptionUnit;
    LocalDateTime purchaseDate;
    LocalDateTime expirationDate;

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(double consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    public double getOtherConsumptionUnit() {
        return otherConsumptionUnit;
    }

    public void setOtherConsumptionUnit(double otherConsumptionUnit) {
        this.otherConsumptionUnit = otherConsumptionUnit;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public CallPurchase(int idOffer, String offerName, double value, double consumptionUnit, double otherConsumptionUnit, LocalDateTime purchaseDate, LocalDateTime expirationDate) {
        this.idOffer = idOffer;
        this.offerName = offerName;
        this.value = value;
        this.consumptionUnit = consumptionUnit;
        this.otherConsumptionUnit = otherConsumptionUnit;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }
    
    public boolean HasIntersection(CallPurchase callpurchase)
    {
        if((Timestamp.valueOf(this.getExpirationDate()).compareTo(Timestamp.valueOf(callpurchase.getPurchaseDate())) >= 0) && (Timestamp.valueOf(this.getExpirationDate()).compareTo(Timestamp.valueOf(callpurchase.getExpirationDate())) < 0 ))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static List<CallPurchase> FusionCallPurchase(List<CallPurchase> callPurchases)
    {
        for(int i = 0;i<callPurchases.size() - 1;i++)
        {
            CallPurchase callPurchaseOne = callPurchases.get(i);
            if(callPurchaseOne != null)
            {
                for(int j = i + 1;j<callPurchases.size();j++)
                {
                    CallPurchase callPurchaseTwo = callPurchases.get(j);
                    if(callPurchaseTwo != null)
                    {
                        if((callPurchaseOne.getIdOffer() == callPurchaseTwo.getIdOffer()) && (callPurchaseOne.HasIntersection(callPurchaseTwo)))
                        {
                            callPurchaseOne.setExpirationDate(callPurchaseTwo.getExpirationDate());
                            callPurchaseOne.setValue(callPurchaseOne.getValue() + callPurchaseTwo.getValue());
                            callPurchases.set(i, callPurchaseOne);
                            callPurchases.set(j, null);
                        }
                    }
                    else
                    {
                        continue;
                    }
                }
            }
            else
            {
                continue;
            }
        }
        for(int i = 0;i<callPurchases.size();i++)
        {
            CallPurchase temp = callPurchases.get(i);
            if(temp == null)
            {
                callPurchases.remove(i);
                i--;
            }
        }
        return callPurchases;
    }
    
    public static List<CallPurchase> RemoveOutOfDate(List<CallPurchase> purchases, LocalDateTime date)
    {
        for(int i = 0;i<purchases.size();i++)
        {
            CallPurchase temp = purchases.get(i);
            if(Timestamp.valueOf(temp.getExpirationDate()).compareTo(Timestamp.valueOf(date)) < 0)
            {
                purchases.remove(i);
                i--;
            }
        }
        return purchases;
    }
}
