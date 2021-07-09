
package com.service.appeltelephonique;

import controllers.HistoriqueController;
import java.util.Map;
import models.responseBody.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HistoriqueService {
    
    @GetMapping(path = "/HistoriqueAppel")
    public static Response historiqueAppel(@RequestParam Map<String,String> data,@RequestHeader HttpHeaders http) throws Exception{
        return  HistoriqueController.getHistoriqueAppel(data, http);
    }
}
