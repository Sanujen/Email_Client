public class Official extends Recipients{
    String designation;

    Official(){

    }
    Official(String[] lineArray) {
        this.name = lineArray[0].substring(10);
        this.email = lineArray[1];
        this.designation = lineArray[2];
    }

}

