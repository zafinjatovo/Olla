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
public class OfferType 
{
    int id;
    String name;
    String unit;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public OfferType(int id) {
        this.id = id;
    }

    public OfferType() {
    }
    
    public OfferType(int id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }
    
    public static List<OfferType> GetOfferType(Connection connection) throws Exception
    {
        List<OfferType> offerTypes = new ArrayList<OfferType>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_offerType";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            while(resultSet.next())
            {
                OfferType offerType = new OfferType(resultSet.getInt("id"), resultSet.getString("typevalue"), resultSet.getString("unit"));
                offerTypes.add(offerType);
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
        return offerTypes;
    }
}
