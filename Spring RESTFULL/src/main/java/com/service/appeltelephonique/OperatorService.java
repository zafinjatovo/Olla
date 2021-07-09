
package com.service.appeltelephonique;

import controllers.OperatorController;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class OperatorService {
   
   @GetMapping(path = "/Usernombre")
   public Response getUserNombre(@RequestHeader HttpHeaders httpHeaders) throws Exception{
       return  OperatorController.getUserNombreByOperator(httpHeaders);
   }
}
