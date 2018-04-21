package Model;

/**
 * Created by lenovo on 3/21/2018.
 */

public class Coupon {
    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getDiscountrate() {
        return discountrate;
    }

    public void setDiscountrate(String discountrate) {
        this.discountrate = discountrate;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    String dealID;
    String discountrate;
    String productname;

    public Coupon(String dealID, String discountrate, String productname, String productprice) {
        this.dealID = dealID;
        this.discountrate = discountrate;
        this.productname = productname;
        this.productprice = productprice;
    }

    String productprice;

    public Coupon(){}
}
