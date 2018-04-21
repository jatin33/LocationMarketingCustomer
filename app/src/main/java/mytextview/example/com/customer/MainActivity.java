package mytextview.example.com.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.User;

public class MainActivity extends AppCompatActivity {
    EditText CustomerName;
    EditText Phone;
    Button Register;
    Button Login;
    EditText Password;
    EditText EmailId;
    EditText Dob;
    EditText City;
    Spinner Gender;
    DatabaseReference customerRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customerRef = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();


        CustomerName = findViewById(R.id.etname);
        Phone = findViewById(R.id.etphone);
        Register = findViewById(R.id.btnregister);
        Login = findViewById(R.id.btnlogin);
        Password = findViewById(R.id.etpasswoed);
        EmailId = findViewById(R.id.etemail);
        Gender = findViewById(R.id.sgender);
        Dob = findViewById(R.id.etdob);
        City = findViewById(R.id.etcity);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCustomer();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Login.class));
                MainActivity.this.finish();
                // startActivity(new Intent(MainActivity.this,Dashboard.class));
            }
        });
    }

    void registerCustomer() {
        String customername = CustomerName.getText().toString();
        final String customerpassword = Password.getText().toString();
        final String customeremail = EmailId.getText().toString();
        String customerphone = Phone.getText().toString();
        String customergender = Gender.getSelectedItem().toString();
        String customerdob = Dob.getText().toString();
        String customercity = City.getText().toString();

        //Validations
        if (TextUtils.isEmpty(customername)) {
            CustomerName.setError("Cannot be empty");
            CustomerName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(customercity)) {
            City.setError("Cannot be empty");
            City.requestFocus();
        }
        if (TextUtils.isEmpty(customerphone)) {
            Phone.setError("Cannot be empty");
            Phone.requestFocus();
        }

        if(TextUtils.isEmpty(customerdob)) {
            Dob.setError("Cannot be Empty");
            Dob.requestFocus();
        }

        if(!isValidPhone(customerphone))
        {
            Phone.setError("Invalid Contact No");
            Phone.requestFocus();
            return;
        }

        if(!isEmailValid(customeremail))
        {
            EmailId.setError("Email not valid");
            EmailId.requestFocus();
            return;
        }

        if(!isPasswordValid(customerpassword))
        {
            Password.setError("Minimum 6 charcters required");
            Password.requestFocus();
            return;
        }


        String id = customerRef.push().getKey();
        User user = new User(customername,customerpassword,customeremail,customerphone,customergender,customerdob,customercity);
        customerRef.child(id).setValue(user);
        mAuth.createUserWithEmailAndPassword(customeremail,customerpassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Customer added",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    intent.putExtra("email",customeremail);
                    intent.putExtra("password",customerpassword);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Registration unsuccessful",Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length()>=6;
    }

    private boolean isValidPhone(String phone){
        return phone.length()>=10&TextUtils.isDigitsOnly(phone);
    }

}



