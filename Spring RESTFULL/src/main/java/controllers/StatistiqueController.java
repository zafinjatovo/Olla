
package controllers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import models.Admin;
import models.DBConnection;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import utils.Util;
import viewModeles.ChiffreAffaireOperator;
import viewModeles.ChiffreAffaireUser;
import viewModeles.OffreStat;
import viewModeles.UserTrafficNombre;

public class StatistiqueController {

    public StatistiqueController() {
    }
    
    public static Response getUserTrafficNombre(HttpHeaders headears) throws Exception{
        Response response=new Response();
        String token=Util.getToken(headears);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
              UserTrafficNombre result=UserTrafficNombre.getNombreUser(connection);
              response= new Response("succes",result);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
    
    public static Response getUserChiffreAffaire(Map<String,String> data,HttpHeaders headears) throws Exception{
        Response response=new Response();
        String token=Util.getToken(headears);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
              int idOperator=Integer.parseInt(data.get("operator"));
              int topN=Integer.parseInt(data.get("top"));
              ArrayList<ChiffreAffaireUser> results=ChiffreAffaireUser.getByOperator(connection, idOperator, topN);
              response= new Response("succes",results);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
    
    public static Response getOperatorChiffreAffaire(HttpHeaders headears) throws Exception{
        Response response=new Response();
        String token=Util.getToken(headears);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
              ArrayList<ChiffreAffaireOperator> results=ChiffreAffaireOperator.getChiffreAffaire(connection);
              response= new Response("succes",results);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
    
    public static Response getOfferStat(Map<String,String> data,HttpHeaders headears) throws Exception{
        Response response=new Response();
        String token=Util.getToken(headears);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
              int idOperator=Integer.parseInt(data.get("operator"));
              ArrayList<OffreStat> results=OffreStat.getStatOffre(connection,idOperator);
              response= new Response("succes",results);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
    
     public static Response getOperatorChiffreAffaireSomme(HttpHeaders headears) throws Exception{
        Response response=new Response();
        String token=Util.getToken(headears);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
                double result=ChiffreAffaireOperator.getChiffreAffaireTotal(connection);
              response= new Response("succes",result);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
}
