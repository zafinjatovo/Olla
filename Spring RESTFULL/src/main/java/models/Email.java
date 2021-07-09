package models;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
    String from;
    String host;
    String passwordFrom;
    String to;
    String subject;
    String object;

    public Email(String from, String host, String passwordFrom, String to, String subject, String object) {
        this.from = from;
        this.host = host;
        this.passwordFrom = passwordFrom;
        this.to = to;
        this.subject = subject;
        this.object = object;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPasswordFrom() {
        return passwordFrom;
    }

    public void setPasswordFrom(String passwordFrom) {
        this.passwordFrom = passwordFrom;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
    
    public Email() {
    }
    
    public static Session getInstanSessionMail(Properties properties,Authenticator authentificator){
        //properties.
        Session session=Session.getInstance(properties, authentificator);
        return session;
    }
    
    public static Authenticator getAuth(String from,String passord){
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from,passord);

            }
        };
    }
    public void SetContent(String url,String token)
    {
        String message="<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Document</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div style='text-align: center'>\n" +
                        "        <h1>Olla!</h1>\n" +
                        "        <p>Click confirmer pour Confirmer votre Compte</p>\n" +
                        "        <form action='" + url + "' method='get' >\n" +
                                    "<input type='hidden' name='token' value='"+ token +"'>"+
                        "            <button type='submit' style='font-size: 2.2vh;\n" +
                        "            width: 10%;\n" +
                        "            margin: 15px auto;\n" +
                        "            background-color: #01e49d;\n" +
                        "            border: 0px solid;\n" +
                        "            height: 2.5em;\n" +
                        "            border-radius: 5px;\n" +
                        "            color: white;\n" +
                        "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;'>\n" +
                        "                Confirmer\n" +
                        "            </button>\n" +
                        "        </form>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";
        this.setObject(message);
    }
    
    public static boolean SendEmail(Email email, String url,String token)
    {
        boolean ans = false;
        try 
        {
            email.SetContent(url, token);
             // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.put("mail.smtp.host", email.host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            
            Session session =Email.getInstanSessionMail(properties, Email.getAuth(email.from, email.passwordFrom));
            session.setDebug(true);
            
            Message message= new MimeMessage(session);
            
             // Set From: header field of the header.
            message.setFrom(new InternetAddress(email.from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.to));

            // Set Subject: header field
            message.setSubject(email.subject);

            // Now set the actual message
            //message.setHeader("Content-Type", "text/style");
            message.setContent(email.object,"text/html");
            //message.setText("<h1>Hello</h1>");
            //message.set
            Transport.send(message);
            ans = true;
        } 
        catch (MessagingException e) 
        {
        }
        return ans;
    }
}
