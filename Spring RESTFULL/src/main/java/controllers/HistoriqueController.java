package controllers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import models.DBConnection;
import models.User;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import utils.Util;
import viewModeles.AppelHistorique;

public class HistoriqueController {

    public HistoriqueController() {
    }
    
    public static Response getHistoriqueAppel(Map<String,String> data,@RequestHeader HttpHeaders http)throws Exception{
        Response response=new Response();
        String token=Util.getToken(http);
        Connection connection = DBConnection.connect();
        int idUser = User.GetUserIdByToken(connection, token);
        if(idUser>-1){
            String type=data.get("type");
            ArrayList<AppelHistorique> result=AppelHistorique.getHistorique(connection,type, idUser);
            response=new Response("succes",result);
        }else{
            response=new Response("error","");
        }
        return response;
    }
}
