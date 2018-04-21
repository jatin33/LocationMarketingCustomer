package mytextview.example.com.customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ShopSubCategory extends AppCompatActivity {

    DatabaseReference shopref;
    ListView subCategoryUI;
    TextView subCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("shopnameForSubCat"));
        setContentView(R.layout.activity_shop_sub_category);

        subCategoryUI = findViewById(R.id.list_sub_category);
        subCategory = findViewById(R.id.sub_category);
        subCategory.setText("SubCategories");

        shopref = FirebaseDatabase.getInstance().getReference("Deal")
                .child(getIntent().getStringExtra("shopEmailForSubCat").replace(".",""))
                .child(getIntent().getStringExtra("selectedCategory"));

        shopref.addValueEventListener(new ValueEventListener() {
                ArrayAdapter listData;
                ArrayList<String> subcategories = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                {
                    String subcategory = child.getKey();
                    Log.v("category",subcategory);
                    subcategories.add(subcategory);
                }
                listData = new ArrayAdapter<String>(ShopSubCategory.this,android.R.layout.simple_list_item_1,subcategories);
                subCategoryUI.setAdapter(listData);

                subCategoryUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedSubCategory = listData.getItem(i).toString();
                        Intent goToCouponList = new Intent(ShopSubCategory.this,CouponList.class);
                        goToCouponList.putExtra("shopnameForCoupons",getIntent().getStringExtra("shopnameForSubCat"));
                        goToCouponList.putExtra("shopEmailForCoupons",getIntent().getStringExtra("shopEmailForSubCat"));
                        goToCouponList.putExtra("selectedCategoryForCoupons",getIntent().getStringExtra("selectedCategory"));
                        goToCouponList.putExtra("selectedSubCategory",selectedSubCategory);
                        goToCouponList.putExtra("number",getIntent().getStringExtra("number"));
                        goToCouponList.putExtra("email",getIntent().getStringExtra("email"));
                        startActivity(goToCouponList);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}