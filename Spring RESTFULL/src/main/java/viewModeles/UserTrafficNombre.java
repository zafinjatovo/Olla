
package viewModeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import models.DBConnection;
import utils.Util;


public class UserTrafficNombre {
    
    int lastTraffic;
    int nowTrafic;
    int lastUser;
    int nowUser;

    public UserTrafficNombre() {
    }

    public UserTrafficNombre(int lastTraffic, int nowTrafic, int lastUser, int nowUser) {
        this.lastTraffic = lastTraffic;
        this.nowTrafic = nowTrafic;
        this.lastUser = lastUser;
        this.nowUser = nowUser;
    }

    public int getLastTraffic() {
        return lastTraffic;
    }

    public void setLastTraffic(int lastTraffic) {
        this.lastTraffic = lastTraffic;
    }

    public int getNowTrafic() {
        return nowTrafic;
    }

    public void setNowTrafic(int nowTrafic) {
        this.nowTrafic = nowTrafic;
    }

    public int getLastUser() {
        return lastUser;
    }

    public void setLastUser(int lastUser) {
        this.lastUser = lastUser;
    }

    public int getNowUser() {
        return nowUser;
    }

    public void setNowUser(int nowUser) {
        this.nowUser = nowUser;
    }
    
    
    public static UserTrafficNombre getNombreUser(Connection connection) throws Exception{
        UserTrafficNombre result=null;
        Calendar cal=Util.NowtoCalendar();
        String sql="select * from getNombreLNMonth("+ (cal.get(Calendar.MONTH)+1) +","+ cal.get(Calendar.YEAR)+")";
       try
        {
            if(connection.isClosed())
            {
                connection = DBConnection.connect();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet r =  preparedStatement.executeQuery();
            while(r.next()){
                result=new UserTrafficNombre(r.getInt(3), r.getInt(4), r.getInt(1), r.getInt(2));
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
