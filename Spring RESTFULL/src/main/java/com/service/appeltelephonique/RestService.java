package com.service.appeltelephonique;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import models.ActionMobileMoney;
import models.Admin;
import models.CallConsumption;
import models.Contact;
import models.DBConnection;
import models.Offer;
import models.OfferComposant;
import models.OfferType;
import models.Operator;
import models.Purchase;
import models.User;
import models.requestBody.UserLogin;
import models.responseBody.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Util;

/**
 *
 * @author Tokiniaina
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestService 
{
    @GetMapping(path = "/")
    public String hello(){
        return "Hello!";
    }
    
    @PostMapping(path = "/Connexion")
    public Response Connexion(@RequestBody UserLogin user) throws Exception
    {
        Response data=new Response();
        if(Util.CheckEmail(user.getEmail()))
        {
            data = User.connect(user);
        }
        else
        {
            data.setStatus("error");
        }
        return  data;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/Operators")
    public Response GetOperators() throws Exception
    {
        Connection connection = DBConnection.connect();
        List<Operator> operators = Operator.GetOperators(connection);
        Response response = new Response("succes", operators);
        return  response;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/Inscription")
    public Response SignUp(@RequestBody Map<String, String> dataBody) throws Exception
    {
        String name =  dataBody.get("name");
        String firstname = dataBody.get("firstname");
        String CIN = dataBody.get("CIN");
        String email = dataBody.get("email");
        String password = dataBody.get("password");
        String idOperator = dataBody.get("idOperator");
        
        if((name != "")&&(firstname != "") && (CIN != "") && (email != "") && (password != "") && (idOperator != ""))
        {
            Connection connection = DBConnection.connect();
            User user = new User(Integer.parseInt(idOperator), name, firstname, email, password, CIN);
            if(user.SignUpAndSendEmail(connection, Util.GetConfirmedSignUpUrl()))
            {
                Response response = new Response("succes",null);
                return response;
            }
            else
            {
                Response response = new Response("failed",null);
                return response;
            }
        }
        else
        {
            Response response = new Response("failed",null);
            return response;    
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/MakeAnAction")
    public Response MakeAnActionOnMobileMoney(@RequestBody Map<String, String> dataBody, @RequestHeader("Authorization") String token) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        ActionMobileMoney.Transaction transaction = null;
        String transactionTypeString = dataBody.get("transactionType").toString().trim();
        if(transactionTypeString.compareTo("deposit") == 0)
        {
            transaction = ActionMobileMoney.Transaction.deposit;
        }
        else if(transactionTypeString.compareTo("withdrawal") == 0)
        {
            transaction = ActionMobileMoney.Transaction.withdrawal;
        }
        else
        {
            transaction = ActionMobileMoney.Transaction.buy;
        }
        ActionMobileMoney action = new ActionMobileMoney(user, Double.valueOf(dataBody.get("cash")), transaction, LocalDateTime.parse(dataBody.get("date")));
        boolean actionAutorized = false;
        if((transaction == ActionMobileMoney.Transaction.deposit) || (user.IsEnought(connection, action)))
        {
            actionAutorized = true;
        }
        if(actionAutorized)
        {
            if(user.MakeAction(connection, action , true))
            {
                return new Response("succes", "");
            }
            else
            {
                return new Response("error", "error: deposit failed");
            }
        }
        else
        {
            return new Response("error", "error: sold not enougth");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/NoConfirmedDeposits")
    public Response GetNoConfirmedDeposits(@RequestHeader("Authorization") String token) throws Exception
    {
        Connection connection = DBConnection.connect();
        int idAdmin = Admin.GetAdminIdByToken(connection, token.split(" ")[1]);
        if(idAdmin != -1)
        {
            Admin admin = Admin.FindById(connection, idAdmin);
            List<ActionMobileMoney> notConfirmedDeposit = admin.GetAllDepositNotConfirmed(connection);
            return new Response("succes", notConfirmedDeposit);
        }
        else
        {
            return new Response("error", "admin not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/ConfirmedDeposit")
    public Response ConfirmedDeposit(@RequestBody Map<String, String> dataBody ,@RequestHeader("Authorization") String token) throws Exception
    {
        Connection connection = DBConnection.connect();
        int idAdmin = Admin.GetAdminIdByToken(connection, token.split(" ")[1]);
        if(idAdmin != -1)
        {
            Admin admin = Admin.FindById(connection, idAdmin);
            if(admin.ConfirmDeposit(connection, Integer.parseInt(dataBody.get("idDeposit")), Boolean.parseBoolean(dataBody.get("confirmation"))))
            {
                return new Response("succes", "confirmation succes");            
            }
            else
            {
                return new Response("succes", "confirmation error");
            }
        }
        else
        {
            return new Response("error", "admin not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetHistoriqueMobileMoney")
    public Response GetHistorique(@RequestHeader("Authorization") String token) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            List<ActionMobileMoney> actions = user.GetHistoriqueAction(connection);
            return new Response("succes", actions);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/GetSolde")
    public Response GetSolde(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        LocalDateTime date = LocalDateTime.parse(dataBody.get("date"));
        System.out.println(date);
        if(user != null)
        {
            double solde = user.GetSolde(connection, date);
            return new Response("succes", solde);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/SaveContact")
    public Response SaveContact(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            Contact contact = new Contact(dataBody.get("name"), dataBody.get("firstname"), dataBody.get("phonenumber"));
            user.SaveContact(contact);
            return new Response("succes", null);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetContact")
    public Response GetContact(@RequestHeader("Authorization") String token) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            List<Contact> contacts = user.GetContacts();
            return new Response("succes", contacts);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/AddCredit")
    public Response InsertCredit(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            if(user.InsertCredit(connection, Double.parseDouble(dataBody.get("valueCredit")), LocalDateTime.parse(dataBody.get("date"))))
            {
                return new Response("succes", "");
            }
            else
            {
                return new Response("error", "insertion failed");
            }
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetSoldeCredit")
    public Response GetSoldeCredit(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            double solde = user.GetSoldeCredit(connection, LocalDateTime.parse(dataBody.get("date")));
            return new Response("succes", solde);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
   @RequestMapping(method = RequestMethod.GET, value = "/OfferTypes")
    public Response GetOfferTypes() throws Exception
    {
        Connection connection = DBConnection.connect();
        List<OfferType> offerTypes = OfferType.GetOfferType(connection);
        Response response = new Response("succes", offerTypes);
        return  response;
    } 
    
    @RequestMapping(method = RequestMethod.POST, value = "/AddOffer")
    public Response AddOffer(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        int idAdmin = Admin.GetAdminIdByToken(connection, token.split(" ")[1]);
        if(idAdmin != -1)
        {
            Operator operator = new Operator(Integer.parseInt(dataBody.get("idOperator").toString()));
            String name = dataBody.get("name").toString();
            double price = Double.parseDouble(dataBody.get("price").toString());
            double validationDuration = Double.parseDouble(dataBody.get("duration").toString());
            
            ObjectMapper mapper = new JsonMapper();
            String jsonOfferComposant = mapper.writeValueAsString(dataBody.get("offerComposant"));
            List<OfferComposant> offerComposants = mapper.readValue(jsonOfferComposant, new TypeReference<List<OfferComposant>>(){});
            
            Offer offer = new Offer(operator, name, price, validationDuration, offerComposants);
            if(offer.Save(connection))
            {
                return new Response("succes", "");
            }
            else
            {
                return new Response("succes", "add offer failed");
            }
        }
        else
        {
             return new Response("error", "admin not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetOperatorOffers")
    public Response GetOffersOperator(@RequestParam Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        Operator operator = new Operator(Integer.parseInt(dataBody.get("idOperator")), dataBody.get("name"), dataBody.get("prefix"));
        List<Offer> offers = operator.GetOffers(connection);
        return new Response("succes", offers);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/buyOffer")
    public Response BuyOffer(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            Offer offer = Offer.GetOfferById(connection, Integer.parseInt(dataBody.get("idOffer")));
            Purchase purchase = new Purchase(offer, Integer.parseInt(dataBody.get("typepurchase")), LocalDateTime.parse(dataBody.get("date")));
            return user.BuyOffer(connection, purchase);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/SimulateCall")
    public Response SimulateCall(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            User receipter = User.FindUserByPhoneNumber(connection, dataBody.get("phonenumber"));
            double callDuration = Double.parseDouble(dataBody.get("duration"));
            LocalDateTime date =  LocalDateTime.parse(dataBody.get("date"));
            if(receipter != null)
            {
                CallConsumption callConsumption = new CallConsumption(0, receipter.getId(), receipter.getIdOperator(), callDuration, date);
                return user.SimulationCall(connection, callConsumption);
            }
            else
            {
                return new Response("error", "phonenumber doesn't exist");
            }
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetOfferSold")
    public Response GetOfferSold(@RequestHeader("Authorization") String token, @RequestParam Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            int idOffer = Integer.parseInt(dataBody.get("idOffer"));
            LocalDateTime date = LocalDateTime.parse(dataBody.get("date"));
            return user.GetSoldOffer(connection, idOffer, date);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetHistoriqueCall")
    public Response GetHistoriqueCall(@RequestHeader("Authorization") String token,  @RequestParam Map<String, String> dataBody) throws Exception
    {
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            LocalDateTime date = LocalDateTime.parse(dataBody.get("date"));
            return user.GetHistoriqueCall(connection, date);
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetUserOperator")
    public Response GetUserOperator(@RequestHeader("Authorization") String token) throws Exception
    {
        Response response = null;
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            response = new Response("succes", user.GetOperator(connection));
            return response;
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/GetUserInformation")
    public Response GetUserInformation(@RequestHeader("Authorization") String token) throws Exception
    {
        Response response = null;
        Connection connection = DBConnection.connect();
        User user = User.GetAuthentification(connection, token.split(" ")[1]);
        if(user != null)
        {
            response = new Response("succes", user);
            return response;
        }
        else
        {
            return new Response("error", "user not authentified");
        }
    }
}
