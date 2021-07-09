/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tokiniaina
 */
public class Admin 
{
    int id;
    String email;
    String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    
    static Admin isAuthentifiate(Connection connection, String email, String password) throws Exception
    {
        Admin admin = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_admin where email = '" + email + "' and password = '" + password + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                admin = new Admin(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("password"));
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
        return admin;
    }
    
    String GenerateTokenAdmin(Connection connection) throws Exception
    {
        String token = "";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "INSERT INTO t_tokenAdmin(idAdmin, value, expiration) VALUES (" + this.getId() + ", MD5(CONCAT(now()," + this.getId() + ")), CURRENT_TIMESTAMP + '3 day');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            sql = "select value from t_tokenAdmin where idAdmin = " + this.getId() + " order by id desc LIMIT 1;";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            if(resultSet.next())
            {
                token = resultSet.getString("value");
            }
            resultSet.close();
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
        return token;
    }
    
    public static int GetAdminIdByToken(Connection connection, String token) throws Exception
    {
        int ans = -1;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select idAdmin from t_tokenAdmin where value = '" + token + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                ans = resultSet.getInt("idAdmin");
            }
            preparedStatement.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
           // no connection close
        }
        return ans;
    }
    
    public static Admin FindById(Connection connection, int id) throws Exception
    {
        Admin admin = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_admin where id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                admin = new Admin(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("password"));
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
        return admin;
    }
    
    public List<ActionMobileMoney> GetAllDepositNotConfirmed(Connection connection) throws Exception
    {
        List<ActionMobileMoney> deposit = new ArrayList<ActionMobileMoney>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_mobilemoney where confirmed = false and transactiontype = 'deposit';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                ActionMobileMoney action = new ActionMobileMoney(resultSet.getInt("id"), new User(resultSet.getInt("id"), resultSet.getInt("idoperator"), resultSet.getString("name"), resultSet.getString("firstname"), resultSet.getString("ncin")), resultSet.getDouble("cash"), ActionMobileMoney.Transaction.deposit, resultSet.getTimestamp("date").toLocalDateTime());
                deposit.add(action);
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
        return deposit;
    }
    
    public static String LogIn(Connection connection, String email, String password) throws Exception
    {
        String token = "";
        try
        {
            Admin admin = Admin.isAuthentifiate(connection, email, password);
            if(admin != null)
            {
                token = admin.GenerateTokenAdmin(connection);
            }
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
        return token;
    }
    
    public boolean ConfirmDeposit(Connection connection, int idDeposit, boolean confirmation) throws Exception
    {
        boolean ans = false;
        try
        {
            ActionMobileMoney action = new ActionMobileMoney(idDeposit);
            if(action.isConfirmed(connection) == false)
            {
                if(connection.isClosed())
                {
                    connection = DBConnection.connect();
                }
                String sql = "insert into t_mobilemoneyconfirmation (idmobilemoney, confirmed, date) values (" + idDeposit + ", " + confirmation + ", now());";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                ans = true;
            }
        }
        catch(Exception ex)
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
