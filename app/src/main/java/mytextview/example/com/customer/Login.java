package mytextview.example.com.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private static final int MULTIPLE_PERMISSIONS = 123;
    private EditText EmailID;
    private EditText Password;
    private Button signIn;
    private Button Register;
    FirebaseAuth mAuth;
    private Button forgotPassword;
    FirebaseDatabase database;
    DatabaseReference custRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermissions();
        EmailID = findViewById(R.id.etemail);
        Password = findViewById(R.id.etpassword);
        signIn = findViewById(R.id.btnlogin);
        Register = findViewById(R.id.btnregister);
        forgotPassword = findViewById(R.id.btnfrgt);
        forgotPassword.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        custRef = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();
        if(getSharedPreferences("pref",MODE_PRIVATE).contains("userid")){
            startActivity(new Intent(Login.this, dashboard1.class));
            Login.this.finish();
        }
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                try {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    String userEmail = firebaseUser.getEmail();
                    getSharedPreferences("pref",MODE_PRIVATE).edit().putString("userid",userId).commit();
                    Log.d("data",userId);
                }catch (Exception e){

                }

            }

        });


       /* EmailID.setText(getIntent().getStringExtra("email"));
        Password.setText(getIntent().getStringExtra("password"));*/

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customerLogin();
                //tartActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        EmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>5 && charSequence.toString().contains("@") && charSequence.toString().contains("."))
                {
                    forgotPassword.setEnabled(true);
                }
                else {
                    forgotPassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = EmailID.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Login.this,"Email sent",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length()>=6;
    }

    public void customerLogin() {
        String customerEmail = EmailID.getText().toString();
        String customerPassword = Password.getText().toString();

        if(!isEmailValid(customerEmail))
        {
            EmailID.setError("Email not valid");
            EmailID.requestFocus();
            return;
        }

        if(!isPasswordValid(customerPassword))
        {
            Password.setError("Minimum 6 characters required");
            Password.requestFocus();
            return;
        }

        final ProgressDialog signInDialog = new ProgressDialog(Login.this);
        signInDialog.setMessage("Logging In");
        signInDialog.show();

        mAuth.signInWithEmailAndPassword(customerEmail, customerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, dashboard1.class));
                    Login.this.finish();
                }

                if(!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG);
                }
            }
        });

    }

    String[] permissions= new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS
    };
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode==MULTIPLE_PERMISSIONS)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // permissions granted.
            } else {
                // no permissions granted.
            }
            return;
        }
    }

}
