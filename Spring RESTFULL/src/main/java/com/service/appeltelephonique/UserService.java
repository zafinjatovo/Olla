package com.service.appeltelephonique;

import controllers.UserController;
import java.util.Map;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserService {
    @GetMapping(path = "/profil")
    public static Response profil(@RequestHeader HttpHeaders http) throws Exception{
        return UserController.getUser(http);
    }
    
   @PostMapping(path = "/Update")
    public  static Response update(@RequestBody Map<String,String> data,@RequestHeader HttpHeaders http) throws Exception{
        System.out.println("ok");
        return UserController.updateUser(data, http);
    }
}
