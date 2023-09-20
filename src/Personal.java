import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Personal extends Recipients implements Wishable{
    private Date dob;
    private String nickName;
    private int month;
    private int day;
    private String wish = "hugs and love on your birthday. Sanujen";
    Personal(String[] lineArray) throws ParseException {
        try{
            this.name = lineArray[0].substring(10);
            this.nickName = lineArray[1];
            this.email = lineArray[2];
            this.dob = new SimpleDateFormat("yyyy/MM/dd").parse(lineArray[3]);
            this.month = dob.getMonth();
            this.day = dob.getDate();
        }catch (ParseException p){
            System.out.println("Invalid input");
        }

    }

    //checking for the input date is the recipient's birthday
    public boolean CheckBday(int m, int d){
        if (month==m && day==d){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Email wish() {
        return sendBdayMail();
    }

    //method to send birthday greeting
    public Email sendBdayMail() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        final String username = "";//Enter email and password
        final String password = "";

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sanuprem6@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Birthday Greetings");
            message.setText(wish);
            Transport.send(message);
            return new Email(email,"Birthday Greetings",wish,dob);

        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getDob() {
        return dob;
    }

    public String getNickName() {
        return nickName;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getWish() {
        return wish;
    }
}
