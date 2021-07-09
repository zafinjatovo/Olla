package viewModeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.DBConnection;

public class AppelHistorique {
    int numero;
    int durer;
    String dateHeure;

    public AppelHistorique() {
    }
    public AppelHistorique(int numero, int durer, String dateHeure) {
        this.numero = numero;
        this.durer = durer;
        this.dateHeure = dateHeure;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDurer() {
        return durer;
    }

    public void setDurer(int durer) {
        this.durer = durer;
    }

    public String getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }
    
    
    public static ArrayList<AppelHistorique> getHistorique(Connection connection,String type,int idUser)throws Exception{
        String sql="";
        ArrayList<AppelHistorique> result=new ArrayList<>();
        if(type.equals("in")){ // niantso anah
            sql="select numbercaller,duration,date from v_call_2 where idreceipter=" + idUser;
        }else if(type.equals("out")){ //natsoiko
            sql="select numberreceipter,duration,date from v_call_2 where idUser=" + idUser;
        }
        System.out.println(sql);
       try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                AppelHistorique appel=new AppelHistorique(r.getInt(1),r.getInt(2), r.getString(3));
                result.add(appel);
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
        return  result;
    }
}
