
package viewModeles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.DBConnection;
import models.User;

public class ChiffreAffaireUser {
    User user;
    double ChiffreAffaire;

    public ChiffreAffaireUser() {
    }

    public ChiffreAffaireUser(User user, double ChiffreAffaire) {
        this.user = user;
        this.ChiffreAffaire = ChiffreAffaire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getChiffreAffaire() {
        return ChiffreAffaire;
    }

    public void setChiffreAffaire(double ChiffreAffaire) {
        this.ChiffreAffaire = ChiffreAffaire;
    }
    
    public static ArrayList<ChiffreAffaireUser> getByOperator(Connection connection,int idOperator,int topN) throws Exception{
        String sql=" select * from v_user_cf_final where idoPerator=" + idOperator + " fetch first "+ topN +" rows only";
        ArrayList<ChiffreAffaireUser> results=new ArrayList<ChiffreAffaireUser>();
        try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r = preparedStatement.executeQuery();
            while(r.next())
            {
                User user = new User(r.getInt("id"), r.getInt("idOperator"), r.getString("name"), r.getString("firstname"), r.getString("email"), r.getString("password"), r.getString("ncin"));
                ChiffreAffaireUser chiffreAffaireUser=new ChiffreAffaireUser(user,r.getDouble(9));
                results.add(chiffreAffaireUser);
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
        return results;
    }
}
