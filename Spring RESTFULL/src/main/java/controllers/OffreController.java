
package controllers;

import java.sql.Connection;
import java.util.List;
import models.Admin;
import models.DBConnection;
import models.OfferType;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import utils.Util;
public class OffreController {
    
    public static Response getallOfferTypes(HttpHeaders headers) throws Exception
    {
        Response response=new Response();
        String token=Util.getToken(headers);
        Connection connection = DBConnection.connect();
        int idAdmin=Admin.GetAdminIdByToken(connection, token);
        if(idAdmin>-1){
            List<OfferType> offerTypes = OfferType.GetOfferType(connection);
            response= new Response("succes", offerTypes);
        }else{
            response= new Response("error", "");
        }
        return  response;
    }
}
