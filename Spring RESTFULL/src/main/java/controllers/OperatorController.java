package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import models.Admin;
import models.DBConnection;
import models.Operator;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import utils.Util;
import viewModeles.Usernombre;

public class OperatorController {
    public static Response getAllOperator(HttpHeaders http) throws Exception
    {
        Response response=new Response();
        String token=Util.getToken(http);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
              List<Operator> operators = Operator.GetOperators(connection);
              response= new Response("succes", operators);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
    
    
    /// statisique
    public static Response getUserNombreByOperator(HttpHeaders http) throws IOException, Exception{
        Response response=new Response();
        String token=Util.getToken(http);
        Connection connection=DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
         if(idAdmin>-1){
              ArrayList<Usernombre> usernombre=Usernombre.getNombreUser(connection);
              response= new Response("succes",usernombre);
        }else{
               response= new Response("error", "");
        }
        return  response;
    }
}
