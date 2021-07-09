package controllers;

import java.sql.Connection;
import java.util.Map;
import models.DBConnection;
import models.User;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import utils.Util;


public class UserController {

    public UserController() {
    }
    
    public static Response getUser(HttpHeaders http) throws Exception{
        Response response=new Response();
        Connection connection=DBConnection.connect();
        String toke=Util.getToken(http);
        int idUser=User.GetUserIdByToken(connection, toke);
        if(idUser>-1){
            User user=User.FindUserAuthentifiedById(connection, idUser);
            response=new Response("succes",user);
        }else{
            response=new Response("error","");
        }
        return response;
    }
    
    public static Response updateUser(Map<String,String> data,HttpHeaders http)throws Exception{
        Response response=new Response();
        Connection connection=DBConnection.connect();
        String toke=Util.getToken(http);
        int idUser=User.GetUserIdByToken(connection, toke);
        if(idUser>-1){
            String name=data.get("name");
            String firstName=data.get("firstname");
            String cin=data.get("CIN");
            User.updateUser(connection, idUser, name, firstName, cin);
            response=new Response("succes","");
        }else{
            response=new Response("error","");
        }
        return response;
    }
}
