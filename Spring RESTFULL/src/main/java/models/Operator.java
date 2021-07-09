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
public class Operator 
{
    int id;
    String name;
    String preffix;

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

    public String getPreffix() {
        return preffix;
    }

    public void setPreffix(String preffix) {
        this.preffix = preffix;
    }

    public Operator() {
    }

    public Operator(int id) {
        this.id = id;
    }
    
    public Operator(int id, String name, String preffix) {
        this.id = id;
        this.name = name;
        this.preffix = preffix;
    }
    
    public static List<Operator> GetOperators(Connection connection) throws Exception
    {
        List<Operator> operators = new ArrayList<Operator>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_operator";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            while(resultSet.next())
            {
                Operator operator = new Operator(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("preffix"));
                operators.add(operator);
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
        return operators;
    }
    
    public List<Offer> GetOffers(Connection connection) throws Exception
    {
        List<Offer> offers = new ArrayList<Offer>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_offer where idoperator = " + this.getId() + " order by idoffer asc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            int count = 0;
            Offer offer = new Offer();
            List<OfferComposant> offerComposants = new ArrayList<OfferComposant>();
            while(resultSet.next())
            {
                if(count == 0)
                {
                  offer = new Offer(resultSet.getInt("idOffer"), this, resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("duration"), null);
                  offerComposants = new ArrayList<OfferComposant>();
                }
                
                if(offer.getId() != resultSet.getInt("idOffer"))
                {
                    offer.setOfferComposants(offerComposants);
                    offers.add(offer);
                    
                    offer = new Offer(resultSet.getInt("idOffer"), this, resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("duration"), null);
                    offerComposants = new ArrayList<OfferComposant>();
                }
                
                OfferComposant offerComposant = new OfferComposant(new OfferType(resultSet.getInt("idoffertype"), resultSet.getString("typevalue"), resultSet.getString("unit")), resultSet.getDouble("value"), resultSet.getDouble("consumptionunit"), resultSet.getDouble("otherconsumptionunit"));
                offerComposants.add(offerComposant);
                count++;
                
                if(resultSet.isLast())
                {
                    offer.setOfferComposants(offerComposants);
                    offers.add(offer);
                }
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
        return offers;
    }
    
    
}
