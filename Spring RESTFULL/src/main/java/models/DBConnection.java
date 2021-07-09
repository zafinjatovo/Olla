package models;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{   
    public static Connection connect() throws IOException, URISyntaxException 
    {
        Connection conn = null;
        try 
        {
            //online1----------
            URI dbUri = new URI("postgres://xgcpswxnrykixy:8107cb8ada5af6edbed4d2800855173663016e6656682da6f278d983e96d610a@ec2-52-19-170-215.eu-west-1.compute.amazonaws.com:5432/dfc72ctu54dqo2");
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            conn = DriverManager.getConnection(dbUrl, username, password);
            
            //online2----------
            /*String url ="jdbc:postgresql://postgresql-26417-0.cloudclusters.net:26417/AppelTelephonique";
            String user = "Ollamobile";
            String password = "12345678";
            conn = DriverManager.getConnection(url, user, password);*/
            
            //offline---------
            /*FileReader reader = new FileReader("src\\main\\resources\\db.properties");
            Properties p = new Properties();
            p.load(reader);
            
            String url = p.getProperty("url");
            String user = p.getProperty("user");
            String password = p.getProperty("password");
            conn = DriverManager.getConnection(url, user, password);*/
            
        } 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static MongoDatabase connectMongo() throws Exception
    {
        MongoDatabase database = null;
        try
        {
            MongoClient mongoClient = MongoClients.create("mongodb+srv://tokiniaina:root@cluster0.1bklf.mongodb.net/ContactDB?retryWrites=true&w=majority");
            database = mongoClient.getDatabase("ContactDB");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return database;
    }
    
}