package Model;

/**
 * Created by Punit Yadav on 1/8/2018.
 */

public class User {
    String Username;
    String Password;
    String Email;
    String Mobileno;
    String Gender;
    String Dob;
    String City;

    public String getPassword() {
        return Password;
    }


    public String getEmail() {
        return Email;
    }

    public String getGender() {
        return Gender;
    }

    public String getDob() {
        return Dob;
    }

    public String getCity() {
        return City;
    }



    public String getUsername() {
        return Username;
    }

    public String getMobileno() {
        return Mobileno;
    }

    public User(String username,String password,String email, String mobileno,String gender,String dob,String city) {

        Username = username;
        Password = password;
        Email = email;
        Mobileno = mobileno;
        Gender = gender;
        Dob = dob;
        City = city;
    }



}
