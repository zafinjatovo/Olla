package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Offer
{
    int id;
    Operator operator;
    String name;
    double price;
    double duration;
    List<OfferComposant> offerComposants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double validateDuration) {
        this.duration = validateDuration;
    }

    public List<OfferComposant> getOfferComposants() {
        return offerComposants;
    }

    public void setOfferComposants(List<OfferComposant> offerComposants) {
        this.offerComposants = offerComposants;
    }
    
    public Offer() 
    {
    }

    public Offer(Operator operator, String name, double price, double duration, List<OfferComposant> offerComposants) {
        this.operator = operator;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.offerComposants = offerComposants;
    }
    
    public Offer(int id, Operator operator, String name, double price, double duration, List<OfferComposant> offerComposants) {
        this.id = id;
        this.operator = operator;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.offerComposants = offerComposants;
    }
   
    
    public int GetLastOfferId(Connection connection, boolean closeConnection) throws Exception
    {
        int id = -1;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select id from t_offer order by id desc limit 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            if(resultSet.next())
            {
                id = resultSet.getInt("id");
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
            if((connection != null) && (connection.isClosed() == false) && (closeConnection))
            {
                connection.close();
            }
        }
        return id;
    }
    
    String FormSqlInsertionForComposantst()
    {
        String sql = "";
        for(int i = 0;i<this.getOfferComposants().size();i++)
        {
            OfferComposant offreComposant = this.getOfferComposants().get(i);
            String newSql = "INSERT INTO t_OfferDetail (idtype, idoffer, value, consumptionUnit, otherConsumptionUnit) VALUES (" + offreComposant.getType().getId() + ", " + this.getId() + ", " + offreComposant.getValue() + ", " + offreComposant.getConsumptionUnit() + ", " + offreComposant.getOtherConsumptionUnit()+ "); \n";
            sql = sql + newSql;
        }
        return sql;
    }
    
    public boolean Save(Connection connection) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            String sql = "INSERT INTO t_offer (idoperator, name, price, duration) VALUES (" + this.getOperator().getId() + ", '" +  this.getName() + "', " + this.getPrice() + ", " + this.getDuration() + ");";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            int id = this.GetLastOfferId(connection, false);
            this.setId(id);
            sql = this.FormSqlInsertionForComposantst();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            ans = true;
            connection.commit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            connection.rollback();
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
    
    public static Offer GetOfferById(Connection connection, int id) throws Exception
    {
        Offer ans = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_offer where idOffer = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            int count = 0;
            Offer offer = new Offer();
            List<OfferComposant> offerComposants = new ArrayList<OfferComposant>();
            while(resultSet.next())
            {
                if(count == 0)
                {
                  offer = new Offer(resultSet.getInt("idOffer"), new Operator(resultSet.getInt("idOperator")), resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("duration"), null);
                  offerComposants = new ArrayList<OfferComposant>();
                }
                
                OfferComposant offerComposant = new OfferComposant(new OfferType(resultSet.getInt("idoffertype"), resultSet.getString("typevalue"), resultSet.getString("unit")), resultSet.getDouble("value"), resultSet.getDouble("consumptionunit"), resultSet.getDouble("otherconsumptionunit"));
                offerComposants.add(offerComposant);
                count++;
                
                if(resultSet.isLast())
                {
                    offer.setOfferComposants(offerComposants);
                }
            }
            ans = offer;
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
        return ans;
    }
    
    String [][] GetOfferTypeUnitStr()
    {
        String [][] ans = new String[this.getOfferComposants().size()][2];
        List<OfferComposant> offerComposants = this.getOfferComposants();
        for(int i = 0;i<offerComposants.size();i++)
        {
            ans[i][0] = this.getOfferComposants().get(i).getType().getName();
            ans[i][1] = this.getOfferComposants().get(i).getType().getUnit();
        }
        return ans;
    }
    
    List<OtherPurchase> GetAllOtherPurchases(Connection connection, User user, LocalDateTime date) throws Exception
    {
        List<OtherPurchase> otherPurchases = new ArrayList<OtherPurchase>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_purchase where idoffer = " + this.getId() + " and typevalue <> 'call' and idUser = " + user.getId() + " and datepurchase <= '" + date + "' order by datepurchase desc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                OtherPurchase otherPurchase = new OtherPurchase(resultSet.getInt("idoffertype"), resultSet.getString("typevalue").toLowerCase(), resultSet.getDouble("value"), resultSet.getTimestamp("datepurchase"), resultSet.getTimestamp("dateexpiration"));
                otherPurchases.add(otherPurchase);
            }
            preparedStatement.close();
            
            if(otherPurchases.size() != 0)
            {
                OtherPurchase last = otherPurchases.get(0);
                if(last.getDateexpiration().compareTo(Timestamp.valueOf(date)) < 0)
                {
                    otherPurchases = new ArrayList<OtherPurchase>();
                }
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
        return otherPurchases;
    }
    
    public HashMap<String, String> GetUserSold(Connection connection, User user, LocalDateTime date) throws Exception
    {
        HashMap<String, String> sold = new HashMap<String, String>();
        String [][] offerTypeStr = this.GetOfferTypeUnitStr();
        List<OtherPurchase> otherPurchases = this.GetAllOtherPurchases(connection, user, date);
        for(int i = 0;i<offerTypeStr.length;i++)
        {
            if(offerTypeStr[i][0].trim().compareTo("call") != 0)
            {
                double value = 0;
                int count = 0;
                Timestamp t = null;
                for(int j = 0;j<otherPurchases.size();j++)
                {
                    OtherPurchase temp = otherPurchases.get(j);
                    if((count == 0) && (temp.getOfferType().toLowerCase().compareTo(offerTypeStr[i][0].toLowerCase())==0))
                    {
                        value = value + temp.getValue();
                        t = temp.getDatepurchase();
                        count++;
                    }
                    else if((count != 0) && (temp.getOfferType().toLowerCase().compareTo(offerTypeStr[i][0].toLowerCase().trim()) == 0))
                    {
                        if(temp.HasIntersectionDate(t) == true)
                        {
                            value = value + temp.getValue();
                            t = temp.getDatepurchase();
                        }
                    }
                }
                sold.putIfAbsent(offerTypeStr[i][0], value + " " + offerTypeStr[i][1]);
            }
            else
            {
                List<CallPurchase> callpurchases = user.GetSoldCallLeft(connection, date);
                String intern = "0";
                String extern = "0";
                for(int u = 0;u<callpurchases.size();u++)
                {
                    CallPurchase callPurchase = callpurchases.get(u);
                    if(callPurchase.getIdOffer() == this.getId())
                    {
                        intern = String.valueOf(callPurchase.getValue() / callPurchase.getConsumptionUnit());
                        extern = new DecimalFormat("##.###").format(callPurchase.getValue() / callPurchase.getOtherConsumptionUnit());
                        break;
                    }
                }
                sold.putIfAbsent(offerTypeStr[i][0], "intern: " + intern + " ar, extern: " + extern + " ar");
            }
        }
        return sold;
    }
}
