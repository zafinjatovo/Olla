package viewModeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.DBConnection;
import models.Operator;

public class ChiffreAffaireOperator {
    Operator operator;
    double chiffreAffaire;
    ArrayList<ChiffreAffaireMonth> detailsMonth;

    public ChiffreAffaireOperator() {
    }

    public ChiffreAffaireOperator(Operator operator, double chiffreAffaire, ArrayList<ChiffreAffaireMonth> detailsMonth) {
        this.operator = operator;
        this.chiffreAffaire = chiffreAffaire;
        this.detailsMonth = detailsMonth;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    public ArrayList<ChiffreAffaireMonth> getDetailsMonth() {
        return detailsMonth;
    }

    public void setDetailsMonth(ArrayList<ChiffreAffaireMonth> detailsMonth) {
        this.detailsMonth = detailsMonth;
    }
    
    public static ArrayList<ChiffreAffaireOperator> getChiffreAffaire(Connection connection)throws Exception {
        ArrayList<ChiffreAffaireOperator> results=new ArrayList<>();
        String sql="select * from v_cf_operatorFinal";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                Operator opetator=new Operator(r.getInt(1),r.getString(2),r.getString(3));
                double chiffreAffaire=r.getDouble(4);
                ArrayList<ChiffreAffaireMonth> detailsMonth=ChiffreAffaireMonth.getFromMonth(connection,opetator.getId());
                ChiffreAffaireOperator tempResult=new ChiffreAffaireOperator(opetator, chiffreAffaire,detailsMonth);
                results.add(tempResult);
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
    
    public static double getChiffreAffaireTotal(Connection connection)throws Exception{
        double result=0;
        String sql="select coalesce(sum(chiffreAffaire),0) from v_cf_operatorFinal";
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                result=r.getDouble(1);
                break;
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
         
        return result;
    }
}
