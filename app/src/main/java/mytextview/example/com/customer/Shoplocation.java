package mytextview.example.com.customer;

/**
 * Created by Punit Yadav on 1/30/2018.
 */

public class Shoplocation {
    String email;
    String latitude;
    String longitude;
    String password;
    String phonenumber;
    String shopname;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
    public Shoplocation(String email, String latitude, String longitude, String password, String phonenumber, String shopname) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
        this.phonenumber = phonenumber;
        this.shopname = shopname;
    }
    public Shoplocation()
    {

    }
    public String getEmail() {
        return email;
    }


    public String getLatitude() {
        return latitude;
    }



    public String getLongitude() {
        return longitude;
    }



    public String getPassword() {
        return password;
    }


    public String getPhonenumber() {
        return phonenumber;
    }



    public String getShopname() {
        return shopname;
    }


}
