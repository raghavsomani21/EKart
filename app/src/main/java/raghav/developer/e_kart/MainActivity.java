package raghav.developer.e_kart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import raghav.developer.e_kart.Model.Users;
import raghav.developer.e_kart.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,logInButton;
    private String userPhoneKey,userPasswordKey;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        joinNowButton = findViewById(R.id.main_join_now_btn);
        logInButton = findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Moving to the LogIn page
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Moving to the register page
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        if(userPhoneKey!=null && userPasswordKey!=null){
            loadingBar.setTitle("Exciting products ahead!!!");
            loadingBar.setMessage("Logging in");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccess(userPhoneKey,userPasswordKey);
        }
    }
    private void allowAccess(final String phone, final String password){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("Users").child(phone).exists()){
                        Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                            if(usersData.getPassword().equals(password)){
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this,"Logged In Successfully!!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                            }
                        }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"This account doesn't exits!!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
