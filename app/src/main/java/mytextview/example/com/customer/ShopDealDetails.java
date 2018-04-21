package mytextview.example.com.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopDealDetails extends AppCompatActivity {

    DatabaseReference shopref;
    ListView categoryUI;
    TextView shopname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("shopname"));
        setContentView(R.layout.activity_shop_deal_details);

        categoryUI = findViewById(R.id.list_category);
        shopname = findViewById(R.id.shop_name);
        shopname.setText("Categories");


        shopref = FirebaseDatabase.getInstance().getReference("Deal")
        .child(getIntent().getStringExtra("shopemail").replace(".",""));

        shopref.addValueEventListener(new ValueEventListener() {
        ArrayAdapter listData;
        ArrayList<String> categories = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                    {
                     String category = child.getKey();
                     Log.v("category",category);
                     categories.add(category);
                    }
                    Log.d("categoriesIn",categories.toString());
                    listData = new ArrayAdapter<String>(ShopDealDetails.this,android.R.layout.simple_list_item_1,categories);
                    categoryUI.setAdapter(listData);

                    categoryUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedCategory = listData.getItem(i).toString();
                            Intent goToSubCategory = new Intent(ShopDealDetails.this,ShopSubCategory.class);
                            goToSubCategory.putExtra("shopnameForSubCat",getIntent().getStringExtra("shopname"));
                            goToSubCategory.putExtra("shopEmailForSubCat",getIntent().getStringExtra("shopemail"));
                            goToSubCategory.putExtra("selectedCategory",selectedCategory);
                            goToSubCategory.putExtra("number",getIntent().getStringExtra("number"));
                            goToSubCategory.putExtra("email",getIntent().getStringExtra("email"));
                            startActivity(goToSubCategory);
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}
