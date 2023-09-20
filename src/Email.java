import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Email implements java.io.Serializable {
    //setting serialVersionUId to avoid InvalidClassException
    private static final long serialVersionUID = 8738595628193547334L;
    private  String recipient;
    private String subject;
    private String content;
    private Date time;
    private Date sendDate = new Date();

    public Email(){

    }

    public Email(String recipient, String subject, String content, Date date) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.time = date;
    }
    public void sendMail(){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        final String username = ""; //Enter the sender email address
        final String password = ""; //Enter the app password generated for your email

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sanuprem6@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Date getTime() {
        return time;
    }
    public String getRecipient() {
        return recipient;
    }
    public String getSubject() {
        return subject;
    }
    public String getContent() {
        return content;
    }

    public Date getSendDate() {
        return sendDate;
    }
}
