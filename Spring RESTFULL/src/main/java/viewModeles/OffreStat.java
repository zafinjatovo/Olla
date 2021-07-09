package viewModeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.DBConnection;
import models.Operator;

public class OffreStat {
    int id;
    String name;
    int idOperator;
    double nombreAcheteur;

    public OffreStat() {
    }

    public OffreStat(int id, String name, int idOperator, double nombreAcheteur) {
        this.id = id;
        this.name = name;
        this.idOperator = idOperator;
        this.nombreAcheteur = nombreAcheteur;
    }

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

    public int getIdOperator() {
        return idOperator;
    }

    public void setIdOperator(int idOperator) {
        this.idOperator = idOperator;
    }

    public double getNombreAcheteur() {
        return nombreAcheteur;
    }

    public void setNombreAcheteur(double nombreAcheteur) {
        this.nombreAcheteur = nombreAcheteur;
    }
    
    
    public static ArrayList<OffreStat> getStatOffre(Connection connection,int idOperator)throws Exception{
        ArrayList<OffreStat> results=new ArrayList<>();
        String sql="select * from v_offre_nombreFinal where idoperator=" + idOperator;
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                OffreStat temp=new OffreStat(r.getInt(1),r.getString(2),r.getInt(3),r.getDouble(4));
                results.add(temp);
            }
            r.close();
            preparedStatement.close();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }finally{
            if(connection!=null) connection.close();
        }
        return results;
    }
}
