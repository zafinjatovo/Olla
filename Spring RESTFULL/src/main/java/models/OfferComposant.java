/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Tokiniaina
 */
public class OfferComposant 
{
    OfferType type;
    double value;
    double consumptionUnit;
    double otherConsumptionUnit;

    public OfferType getType() {
        return type;
    }

    public void setType(OfferType type) {
        this.type = type;
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

    public OfferComposant() {
    }

    public OfferComposant(OfferType type, double value, double consumptionUnit, double otherConsumptionUnit) {
        this.type = type;
        this.value = value;
        this.consumptionUnit = consumptionUnit;
        this.otherConsumptionUnit = otherConsumptionUnit;
    }
    
    
}
