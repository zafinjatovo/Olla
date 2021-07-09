package com.service.appeltelephonique;

import java.util.Map;
import models.responseBody.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AdminService {
    @PostMapping(path = "/LoginAdmin")
    public Response controlConnectAdmin(@RequestBody Map<String,String> dataBody) throws Exception{
        return controllers.AdminController.LogAdmin(dataBody);
    }
}
