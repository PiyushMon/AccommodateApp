package accommodate.rentapp.CommanActivity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import accommodate.rentapp.Activity.DashbordActivity;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;


public class LoginActivityHome extends AppCompatActivity {


    private static final int RC_SIGN_IN = 100;
    Activity activity;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    Button enterlogin, entercreateaccount, signinwithgoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        activity = this;
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))//Default_web_client_id will be matched with the
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signinwithgoogle = findViewById(R.id.signinwithgoogle);
        signinwithgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleLogin();
            }
        });


        enterlogin = findViewById(R.id.enterlogin);
        enterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwichToLogin();
            }
        });
        entercreateaccount = findViewById(R.id.entercreateaccount);
        entercreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwichToRegistration();
            }
        });
    }


    private void SwichToLogin() {
        Intent intent = new Intent(this, EnterLogInDetailsActivity.class);
        startActivity(intent);

    }

    private void SwichToRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


    private void GoogleLogin() {
        Globaldata.progressDialogShow(activity);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Globaldata.progressDialogDismiss();
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google Sign in Failed " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Globaldata.progressDialogShow(activity);
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ChekUserTypeData();
                            Globaldata.progressDialogDismiss();
                        } else {
                            Globaldata.progressDialogDismiss();
                            Toast.makeText(LoginActivityHome.this, "Firebase Authentication failed:" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void ChekUserTypeData() {
        Globaldata.progressDialogShow(activity);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
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
                                Globaldata.progressDialogDismiss();
                            }

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
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Tenant);
                Intent intent = new Intent(this, DashbordActivity.class);
                startActivity(intent);
                finish();
            } else if (usertype.equalsIgnoreCase(Datakey.Owner)) {
                PreferenceManager.editor(Datakey.SelectHouseType, Datakey.Owner);
                Intent intent = new Intent(activity, DashbordActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

}