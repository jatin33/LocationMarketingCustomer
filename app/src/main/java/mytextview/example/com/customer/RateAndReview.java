package mytextview.example.com.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Review;

public class RateAndReview extends AppCompatActivity {

    RatingBar rate;
    EditText review;
    Button submitReview;
    DatabaseReference reviewsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("shopNameForRate"));
        setContentView(R.layout.activity_rate_and_review);

        rate = findViewById(R.id.ratingBar);
        review = findViewById(R.id.review);
        submitReview = findViewById(R.id.submit_rate_review);

        reviewsRef = FirebaseDatabase.getInstance().getReference("Reviews");
//                .child(getIntent().getStringExtra("shopEmailForRate").replace(".",""));

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReviews();
            }
        });

    }

    void setReviews()
    {
      String ratings = String.valueOf(rate.getRating());
      String feedback = review.getText().toString();

      if(TextUtils.isEmpty(feedback))
      {
            review.setError("Cannot be Empty");
            return;
      }

        String id = reviewsRef.push().getKey();
        Review review = new Review(id,ratings,feedback);
        reviewsRef.child(getIntent().getStringExtra("shopEmailForRate").replace(".","")).child(id).setValue(review);
        Toast.makeText(this, "Review Added successfully", Toast.LENGTH_SHORT).show();
    }
}
