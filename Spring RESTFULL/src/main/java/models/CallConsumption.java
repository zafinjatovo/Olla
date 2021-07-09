/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tokiniaina
 */
public class CallConsumption 
{
    int id;
    int idReceipter;
    int receipterIdOperator;
    double callduration;
    LocalDateTime date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReceipter() {
        return idReceipter;
    }

    public void setIdReceipter(int idReceipter) {
        this.idReceipter = idReceipter;
    }

    public int getReceipterIdOperator() {
        return receipterIdOperator;
    }

    public void setReceipterIdOperator(int receipterIdOperator) {
        this.receipterIdOperator = receipterIdOperator;
    }

    public double getCallduration() {
        return callduration;
    }

    public void setCallduration(double callduration) {
        this.callduration = callduration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public CallConsumption(int id, int idReceipter, int receipterIdOperator, double callduration, LocalDateTime date) {
        this.id = id;
        this.idReceipter = idReceipter;
        this.receipterIdOperator = receipterIdOperator;
        this.callduration = callduration;
        this.date = date;
    }
    
    public List<CallPurchase> RemoveCallPurchase(List<CallPurchase> callPurchases)
    {
        List<CallPurchase> ans = new ArrayList<CallPurchase>();
        for(int i = 0;i<callPurchases.size();i++)
        {
            CallPurchase callPurchase = callPurchases.get(i);
            Timestamp tPurchase = Timestamp.valueOf(callPurchase.getPurchaseDate());
            Timestamp tCallConsumption = Timestamp.valueOf(this.getDate());
            int cmp = tCallConsumption.compareTo(tPurchase);
            if(cmp >= 0)
            {
                ans.add(callPurchases.remove(i));
                i--;
            }
        }
        return ans;
    }
   
    public List<CallPurchase> Consume(List<CallPurchase> callPurchases, User user)
    {
        List<CallPurchase> callPurchasesleft = new ArrayList<CallPurchase>();
        double callDuration = this.getCallduration();
        for(int i = 0;i<callPurchases.size();i++)
        {
            CallPurchase callPurchase = callPurchases.get(i);
            double callValue = 0;
            if(user.getIdOperator() == this.getReceipterIdOperator())
            {
                callValue = callDuration * callPurchase.getConsumptionUnit();
            }
            else
            {
                callValue = callDuration * callPurchase.getOtherConsumptionUnit();
            }
            
            if(callValue < callPurchase.getValue())
            {
                callPurchase.setValue(callPurchase.getValue() - callValue);
                callPurchasesleft.add(callPurchase);
                if(i < callPurchases.size() -1)
                {
                    List<CallPurchase> callPurchaseDown = callPurchases.subList(i + 1, callPurchases.size());
                    callPurchasesleft.addAll(callPurchaseDown);
                }
                break;
            }
            else
            {
                //calculena ny duration lany de atao moins de ini ny duration vao
                double durationConsume = 0;
                if(user.getIdOperator() == this.getReceipterIdOperator())
                {
                    durationConsume = callPurchase.getValue() / callPurchase.getConsumptionUnit();
                }
                else
                {
                    durationConsume = callPurchase.getValue() / callPurchase.getOtherConsumptionUnit();
                }
                callDuration = callDuration - durationConsume;
            }
        }
        return callPurchasesleft;
    }
    
    public List<CallPurchase> RemoveOutOfDate(List<CallPurchase> purchases)
    {
        for(int i = 0;i<purchases.size();i++)
        {
            CallPurchase temp = purchases.get(i);
            if(Timestamp.valueOf(temp.getExpirationDate()).compareTo(Timestamp.valueOf(this.getDate())) < 0)
            {
                purchases.remove(i);
                i--;
            }
        }
        return purchases;
    }
    
    public boolean Save(Connection connection, User user) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "insert into t_callconsumption (iduser, idreceipter, duration, date) values (" + user.getId() + ", " + this.getIdReceipter() + ", " + this.getCallduration() + ", '" + this.getDate() + "');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            ans = true;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if((connection != null) && (connection.isClosed() == false))
            {
                connection.close();
            }
        }
        return ans;
    }
}
