package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ActionMobileMoney 
{
    public enum Transaction
    {
        deposit,
        withdrawal,
        buy
    }
    
    public static Transaction StringToTransaction(String str)
    {
        if((Transaction.deposit.toString()).compareTo(str)==0)
        {
            return Transaction.deposit;
        }
        else if((Transaction.withdrawal.toString()).compareTo(str)==0)
        {
            return Transaction.withdrawal;            
        }
        else
        {
            return Transaction.buy;
        }
    }
    
    int id;
    User user;
    double cash;
    Transaction transaction;
    LocalDateTime date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ActionMobileMoney() {
    }

    public ActionMobileMoney(int id) {
        this.id = id;
    }
    
    public ActionMobileMoney(User user, double cash, Transaction transaction, LocalDateTime date) {
        this.user = user;
        this.cash = cash;
        this.transaction = transaction;
        this.date = date;
    }

    public ActionMobileMoney(int id, User user, double cash, Transaction transaction, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.cash = cash;
        this.transaction = transaction;
        this.date = date;
    }

    public ActionMobileMoney(double cash, Transaction transaction, LocalDateTime date) {
        this.cash = cash;
        this.transaction = transaction;
        this.date = date;
    }
    
    
    public boolean save(Connection connection, boolean closeConnection) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "INSERT INTO t_mobilemoney (iduser, cash, transactiontype, date) VALUES (" + this.getUser().getId() + ", " + this.getCash() + ", '" + this.getTransaction().toString() + "', '" + this.getDate() + "')" ;
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
            if((connection != null) && (connection.isClosed() == false) && (closeConnection))
            {
                connection.close();
            }
        }
        return ans;
    }
    
    public boolean isConfirmed(Connection connection) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_mobilemoney where confirmed = true and transactiontype = 'deposit' and id = " + this.getId() + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
               ans = true;
            }
            preparedStatement.close();
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
