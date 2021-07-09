
package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import org.springframework.http.HttpHeaders;

public class Util {

    public Util() 
    {
    }
    
    public static String nowString(){
        LocalDateTime now=LocalDateTime.now();
        return  now.toString();
    } 
     public static LocalDateTime nowDatetime(){
        LocalDateTime now=LocalDateTime.now();
        return now;
    } 
    
    public static String getToken(HttpHeaders http)
    {
        String token="";
        List<String> list=http.get("authorization");
        token=list.get(0).split(" ")[1];
        return token;
    }
    
    public static boolean CheckEmail(String email)
    {
        boolean result=true;
        try {
            InternetAddress e=new InternetAddress(email);
            e.validate();
        } catch (Exception e) 
        {
            e.printStackTrace();
            result=false;
        }
        return result;
    }
    
    public static String changeFormatToTime(int seconde){
        String value="";
        LocalTime time=LocalTime.ofSecondOfDay(seconde);
        value=time.format(DateTimeFormatter.ISO_TIME);
        return value;
    }
    
    public static int ChangeFormatToSecond(String time){
        int seconde=0;
        LocalTime times=LocalTime.parse(time);
        seconde=times.toSecondOfDay();
        return  seconde;
    }
   
    public static String GetConfirmedSignUpUrl() throws FileNotFoundException, IOException
    {
        //return "http://localhost:8081/confirmSignUp";
        return "https://ollamobile.herokuapp.com/confirmSignUp";
    }
    
    public static Calendar NowtoCalendar(){
        Calendar cal = Calendar.getInstance();
        LocalDateTime datetime=LocalDateTime.now();
        System.out.println(datetime.toString());
        cal.set(datetime.getYear(),datetime.getMonthValue()-1,datetime.getDayOfMonth(),datetime.getHour(), datetime.getMinute(),datetime.getSecond());
        return cal;
    }
    
      public static  void ajout(Calendar calendar,int year,int month,int week,int day){
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.YEAR,year);
        calendar.add(Calendar.MONTH,month);
        calendar.add(Calendar.WEEK_OF_YEAR,week);
    }
    
    public static String toString(Calendar c){
        String endTime = "";
        DateFormat timeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        endTime = timeFormat.format(c.getTime());
        return endTime;
    }
}
