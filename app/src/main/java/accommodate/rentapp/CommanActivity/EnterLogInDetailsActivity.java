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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import accommodate.rentapp.Activity.DashbordActivity;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;


public class EnterLogInDetailsActivity extends AppCompatActivity {


    Activity activity;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_log_in_details);
        activity = this;
        mAuth = FirebaseAuth.getInstance();


        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });


        TextView tvSwitchToRegister = findViewById(R.id.tvSwitchToRegister);
        tvSwitchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegister();
            }
        });
    }

    private void authenticateUser() {
        Globaldata.progressDialogShow(activity);
        EditText etLoginEmail = findViewById(R.id.etLoginEmail);
        EditText etLoginPassword = findViewById(R.id.etLoginPassword);

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(EnterLogInDetailsActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ChekUserTypeData();
                            Globaldata.progressDialogDismiss();

                        } else {
                            Globaldata.progressDialogDismiss();
                            Toast.makeText(EnterLogInDetailsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(EnterLogInDetailsActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Globaldata.progressDialogDismiss();
                        Toast.makeText(EnterLogInDetailsActivity.this, "" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void ChekUserTypeData() {
        DatabaseReference reference;
        Globaldata.progressDialogShow(activity);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Globaldata.progressDialogDismiss();
                    String username = (String) dataSnapshot.child("username").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String profilephoto = (String) dataSnapshot.child("imageURL").getValue();
                    String user = (String) dataSnapshot.child("usertype").getValue();
                    String mobilenumber = (String) dataSnapshot.child("mobilenumber").getValue();
                    PreferenceManager.editor(Datakey.USERNAME, username);
                    PreferenceManager.editor(Datakey.USEREMAIL, email);
                    PreferenceManager.editor(Datakey.USERProfile, profilephoto);
                    PreferenceManager.editor(Datakey.MobileNumber, mobilenumber);
                    PreferenceManager.editor(Datakey.USERTYPE, user);
                    showMainActivity(user);
                    Globaldata.progressDialogDismiss();
                } else {
                    Globaldata.progressDialogDismiss();
                    Toast.makeText(activity, "User Not Exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Globaldata.progressDialogDismiss();
                // Handle any errors here
            }
        });
    }


    private void showMainActivity(String user) {
        if (user.equalsIgnoreCase("default")) {
            Intent intent = new Intent(this, SelectHouseTypeActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (user.equalsIgnoreCase(Datakey.Tenant)) {
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Tenant);
                Intent intent = new Intent(this, DashbordActivity.class);
                startActivity(intent);
                finish();
            } else if (user.equalsIgnoreCase(Datakey.Owner)) {
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Owner);
                Intent intent = new Intent(this, DashbordActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    private void switchToRegister() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

}