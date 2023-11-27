package accommodate.rentapp.CommanActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import accommodate.rentapp.Activity.DashbordActivity;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {


    Activity activity;


    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        activity = this;

        mAuth = FirebaseAuth.getInstance();

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        TextView textViewSwitchToLogin = findViewById(R.id.tvSwitchToLogin);
        textViewSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });

    }

    private void registerUser() {
        Globaldata.progressDialogShow(activity);
        EditText username = findViewById(R.id.etUserName);
        EditText etRegisterEmail = findViewById(R.id.etRegisterEmail);
        EditText etRegisterPassword = findViewById(R.id.etRegisterPassword);

        String usernamedata = username.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();

        if (usernamedata.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            String email = firebaseUser.getEmail();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", usernamedata);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("usertype", "default");
                            hashMap.put("mobilenumber", "default");


                            PreferenceManager.editor(Datakey.USERNAME, usernamedata);
                            PreferenceManager.editor(Datakey.USEREMAIL, email);
                            PreferenceManager.editor(Datakey.USERProfile, "default");
                            PreferenceManager.editor(Datakey.MobileNumber, "default");
                            PreferenceManager.editor(Datakey.USERTYPE, "default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (task.isComplete()) {
                                            showMainActivity("default");
                                        }
                                    }
                                    Globaldata.progressDialogDismiss();
                                }
                            });
                        } else {
                            Globaldata.progressDialogDismiss();
                            Toast.makeText(activity, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void showMainActivity(String user) {
        if (user.equalsIgnoreCase("default")) {
            Intent intent = new Intent(activity, SelectHouseTypeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            if (user.equalsIgnoreCase(Datakey.Tenant)) {
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Tenant);
            } else if (user.equalsIgnoreCase(Datakey.Owner)) {
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Owner);
            }
            Intent intent = new Intent(activity, DashbordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void switchToLogin() {
        Intent intent = new Intent(this, EnterLogInDetailsActivity.class);
        startActivity(intent);
        finish();
    }
}