package viewModeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.DBConnection;

public class Usernombre {
    int idOperator;
    String nameOperator;
    double nombre;

    public Usernombre() {
    }

    public Usernombre(int idOperator, String nameOperator, double nombre) {
        this.idOperator = idOperator;
        this.nameOperator = nameOperator;
        this.nombre = nombre;
    }

    public int getIdOperator() {
        return idOperator;
    }

    public void setIdOperator(int idOperator) {
        this.idOperator = idOperator;
    }

    public String getNameOperator() {
        return nameOperator;
    }

    public void setNameOperator(String nameOperator) {
        this.nameOperator = nameOperator;
    }

    public double getNombre() {
        return nombre;
    }

    public void setNombre(double nombre) {
        this.nombre = nombre;
    }
    
    
    public static ArrayList<Usernombre> getNombreUser(Connection connection) throws Exception{
        ArrayList<Usernombre> results=new ArrayList<>();
        String sql="select * from v_user_nombre";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                Usernombre nombre=new Usernombre(r.getInt(1),r.getString(2), r.getDouble(3));
                results.add(nombre);
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
       
        return  results;
    }
}
