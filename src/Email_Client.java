import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Email_Client {
    //your index number - 200583P
    static int count = 0;
    static Date currentDate = new Date();    //present date
    static int month = currentDate.getMonth();
    static int day = currentDate.getDate();
    static ArrayList<Office_friend> bDaylistToday1 = new ArrayList(); //official friends' birthday list
    static ArrayList<Personal> bDaylistToday2 = new ArrayList(); //personal friends' birthday list
    static ArrayList<Email> appendList = new ArrayList(); //list of email objects which were sent
    static ArrayList<String> greetinglist = new ArrayList(); //list of birthday recipients who received their birthday wishes
    static ArrayList birthdayList = new ArrayList();
    static File eMailFile = new File("eMailFile.ser"); //file of serialized emails
    static File greetingFile = new File("greetingFile.ser"); //file of serialized birthday recipients
    static int bDayCount = 0; //initializing total of recipients who have birthdays

    //method to write to the file
    public static void write(File file, String details){
        try {
            FileWriter writer = new FileWriter(file.getName(),true);
            writer.write(details+"\n");
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //method of checking before writing to the file if there is already a record of the recipient
    public static void add(File file, ArrayList list, String details){
        if (list.contains(details)){
            System.out.println("Recipient already added");
        }else{
            write(file,details);
        }
    }

    //method of serialization
    public static void serialize(Object o, File file){
        try {
            FileOutputStream fileOut = new FileOutputStream(file.getName());
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(o);
            output.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    //method of deserialization
    public static Object deserialize(Object o, File file){
        o = null;
        try {
            FileInputStream fileIn = new FileInputStream(file.getName());
            ObjectInputStream input = new ObjectInputStream(fileIn);
            o = (Object) input.readObject();
            input.close();
            fileIn.close();
            return o;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("class not found");
            c.printStackTrace();
            return null;
        } catch (NullPointerException c) {
            System.out.println("pointing null pointer");
            c.printStackTrace();
            return null;
        }
    }

    public static boolean isInputInt(String s){
        try{
            int i = Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    //method to read the file of clients, creating recipient objects
    public static void read(File file1, ArrayList list, ArrayList o, ArrayList of, ArrayList p) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file1.getName()));
            String line = reader.readLine();
            while (line != null ) {
                list.add(line);
                String[] lineArray = line.split("[,]", 0); //splitting details
                //creating Official_friend object
                if (line.startsWith("Office_friend")){
                    Office_friend of1 = new Office_friend(lineArray);
                    if(of1.CheckBday(month,day)){//checking if birthday and present day are matching
                        bDaylistToday1.add(of1);
                        bDayCount++;
                    }
                    of.add(of1); //adding to list of objects
                    count++;
                }
                //creating Official object
                else if (line.startsWith("Official")){
                    Official o1 = new Official(lineArray);
                    o.add(o1); //adding to list of objects
                    count++;
                }
                //creating personal object
                else if (line.startsWith("Personal")){
                    Personal p1 = new Personal(lineArray);
                    if (p1.CheckBday(month,day)){//checking if birthday and present day are matching
                        bDaylistToday2.add(p1);
                        bDayCount++;
                    }
                    p.add(p1); //adding to list of objects
                    count++;
                }
                line = reader.readLine();
            }
            birthdayList.add(bDaylistToday1);
            birthdayList.add(bDaylistToday2);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //method of sending birthday greetings
    public static void sendBdayMail(ArrayList<Office_friend> list,ArrayList<Personal> list2) throws IOException {
        System.out.println("Wait for "+bDayCount+
                           " \"mail sent\" or \"Already mail sent for birthday\" messages to continue");

        if (!list.isEmpty()){ //checking the recipient objects of Office_friend
            if (eMailFile.length() != 0){
                //deserializing email objects from file
                appendList = (ArrayList<Email>) deserialize(appendList,eMailFile);
            }
            if (greetingFile.length()==0) {
                for (int i = 0; i < list.size(); i++) {
                    appendList.add(list.get(i).wish()); //adding to email list and sending birthday mail
                    System.out.println("Mail sent");
                    greetinglist.add(list.get(i).email); //adding birthday recipient mail address to list
                }
            }
            else{
                //deserializing mail addresses from greeting file
                greetinglist = (ArrayList<String>) deserialize(greetinglist,greetingFile);
                for (int i=0; i < list.size();i++){
                    //checking if birthday mail is already sent
                    if (greetinglist.contains(list.get(i).email)){
                        System.out.println("Already mail sent to "+list.get(i).name+" for birthday");
                    }else{
                        appendList.add(list.get(i).wish()); //adding to email list and sending birthday mail
                        System.out.println("Mail sent");
                        greetinglist.add(list.get(i).email); //adding birthday recipient mail address to list
                    }
                }
            }
            serialize(greetinglist,greetingFile); //serializing recipient addresses to the greeting file
            serialize(appendList,eMailFile); //serializing email objects to the email file
        }

        if (!list2.isEmpty()){ //checking the recipient objects of Personal friends
            if (eMailFile.length()!=0){
                //deserializing email objects from file
                appendList = (ArrayList<Email>) deserialize(appendList,eMailFile);
            }
            if (greetingFile.length()==0) {
                for (int i = 0; i < list2.size(); i++) {
                    appendList.add(list2.get(i).wish()); //adding to email list and sending birthday mail
                    System.out.println("Mail sent");
                    greetinglist.add(list2.get(i).email); //adding birthday recipient mail address to list
                }
            }
            else{
                //deserializing mail addresses from greeting file
                greetinglist = (ArrayList<String>) deserialize(greetinglist,greetingFile);
                for (int i=0; i < list2.size();i++){
                    //checking if birthday mail is already sent
                    if (greetinglist.contains(list2.get(i).email)){
                        System.out.println("Already mail sent to "+list2.get(i).name+" for birthday");
                    }else{
                        appendList.add(list2.get(i).wish()); //adding to email list and sending birthday mail
                        System.out.println("Mail sent");
                        greetinglist.add(list2.get(i).email); //adding birthday recipient mail address to list
                    }
                }
            }
            serialize(greetinglist,greetingFile); //serializing recipient addresses to the greeting file
            serialize(appendList,eMailFile); //serializing email objects to the email file
        }
        System.out.println();
    }

    public static void main(String[] args) throws ParseException, IOException {
        ArrayList<String> list = new ArrayList(); //list of all recipient details
        ArrayList<Official> officialList = new ArrayList();
        ArrayList<Office_friend> officeFriendList = new ArrayList();
        ArrayList<Personal> personalList = new ArrayList();
        File clientFile = new File("clientList.txt"); // accessing clientList file
        clientFile.createNewFile();
        read(clientFile,list,officialList,officeFriendList,personalList); //reading clientList file
        //sending birthday greetings
        sendBdayMail((ArrayList<Office_friend>) birthdayList.get(0), (ArrayList<Personal>) birthdayList.get(1));

        Scanner scanner = new Scanner(System.in);
        String startupMessage = "Enter option type: \n"
                + "1 - Adding a new recipient\n"
                + "2 - Sending an email\n"
                + "3 - Printing out all the recipients who have birthdays\n"
                + "4 - Printing out details of all the emails sent\n"
                + "5 - Printing out the number of recipient objects in the application\n"
                + "-1 - Exit the program";
        System.out.println(startupMessage);
        String option = scanner.nextLine(); //getting option input
        if (!isInputInt(option)) option ="6"; //avoiding input mismatch
        int optionInt = Integer.parseInt(option);
        while (optionInt != -1){
            switch (optionInt) {
                case 1:
                    Scanner detail = new Scanner(System.in);
                    System.out.println("Enter the details as below\n"+
                            "Official: <name>,<email>,<designation>\n"+
                            "Office_friend: <name>,<email>,<designation>,<dob YYYY/MM/DD>\n"+
                            "Personal: <name>,<nick-name>,<email>,<dob YYYY/MM/DD>"
                    );
                    String details = detail.nextLine();
                    add(clientFile, list, details); //adding to clientList file
                    System.out.println();
                    break;

                case 2:
                    Scanner mail = new Scanner(System.in);
                    System.out.println("Enter the mail input format - <email>,<subject>,<content>");
                    String mailString = mail.nextLine();
                    String[] mailArray = mailString.split("[,]", 0);
                    String recipient = mailArray[0];
                    String subject = mailArray[1];
                    String content = mailArray[2];
                    Email eMail = new Email(recipient, subject, content, currentDate);// creating email object
                    eMail.sendMail(); //sending email
                    if (eMailFile.length() == 0) {
                        appendList.add(eMail); //adding email object
                    } else {
                        appendList = (ArrayList<Email>) deserialize(appendList, eMailFile); //deserializing email file
                        appendList.add(eMail); //adding email object
                    }
                    serialize(appendList, eMailFile); //serializing email objects to eMail file
                    System.out.println();
                    break;

                case 3:
                    //getting the date input to check birthdays
                    Scanner dob = new Scanner(System.in);
                    System.out.println("Enter the date yyyy/MM/dd :");
                    String dateString = dob.nextLine();
                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dateString);

                    System.out.println("Names of recipients who have birthday on this date : ");
                    boolean checkBdayGuys = true;
                    //traversing official friends object list
                    for (int i = 0; i < officeFriendList.size(); i++) {
                        //Checking for birthday matches
                        if ((officeFriendList.get(i).getMonth()) == (date.getMonth()) && (officeFriendList.get(i).getDay()) == (date.getDate())) {
                            System.out.println("Official friend: "+officeFriendList.get(i).name);
                            checkBdayGuys = false;
                        }
                    }
                    //traversing personal friends object list
                    for (int i = 0; i < personalList.size(); i++) {
                        //Checking for birthday matches
                        if ((personalList.get(i).getMonth()) == (date.getMonth()) && (personalList.get(i).getDay()) == (date.getDate())) {
                            System.out.println("Personal friend: "+personalList.get(i).name);
                            checkBdayGuys = false;
                        }
                    }
                    if (checkBdayGuys) {
                        System.out.println("No one has birthday.");
                    }
                    System.out.println();
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print recipients who have birthdays on the given date
                    break;

                case 4:
                    boolean check = true;
                    Scanner dateCheck = new Scanner(System.in);
                    System.out.println("Enter the date yyyy/MM/dd :");
                    String stringDate = dateCheck.nextLine();
                    try {
                        if (eMailFile.length() == 0) {
                            System.out.println("Empty file");
                        } else {
                            appendList = (ArrayList) deserialize(appendList, eMailFile);
                            for (Email dSMail : appendList) {
                                SimpleDateFormat dateStringchange = new SimpleDateFormat("yyyy/MM/dd");
                                String onlyDate = dateStringchange.format(dSMail.getSendDate());
                                if (stringDate.equals(onlyDate)) {
                                    System.out.println("Recipient email: "+dSMail.getRecipient());
                                    System.out.println("Subject: "+dSMail.getSubject());
                                    System.out.println("Content: "+dSMail.getContent() + "\n");
                                    check = false;
                                }
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Null object");
                    }
                    if (check) {
                        System.out.println("No mails on this day");
                    }
                    System.out.println();
                    break;

                case 5:
                    // code to print the number of recipient objects in the application
                    System.out.println("number of recipient objects in the application: " + count+"\n");
                    break;

                case -1:
                    System.out.println("Exiting the program");
                    break;

                default:
                    System.out.println("Invalid input! program running again\n");
                    break;
            }
            list.clear();
            officialList.clear();
            officeFriendList.clear();
            personalList.clear();
            bDaylistToday1.clear();
            bDaylistToday2.clear();
            birthdayList.clear();
            bDayCount = 0;
            count = 0;
            read(clientFile,list,officialList,officeFriendList,personalList); //reading clientList file
            //sending birthday greetings
            sendBdayMail((ArrayList<Office_friend>) birthdayList.get(0), (ArrayList<Personal>) birthdayList.get(1));
            System.out.println(startupMessage);

            option = scanner.nextLine(); //getting option input
            if (!isInputInt(option)) option ="6";
            optionInt = Integer.parseInt(option);
        }
    }
}



