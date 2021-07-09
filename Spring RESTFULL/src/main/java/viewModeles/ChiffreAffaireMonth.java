
package viewModeles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import models.DBConnection;
import utils.Util;

public class ChiffreAffaireMonth {
    double chiffreAffaire;
    int month;
    int year;

    public ChiffreAffaireMonth() {
    }

    public ChiffreAffaireMonth(double chiffreAffaire, int month, int year) {
        this.chiffreAffaire = chiffreAffaire;
        this.month = month;
        this.year = year;
    }
    
    
    public void setChiffreAffaire(double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
   
    
   public static ArrayList<ChiffreAffaireMonth> getFromMonth(Connection connection,int idOperator) throws Exception{
       ArrayList<ChiffreAffaireMonth> results=new ArrayList<>();
       Calendar cal=Util.NowtoCalendar();
       String sql="select idOperator,getChiffreAffaire(month,year,idOperator) as chiffreAffaire,month,year from v_cf_operatorBymonth where idOperator="+ idOperator +" and year="+ cal.get(Calendar.YEAR) +" order by month";
       
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
               ChiffreAffaireMonth temp=new ChiffreAffaireMonth(r.getDouble(2),r.getInt(3),r.getInt(4));
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

    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

}
