package Model;

/**
 * Created by lenovo on 3/21/2018.
 */

public class Review {
    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    String ratings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Review(String id,String ratings, String feedback) {
        this.ratings = ratings;
        this.feedback = feedback;
        this.id = id;
    }

    String feedback;

    Review(){}
}
