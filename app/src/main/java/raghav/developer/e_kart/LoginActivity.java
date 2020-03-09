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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import raghav.developer.e_kart.Model.Users;
import raghav.developer.e_kart.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber,InputPassword;
    private Button logInButton;
    private ProgressDialog loadingBar;
    private com.rey.material.widget.CheckBox chkBoxRememberMe;
    private TextView adminLink,notAdminLink;
    private String parentDbname = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        logInButton = findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);

        Paper.init(this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbname = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbname = "Users";
            }
        });
    }

    public void logInUser(){
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
            Toast.makeText(this,"Please enter a phone number!!",Toast.LENGTH_LONG).show();
        if(TextUtils.isEmpty(password))
            Toast.makeText(this,"Please enter a password!!",Toast.LENGTH_LONG).show();
        else{
            loadingBar.setTitle("Exciting products ahead!!!");
            loadingBar.setMessage("Logging in");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone,password);
        }


    }

    public void allowAccessToAccount(final String phone, final String password){

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbname).child(phone).exists()){

                    if(dataSnapshot.child(parentDbname).child(phone).exists()){

                        Users usersData = dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);

                        if(usersData.getPhone().equals(phone)){

                            if(usersData.getPassword().equals(password)){

                               if(parentDbname.equals("Admins")){

                                   loadingBar.dismiss();
                                   Toast.makeText(LoginActivity.this,"Welcome Admin, You are logged in Successfully!!",Toast.LENGTH_LONG).show();
                                   Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                   startActivity(intent);

                               }

                               else if(parentDbname.equals("Users")){

                                   loadingBar.dismiss();
                                   Toast.makeText(LoginActivity.this,"Logged In Successfully!!",Toast.LENGTH_LONG).show();
                                   Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                   Prevalent.currentOnlineUser = usersData;
                                   startActivity(intent);

                               }

                            }

                            else{

                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
                else{

                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this,"This account doesn't exits!!",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
