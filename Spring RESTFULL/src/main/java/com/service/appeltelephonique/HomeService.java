/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.appeltelephonique;

import java.sql.Connection;
import java.util.Map;
import models.DBConnection;
import models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Tokiniaina
 */
@Controller
public class HomeService 
{    
    @RequestMapping(method = RequestMethod.GET, value = "/confirmSignUp")
    @ResponseBody
    public String page(@RequestParam Map<String, String> dataBody) throws Exception
    {
        String token = dataBody.get("token");
        System.out.println(token);
        if(token != "")
        {
            Connection connection = DBConnection.connect();
            if(User.GeneratePhoneNumberAfterEmailConfirmation(connection, token))
            {
                return "<h1 style='text-align:center'>your email was verified</h1>";
            }
            else
            {
                return "<h1 style='text-align:center'>error server</h1>";
            }
        }
        else
        {
            return "<h1 style='text-align:center'>error server</h1>";
        }
    }
    
}
