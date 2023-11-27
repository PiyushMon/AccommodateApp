package accommodate.rentapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import accommodate.rentapp.Activity.DashbordActivity;
import accommodate.rentapp.CommanActivity.LoginActivityHome;
import accommodate.rentapp.CommanActivity.SelectHouseTypeActivity;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {


    Activity activity;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        activity = this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if (firebaseUser != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChekUserTypeData();
                }
            }, 1500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivityHome.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }
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
                    String userid = firebaseUser.getUid();
                    String usernamedata = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    String imageURL = firebaseUser.getPhotoUrl().toString();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", usernamedata);
                    hashMap.put("email", email);
                    hashMap.put("imageURL", imageURL);
                    hashMap.put("usertype", "default");
                    hashMap.put("mobilenumber", "default");

                    PreferenceManager.editor(Datakey.USERNAME, usernamedata);
                    PreferenceManager.editor(Datakey.USEREMAIL, email);
                    PreferenceManager.editor(Datakey.USERProfile, imageURL);
                    PreferenceManager.editor(Datakey.MobileNumber, "default");
                    PreferenceManager.editor(Datakey.USERTYPE, "default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                showMainActivity("default");
                            }
                            Globaldata.progressDialogDismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Globaldata.progressDialogDismiss();
                // Handle any errors here
            }
        });
    }

    private void showMainActivity(String usertype) {
        if (usertype.equalsIgnoreCase("default")) {
            Intent intent = new Intent(this, SelectHouseTypeActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (usertype.equalsIgnoreCase(Datakey.Tenant)) {
                Globaldata.getAvailableTenantData(activity, new Globaldata.DataLoadListener() {
                    @Override
                    public void onLoad() {
                        PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Tenant);
                        Intent intent = new Intent(activity, DashbordActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onLoadFail() {
                        PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Tenant);
                        Intent intent = new Intent(activity, DashbordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (usertype.equalsIgnoreCase(Datakey.Owner)) {
                Globaldata.getOwnerPropertylistData(activity, new Globaldata.DataLoadListener() {
                    @Override
                    public void onLoad() {
                        PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Owner);
                        Intent intent = new Intent(activity, DashbordActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onLoadFail() {
                        PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Owner);
                        Intent intent = new Intent(activity, DashbordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }


}