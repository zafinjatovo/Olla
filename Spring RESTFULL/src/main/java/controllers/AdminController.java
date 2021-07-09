package controllers;

import java.sql.Connection;
import java.util.Map;
import models.Admin;
import models.DBConnection;
import models.responseBody.Response;
import org.springframework.web.bind.annotation.RequestBody;

public class AdminController {
    public static Response LogAdmin(@RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        String token = Admin.LogIn(connection, dataBody.get("email"), dataBody.get("password"));
        if(token != "")
        {
            return new Response("succes",token);
        }
        else{
            return new Response("Error : Email or password Incorect",null);
        }
    }
}
