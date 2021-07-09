package models.requestBody;


public class UserSignup 
{
    String name;;
    String firstname;
    String CIN;
    String email;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSignup() 
    {
    }

    public UserSignup(String name, String firstname, String CIN, String email, String password) 
    {
        this.name = name;
        this.firstname = firstname;
        this.CIN = CIN;
        this.email = email;
        this.password = password;
    }
    
}
