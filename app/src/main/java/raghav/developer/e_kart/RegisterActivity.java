package raghav.developer.e_kart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputNumber, InputPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputNumber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = InputName.getText().toString();
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        //Checking if any of the field is empty
        if (TextUtils.isEmpty(name))
            Toast.makeText(this, "Name field cannot be empty!!", Toast.LENGTH_LONG).show();
        if (TextUtils.isEmpty(phone))
            Toast.makeText(this, "Please enter a phone number!!", Toast.LENGTH_LONG).show();
        if (TextUtils.isEmpty(password))
            Toast.makeText(this, "Please enter a password!!", Toast.LENGTH_LONG).show();
        else {
            //if all is good,save this data onto firebase
            loadingBar.setTitle("Create account");
            loadingBar.setMessage("Please wait, while we check the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            int k;
            k = passCheck(password);
            if (k == 1)
                validate(name, phone, password);
            else {
                loadingBar.dismiss();
                Toast.makeText(this, "Password should be atleast 8 characters long and contain atleast one speacial character,one number,and one uppercase letter", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void validate(final String name, final String phone, final String password) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("password", password);
                    userDataMap.put("name", name);
                    rootRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Congratulations!!! Your account has been created", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "This  number already exists", Toast.LENGTH_LONG).show();
                    //Toast.makeText(RegisterActivity.this, "Please try using a different phone number", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    int passCheck(String p){
        if(p.length()<8)
            return 0;
        else
            return 1;
    }
}
