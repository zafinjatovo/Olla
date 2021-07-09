/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import models.requestBody.UserLogin;
import models.responseBody.Response;
import org.bson.Document;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import utils.Util;

/**
 *
 * @author Tokiniaina
 */
@EnableMongoRepositories(basePackages = "com.applesauce.repository")
@Service
public class User 
{
    
    int id;
    int idOperator;
    String name;
    String firstname;
    String email;
    String password;
    String NCin;
    String phoneNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOperator() {
        return idOperator;
    }

    public void setIdOperator(int idOperator) {
        this.idOperator = idOperator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public String getNCin() {
        return NCin;
    }

    public void setNCin(String NCin) {
        this.NCin = NCin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, int idOperator, String name, String firstname, String NCin, String phoneNumber) {
        this.id = id;
        this.idOperator = idOperator;
        this.name = name;
        this.firstname = firstname;
        this.NCin = NCin;
        this.phoneNumber = phoneNumber;
    }
    
    
    public User(int id, int idOperator, String name, String firstname, String email, String password, String NCin) 
    {
        this.id = id;
        this.idOperator = idOperator;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.NCin = NCin;
    }

    public User(int idOperator, String name, String firstname, String email, String password, String NCin) 
    {
        this.idOperator = idOperator;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.NCin = NCin;
    }

    public User(int id, int idOperator, String name, String firstname, String email, String password, String NCin, String phoneNumber) {
        this.id = id;
        this.idOperator = idOperator;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.NCin = NCin;
        this.phoneNumber = phoneNumber;
    }

    public User(int id, int idOperator, String name, String firstname, String NCin) {
        this.id = id;
        this.idOperator = idOperator;
        this.name = name;
        this.firstname = firstname;
        this.NCin = NCin;
    }
    
    
    
    public int save(Connection connection) throws Exception
    { 
        int idUser = -1;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
               // connection.setAutoCommit(false);
            } 
            String sql = "INSERT INTO t_User (idOperator, name, firstname, email, password, NCin,dateinscription) VALUES (" + this.getIdOperator() + ", '" +  this.getName() + "', '" + this.getFirstname() + "', '" + this.getEmail() + "', '" + this.getPassword() + "', '" + this.getNCin() + "','"+ Util.nowString() +"');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            sql = "select id from t_User order by id desc LIMIT 1";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();
            if(resultSet.next())
            {
                idUser = resultSet.getInt("id");
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
            { //  connection.commit();
                connection.close();
            }
        }
        return idUser;
    }    
    
    public static String GenerateToken(Connection connection, int idUser) throws Exception
    {
        String token = "";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "INSERT INTO t_token(iduser, value, expiration) VALUES (" + idUser + ", MD5(CONCAT(now()," + idUser + ")), CURRENT_TIMESTAMP + '3 day');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            sql = "select value from t_token where idUser = " + idUser + " order by id desc LIMIT 1;";
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
    
    public static int GetUserIdByToken(Connection connection, String token) throws Exception
    {
        int ans = -1;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select idUser from t_token where value = '" + token + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                ans = resultSet.getInt("idUser");
            }
            preparedStatement.close();
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
    
    public static User FindById(Connection connection, int id) throws Exception
    {
        User user = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_User where id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                user = new User(resultSet.getInt("id"), resultSet.getInt("idOperator"), resultSet.getString("name"), resultSet.getString("firstname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("ncin"));
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
        return user;
    }
    
    public static Response connect(UserLogin user) throws Exception
    {
        //select * from v_user where email='zafinjatovolouisparfait@gmail.com' and password='01011101'
        String sql="select * from v_user where email='" + user.getEmail() + "' and password='" + user.getPassword() + "'";
        Response data =new Response();
        Connection connection=DBConnection.connect();
        PreparedStatement statement=connection.prepareStatement(sql);
        
        try {
            ResultSet r=statement.executeQuery();
            String token="";
                data.setStatus("error");
                while(r.next()){
                    System.out.println(r.getInt(1));
                    // check si deja confirmer
                    if(!r.getString(8).equals("-1")){
                       token=User.findTokenById(r.getInt(1));
                       if("".equals(token)){
                           token=User.GenerateToken(connection,r.getInt(1));
                       }
                       data.setStatus("succes");
                       data.setData(token);
                    }else{
                        data.setStatus("not confirmed");
                    }
                    break;
                }
            r.close();
        } 
        catch (SQLException e) {
            data.setStatus("error");
        }
        finally
        {
            if(statement!=null) statement.close();
            connection.close();
        }
        return data;
    }
   
    static void UpdateLastUsingPhoneNumber(Connection connection) throws Exception
    { 
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "insert into t_lastphonenumber (label) values ('t');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
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
    }
    
    String GetPhoneNumber(Connection connection) throws Exception
    {
        String newPhoneNumber = "";
        try
        {
            User.UpdateLastUsingPhoneNumber(connection);
            
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            //maka prefix
            String sql = "select preffix from t_operator where id = " + this.getIdOperator() + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                newPhoneNumber = resultSet.getString("preffix");
            }
            
            resultSet.close();
            preparedStatement.close();
            
            //maka ny numero farana sans prefix
            sql = "SELECT nextphonenumber FROM t_lastphonenumber order by nextphonenumber desc LIMIT 1;";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                newPhoneNumber = newPhoneNumber + resultSet.getInt("nextphonenumber");
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
        return newPhoneNumber;
    }
    
    public boolean AddNewPhoneNumberToUser(Connection connection, String newPhoneNumber) throws Exception
    { 
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "insert into t_phonenumber (iduser, number) values (" + this.getId() + ", '" + newPhoneNumber + "');";
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
    
    public static String findTokenById(int id) throws Exception
    {
        String token="";
        String sql="SELECT VALUE FROM T_TOKEN WHERE EXPIRATION>current_timestamp AND IDUSER=" + id;
        Connection connection=DBConnection.connect();
        PreparedStatement statement=connection.prepareStatement(sql);
        
        try {
            ResultSet r=statement.executeQuery();
            while(r.next()){
                token=r.getString(1);
                break;
            }
            r.close();
        } catch (SQLException e) {
            token="";
        }finally{
            if(statement!=null) statement.close();
            connection.close();
        }
        return token;
    }
    
    //return new token validation
    String SignUp(Connection connection)throws Exception
    {
        String token = "";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            int id = this.save(connection);
            if(id != -1)
            {
                token = User.GenerateToken(connection, id);
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
        return token;
    }
    
    public static User FindUserAuthentifiedById(Connection connection, int id) throws Exception
    {
        User user = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_user where id = " + id + " and number != '-1'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                user = new User(resultSet.getInt("id"), resultSet.getInt("idOperator"), resultSet.getString("name"), resultSet.getString("firstname"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("ncin"), resultSet.getString("number"));
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
        return user;
    }
    
    public static void updateUser(Connection connection,int id,String name,String firstname,String cin) throws SQLException{
        String sql="update t_user set name='" + name + "' , firstname='" + firstname + "', ncin='" + cin + "' where id="+ id;
         try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
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
    }
    public boolean SignUpAndSendEmail(Connection connection, String url) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String token = this.SignUp(connection);
            if(token.compareTo("") != 0)
            {
                ans = User.GeneratePhoneNumberAfterEmailConfirmation(connection, token);
            }
            else
            {
                ans = false;
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
    
    public static boolean GeneratePhoneNumberAfterEmailConfirmation(Connection connection, String token) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            int id = User.GetUserIdByToken(connection, token);
            User user = User.FindById(connection, id);
            String newPhoneNumber = user.GetPhoneNumber(connection);
            if(user.AddNewPhoneNumberToUser(connection, newPhoneNumber))
            {
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
    
    public static User GetAuthentification(Connection connection, String token) throws Exception
    {
        User user = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            int id = User.GetUserIdByToken(connection, token);
            user = User.FindUserAuthentifiedById(connection, id);
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
        return user;
    }
    
    public boolean MakeAction(Connection connection, ActionMobileMoney action, boolean  closeConnection) throws  Exception
    {
        boolean ans = false;
        try
        {
            action.setUser(this);
            ans = action.save(connection, false);
        }
        catch(Exception ex)
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
    
    public static User FindUserByPhoneNumber(Connection connection, String phoneNumber) throws Exception
    {
        User user = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_user where number = '" + phoneNumber + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                user = new User(resultSet.getInt("id"), resultSet.getInt("idoperator"), resultSet.getString("name"), resultSet.getString("firstname"), resultSet.getString("ncin"), phoneNumber);
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
        return user;
    }
    
    public List<ActionMobileMoney> GetHistoriqueAction(Connection connection) throws Exception
    {
        List<ActionMobileMoney> actions = new ArrayList<ActionMobileMoney>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_mobilemoney where v_mobilemoney.idUser = " + this.getId() + " and ((transactiontype = 'deposit' and confirmed = true) or (transactiontype<> 'deposit')) order by date desc;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                ActionMobileMoney action = new ActionMobileMoney(resultSet.getInt("id"), new User(resultSet.getInt("id"), resultSet.getInt("idoperator"), resultSet.getString("name"), resultSet.getString("firstname"), resultSet.getString("ncin")), resultSet.getDouble("cash"), ActionMobileMoney.Transaction.valueOf(resultSet.getString("transactiontype")) , resultSet.getTimestamp("date").toLocalDateTime());
                actions.add(action);
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
        return actions;
    }
    
    public Double GetSolde(Connection connection, LocalDateTime date) throws Exception
    {
        double solde = 0;
        try
        {
            double deposit = 0;
            double withdrawal = 0;
            double buy = 0;
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select sum(cash) as total, transactiontype from v_mobilemoney where date <= '" + date + "' and idUser = " + this.getId() + " and transactiontype = 'deposit' and confirmed = 'true' group by transactiontype;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                deposit = resultSet.getDouble("total");
            }
            resultSet.close();
            preparedStatement.close();
            
            sql = "select sum(cash) as total, transactiontype  from v_mobilemoney where date <= '" + date + "' and idUser = " + this.getId() + " and transactiontype = 'withdrawal' group by transactiontype;";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                withdrawal = resultSet.getDouble("total");
            }
            resultSet.close();
            preparedStatement.close();
            
            sql = "select sum(cash) as total, transactiontype from v_mobilemoney where date <= '" + date + "' and idUser = " + this.getId() + " and transactiontype = 'buy' group by transactiontype;";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                buy = resultSet.getDouble("total");
            }
            resultSet.close();
            preparedStatement.close();
            
            solde = deposit - withdrawal - buy;
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
        return solde;
    }
    
    public boolean IsEnought(Connection connection, ActionMobileMoney action) throws Exception
    {
        boolean ans = false;
        try
        {
            double solde = this.GetSolde(connection, action.getDate());
            if(solde >= action.getCash())
            {
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

    public boolean SaveContact(Contact contact)
    {
        MongoDatabase dbmongo;
        boolean ans = false;
        try
        {
            dbmongo = DBConnection.connectMongo();
            MongoCollection collection = dbmongo.getCollection("Contact");
            contact.setIdContactOwner(String.valueOf(this.getId()));
            collection.insertOne(contact.toDocument());
            ans = true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ans;
    }
    
    public List<Contact> GetContacts()
    {
        List<Contact> contacts = new ArrayList<Contact>();
        MongoDatabase dbmongo;
        try
        {
            dbmongo = DBConnection.connectMongo();
            MongoCollection collection = dbmongo.getCollection("Contact");
            Document query = new Document("idContactOwner", String.valueOf(this.getId()));
            FindIterable<Document> iterobj = (FindIterable<Document>) collection.find(query);
            Iterator itr = iterobj.iterator(); 
            while (itr.hasNext()) 
            { 
                Document doc = (Document) itr.next();
                Contact contact = new Contact(doc.get("_id").toString() , doc.get("idContactOwner").toString(), doc.get("name").toString(), doc.get("firstname").toString(), doc.get("phonenumber").toString());
                contacts.add(contact);
            } 
            iterobj.cursor().close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return contacts;
    }
    
    public boolean InsertCredit(Connection connection, double valueCredit, LocalDateTime date) throws Exception
    {
        boolean ans = false;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "insert into t_credit (iduser, valuecredit, date) values (" + this.getId() + ", " + valueCredit + ", '" + date + "');";
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
    
    public double GetSoldeCredit(Connection connection, LocalDateTime date) throws Exception
    {
        double soldeCredit = 0;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select sum(credit) as soldecredit from v_actioncredit where iduser = " + this.getId() + " and date <= '" + date + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                soldeCredit = resultSet.getDouble("soldecredit");
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
        return soldeCredit;
    }
            
    public Response BuyOffer(Connection connection, Purchase purchase) throws Exception
    {
        Response response  = null;
        try
        {
            double soldeUser = 0;
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            if(purchase.typepurchase == 0)
            {
                soldeUser = this.GetSoldeCredit(connection, purchase.getDate());
            }
            else
            {
                soldeUser = this.GetSolde(connection, purchase.getDate());
            }
            double price = purchase.getOffer().getPrice();
            if(soldeUser >= price)
            {
                if(connection.isClosed())
                {
                    connection = DBConnection.connect();
                }
                connection.setAutoCommit(false);
                String sql = "INSERT INTO t_purchase (iduser, typepurchase, idoffer, date) VALUES (" + this.getId() + "," + purchase.getTypepurchase() + ", " + purchase.getOffer().getId() + ", '" + purchase.getDate() +"');";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                if(purchase.typepurchase != 0)
                {
                    ActionMobileMoney action = new ActionMobileMoney(price, ActionMobileMoney.Transaction.buy, purchase.getDate());
                    if(this.MakeAction(connection, action , false))
                    {
                        connection.commit();
                        response = new Response("succes" , "");
                    }
                    else
                    {
                        connection.rollback();
                        response = new Response("error" , "buy offer failed");
                    }
                }
                else
                {
                    connection.commit();
                    response = new Response("succes" , "");
                }
            }
            else
            {
                response = new Response("error" , "sold insuficient");
            }
        }
        catch(Exception ex)
        {
            connection.rollback();
            ex.printStackTrace();
        }
        finally
        {
            if((connection != null) && (connection.isClosed() == false))
            {
                connection.close();
            }
        }
        return response;
    }
    
    
    
    public List<CallPurchase> GetCallPurchaseList(Connection connection, LocalDateTime date) throws Exception
    {
        List<CallPurchase> callpurchases = new ArrayList<CallPurchase>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_purchase where typevalue = 'call' and iduser = " + this.getId() + " and datepurchase <= '" + date + "' order by datepurchase asc;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                CallPurchase callPurchase = new CallPurchase(resultSet.getInt("idoffer"), resultSet.getString("name"), resultSet.getDouble("value"), resultSet.getDouble("consumptionunit"), resultSet.getDouble("otherconsumptionunit"), resultSet.getTimestamp("datepurchase").toLocalDateTime(), resultSet.getTimestamp("dateexpiration").toLocalDateTime());
                callpurchases.add(callPurchase);
            }
            preparedStatement.clearBatch();
            preparedStatement.close();
            resultSet.close();
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
        return callpurchases;
    }
    
    public List<CallConsumption> GetCallConsumptionList(Connection connection, LocalDateTime date) throws Exception
    {
        List<CallConsumption> callConsumptions = new ArrayList<CallConsumption>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from v_callconsumption where iduser = " + this.getId() + " and v_callconsumption.date <= '" + Timestamp.valueOf(date) + "' order by v_callconsumption.date asc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                CallConsumption callConsumption = new CallConsumption(resultSet.getInt("id"), resultSet.getInt("idreceipter"), resultSet.getInt("operatorreceipter"), resultSet.getDouble("duration"), resultSet.getTimestamp("date").toLocalDateTime());
                callConsumptions.add(callConsumption);
            }
            preparedStatement.close();
            resultSet.close();
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
        return callConsumptions;
    }
    
    public List<CallPurchase> GetSoldCallLeft(Connection connection, LocalDateTime date) throws Exception
    {
        List<CallPurchase> ans = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            List<CallPurchase> callPurchases = this.GetCallPurchaseList(connection, date);
            List<CallConsumption> callConsumptions = this.GetCallConsumptionList(connection, date);
            List<CallPurchase> callPurchaseLeft = new ArrayList<CallPurchase>();
            for(int i = 0;i<callConsumptions.size();i++)
            {
                CallConsumption callconsumptionTemp = callConsumptions.get(i);
                callPurchaseLeft.addAll(callconsumptionTemp.RemoveCallPurchase(callPurchases));
                callPurchaseLeft = CallPurchase.FusionCallPurchase(callPurchaseLeft);
                callPurchaseLeft = callconsumptionTemp.RemoveOutOfDate(callPurchaseLeft);
                callPurchaseLeft = callconsumptionTemp.Consume(callPurchaseLeft, this);
            }
            ans = callPurchaseLeft;
            ans.addAll(callPurchases);
            ans = CallPurchase.FusionCallPurchase(ans);
            ans = CallPurchase.RemoveOutOfDate(ans, date);
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
    
    public double[] GetCallDurationSold(Connection connection, LocalDateTime date) throws Exception
    {
        double [] ans = new double[2];
        ans[0] = 0;
        ans[1] = 0;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            List<CallPurchase> callPurchaseleft = this.GetSoldCallLeft(connection, date);
            for(int i = 0;i<callPurchaseleft.size();i++)
            {
                CallPurchase temp = callPurchaseleft.get(i);
                ans[0] = ans[0] + (temp.getValue() / temp.getConsumptionUnit()); 
                ans[1] = ans[1] + (temp.getValue() / temp.getOtherConsumptionUnit()); 
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
    
    public Response SimulationCall(Connection connection, CallConsumption call) throws Exception
    {
        Response respons = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            double[] soldeOtherAndSame = this.GetCallDurationSold(connection, call.getDate());
            double solde = 0;
            if(this.getIdOperator() == call.getReceipterIdOperator())
            {
                solde = soldeOtherAndSame[0];
            }
            else
            {
                solde = soldeOtherAndSame[1];
            }
            
            if(call.getCallduration() <= solde)
            {
                call.Save(connection, this);
                respons = new Response("succes", "");
            }
            else
            {
                respons = new Response("error", "solde insuficient, you have " + soldeOtherAndSame[0] + " ar to intern and " + soldeOtherAndSame[1] + " ar to extern");
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
        return respons;
    }
    
    
    public  Response GetSoldOffer(Connection connection, int idOffer, LocalDateTime date) throws Exception
    {
        Response response = null;
        try
        {
            Offer offer = Offer.GetOfferById(connection, idOffer);
            HashMap<String, String> offerSold =  offer.GetUserSold(connection, this, date);
            response = new Response("succes", offerSold);
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
        return response;
    }
    
    public Response GetHistoriqueCall(Connection connection, LocalDateTime date) throws Exception
    {
        Response response = null;
        List<CallMade> callHistoric = new ArrayList<CallMade>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
                String sql ="select \n" +
                            "	v_callconsumption.*,\n" +
                            "	v_user.id as interlocid, \n" +
                            "	v_user.name as interlocName, \n" +
                            "	v_user.firstname as interlocFName, \n" +
                            "	v_user.number as interlocPHNumber  \n" +
                            "from \n" +
                            "	v_callconsumption \n" +
                            "		inner join \n" +
                            "	v_user \n" +
                            "on \n" +
                            "	v_callconsumption.iduser = v_user.id\n" +
                            "where\n" +
                            "	v_callconsumption.idreceipter = " + this.getId() + "\n" +
                            "union all\n" +
                            "select \n" +
                            "	v_callconsumption.*,\n" +
                            "	v_user.id as interlocid, \n" +
                            "	v_user.name as interlocName, \n" +
                            "	v_user.firstname as interlocFName, \n" +
                            "	v_user.number as interlocPHNumber  \n" +
                            "from \n" +
                            "	v_callconsumption \n" +
                            "		inner join \n" +
                            "	v_user \n" +
                            "on \n" +
                            "	v_callconsumption.idreceipter = v_user.id\n" +
                            "where\n" +
                            "	v_callconsumption.iduser = " + this.getId() + " \n" +
                            "order by date desc;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String calltype = null;
                if(resultSet.getInt("iduser") != resultSet.getInt("interlocid"))
                {
                    calltype = "entrant";
                }
                else
                {
                    calltype = "sortant";
                }
                CallMade callMade = new CallMade(resultSet.getString("interlocname") + " " + resultSet.getString("interlocfname"), resultSet.getString("interlocphnumber"), resultSet.getTimestamp("date").toLocalDateTime(), calltype);
                callHistoric.add(callMade);
            }
            response = new Response("succes", callHistoric);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            response = new Response("error" , "error in the server");
        }
        finally
        {
            if((connection != null) && (connection.isClosed() == false))
            {
                connection.close();
            }  
        }
        return response;
    }
    
    public Operator GetOperator(Connection connection) throws Exception
    {
        Operator operator = null;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            String sql = "select * from t_Operator where id = " + this.getIdOperator();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                operator = new Operator(this.getIdOperator(), resultSet.getString("name"), resultSet.getString("preffix"));
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
        return operator;
    }
}

