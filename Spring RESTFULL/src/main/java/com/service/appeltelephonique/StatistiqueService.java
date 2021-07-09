
package com.service.appeltelephonique;

import controllers.StatistiqueController;
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
public class StatistiqueService {
    @GetMapping(path = "/UserTrafficNombre")
    public static Response UserTrafficNombre(@RequestHeader HttpHeaders headers) throws Exception{
        return StatistiqueController.getUserTrafficNombre(headers);
    }
    
    @GetMapping(path = "/UserChiffreAffaire")
    public static Response UserChiffreAffaire(@RequestParam Map<String,String> data,@RequestHeader HttpHeaders headears) throws Exception{
        return StatistiqueController.getUserChiffreAffaire(data, headears);
    }
    
    @GetMapping(path = "/OperatorChiffreAffaire")
    public static Response OperatorChiffreAffaire(@RequestHeader HttpHeaders http) throws Exception{
        return StatistiqueController.getOperatorChiffreAffaire(http);
    }
    
    @GetMapping(path = "/StatOffer")
    public static Response StatOffer(@RequestParam Map<String,String> data,@RequestHeader HttpHeaders http) throws Exception{
        return StatistiqueController.getOfferStat(data, http);
    }
    
     @GetMapping(path = "/ChiffreAffaireTotal")
    public static Response ChiffreAffaireTotal(@RequestHeader HttpHeaders http) throws Exception{
        return StatistiqueController.getOperatorChiffreAffaireSomme(http);
    }
}
