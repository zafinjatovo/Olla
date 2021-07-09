/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Tokiniaina
 */
public class CallMade 
{
    String interlocName;
    String interlocPhoneNb;
    LocalDateTime timestamp;
    String type;

    public String getInterlocName() {
        return interlocName;
    }

    public void setInterlocName(String interlocName) {
        this.interlocName = interlocName;
    }

    public String getInterlocPhoneNb() {
        return interlocPhoneNb;
    }

    public void setInterlocPhoneNb(String interlocPhoneNb) {
        this.interlocPhoneNb = interlocPhoneNb;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CallMade() {
    }

    public CallMade(String interlocName, String interlocPhoneNb, LocalDateTime timestamp, String type) {
        this.interlocName = interlocName;
        this.interlocPhoneNb = interlocPhoneNb;
        this.timestamp = timestamp;
        this.type = type;
    }
}
