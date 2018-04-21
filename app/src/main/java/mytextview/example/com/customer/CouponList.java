package mytextview.example.com.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Coupon;

public class CouponList extends AppCompatActivity {
    ArrayList<Coupon> ar=new ArrayList<>();
    private DatabaseReference couponRef;
    private ListView listViewData;
    String smsbody="";
    RadioGroup rg;
    String Methodselected;
    dbHelper d;
    private FloatingActionButton rateAndReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("shopnameForCoupons"));
        setContentView(R.layout.activity_coupon_list);
        d=new dbHelper(CouponList.this);
        listViewData = findViewById(R.id.listDeals);
        rateAndReview = findViewById(R.id.rate_review);

        findViewById(R.id.sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentselection("sms");

            }
        });
        findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentselection("email");
            }
        });
        couponRef = FirebaseDatabase.getInstance().getReference("Deal")
                .child(getIntent().getStringExtra("shopEmailForCoupons").replace(".",""))
                .child(getIntent().getStringExtra("selectedCategoryForCoupons"))
                .child(getIntent().getStringExtra("selectedSubCategory"));

//        Log.v("shopemail",getIntent().getStringExtra("shopEmailForCoupons"));
//        Log.v("selectedCategory",getIntent().getStringExtra("selectedCategoryForCoupons"));
//        Log.v("selectedSubCategory",getIntent().getStringExtra("selectedSubCategory"));

        couponRef.addValueEventListener(new ValueEventListener() {
            ArrayList<Coupon> coupons = new ArrayList<>();
            CouponAdapter couponAdapter;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    if(child==null)
                    {
                        Toast.makeText(CouponList.this, "No Coupons Available", Toast.LENGTH_SHORT).show();
                    }
                    Coupon coupon = child.getValue(Coupon.class);
                    Log.v("coupon",coupon.toString());
                    //Log.v("couponProductName",coupon.getProductName());
                    coupons.add(coupon);

                }
                couponAdapter = new CouponAdapter(CouponList.this,R.layout.coupon_description,coupons);
                listViewData.setAdapter(couponAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rateAndReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRateAndReview = new Intent(CouponList.this,RateAndReview.class);
                goToRateAndReview.putExtra("shopNameForRate",getIntent().getStringExtra("shopnameForCoupons"));
                goToRateAndReview.putExtra("shopEmailForRate",getIntent().getStringExtra("shopEmailForCoupons"));
                startActivity(goToRateAndReview);
            }
        });
    }

    private void sendemail(String number, String smsbody) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{number});
        i.putExtra(Intent.EXTRA_SUBJECT, "Coupon Booking");
        i.putExtra(Intent.EXTRA_TEXT   , "Hello , there your i would like to buy one ticket " +"\n"+smsbody);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CouponList.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    protected void sendSMSMessage1(String s, String data) {

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(data);
        sms.sendMultipartTextMessage(s, null, parts, null, null);
        Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();

    }
    protected void sendSMSMessage(String s, String data) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(s, null, data, null, null);
            Toast.makeText(this,"Sent?: Yes:", Toast.LENGTH_SHORT).show();
        }catch (Exception e){


            Toast.makeText(this,"Sent?: Failed:", Toast.LENGTH_SHORT).show();
        }


    }
    public class CouponAdapter extends ArrayAdapter {


        public CouponAdapter(Context context, int resource, List<Coupon> coupons) {
            super(context, resource, coupons);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null)
            {
                convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.coupon_description,parent,false);
            }

            TextView productName = convertView.findViewById(R.id.product_name);
            TextView productCondition = convertView.findViewById(R.id.product_price);
            TextView productDiscount = convertView.findViewById(R.id.discount_on_product);
            CheckBox checkBox =convertView.findViewById(R.id.checkBox);
            final Coupon coupon =(Coupon)getItem(position);

            productName.setText("Product: "+coupon.getProductname());
            productCondition.setText("Price: "+coupon.getProductprice());
            productDiscount.setText("Discount: "+coupon.getDiscountrate()+" %");

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        ar.add(coupon);
                    }else{
                        ar.remove(coupon);
                    }
                }
            });

            return convertView;
        }
    }
    public void showalert(final String sms){
        final Dialog dialog=new Dialog(CouponList.this);
        dialog.setContentView(R.layout.alertview);
        dialog.setCancelable(true);

        CardForm cardForm = (CardForm) dialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(CouponList.this);

        dialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sms.equals("sms")){
                    bookusingsms();
                }else {
                    bookusingemail();
                }
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);



    }

    private void bookusingemail() {
        smsbody="";
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String date1 = s1.format(new Date());
        for(int i=0;i<ar.size();i++){
            Coupon c=ar.get(i);
            purchasepojo p=new purchasepojo();
            p.setMethodselected(Methodselected);
            p.setProductname(c.getProductname());
            p.setUnique(format+"_"+getSharedPreferences("pref",MODE_PRIVATE).getString("userid",""));
            p.setDate(date1);
            d.insertdata(p);
            smsbody=smsbody+"\n"+(i+1)+". "+"Prodcutname:"+c.getProductname()+"\n Productname:"+c.getProductname()+"\nUnique key:"+format+"_"+getSharedPreferences("pref",MODE_PRIVATE).getString("userid","")+"\n"+"Payment type: "+ Methodselected;
        }

        sendemail(getIntent().getStringExtra("email"),smsbody);
    }


    public void paymentselection(final String sms){
        final Dialog dialog=new Dialog(CouponList.this);
        dialog.setContentView(R.layout.paymentalert);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final Button placeorder= (Button) dialog.findViewById(R.id.place);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rg= (RadioGroup) dialog.findViewById(R.id.rg);
                RadioButton rb= (RadioButton) dialog.findViewById(rg.getCheckedRadioButtonId());
                Methodselected=rb.getText().toString();
                if(Methodselected.equals("Pay using Cards")){
                    showalert(sms);
                    dialog.dismiss();
                }else{
                    if(sms.equals("sms")){
                        bookusingsms();
                    }else {
                        bookusingemail();
                    }
                    dialog.dismiss();
                }
            }
        });

        ImageView iv= (ImageView) dialog.findViewById(R.id.cancel);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    private void bookusingsms() {
        smsbody="";
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String format = s.format(new Date());
        String date1 = s1.format(new Date());
        for(int i=0;i<ar.size();i++){
            Coupon c=ar.get(i);

            purchasepojo p=new purchasepojo();
            p.setMethodselected(Methodselected);
            p.setProductname(c.getProductname());
            p.setUnique(format+"_"+getSharedPreferences("pref",MODE_PRIVATE).getString("userid",""));
            p.setDate(date1);
            d.insertdata(p);
            smsbody=smsbody+"\n"+(i+1)+". "+"Prodcutname:"+c.getProductname()+"\n Productname:"+c.getProductname()+"\nUnique key:"+format+"_"+getSharedPreferences("pref",MODE_PRIVATE).getString("userid","")+"\n"+"Payment type: "+ Methodselected;
        }

        sendSMSMessage1(getIntent().getStringExtra("number"),smsbody);
    }
}

