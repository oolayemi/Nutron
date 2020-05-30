package com.stylet.nutron.Model;

public class Patient extends PatientId {

    String user_id;
    String useremail;
    String username;
    String userimage;


    public Patient(String user_id, String useremail, String username, String userimage) {
        this.user_id = user_id;
        this.useremail = useremail;
        this.username = username;
        this.userimage = userimage;
    }

    public Patient() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

}
