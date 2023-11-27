package accommodate.rentapp.Utils;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import accommodate.rentapp.CommanActivity.LoginActivityHome;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;


public class Globaldata {


    public static DataLoadListener dataLoadListener;
    public static Dialog progressDialog;
    public static ArrayList<MYPostModel> myPostModelArrayList = new ArrayList<MYPostModel>();
    //
    public static ArrayList<MYPostModel> AvilableArrayList = new ArrayList<MYPostModel>();
    public PermissionListener permissionListener;
    DialogListener dialogListener;

    public static void toolbar(Activity activity, String title) {

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    public static void alertDialog(Activity activity, int layout, DialogListener dialogListener) {

        Globaldata globaldata = new Globaldata();
        globaldata.dialogListener = dialogListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(layout, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogListener.onCreated(alertDialog);
    }


    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void getOwnerPropertylistData(Activity activity, DataLoadListener dataLoadListener1) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dataLoadListener = dataLoadListener1;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ALLpropertylist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.e("Loaddataproperty", "Checkdata1");
                    myPostModelArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MYPostModel myPostModel = new MYPostModel();
                        myPostModel.setTenanttype(snapshot.child("Tenanttype").getValue(String.class));
                        myPostModel.setPID(snapshot.child("PID").getValue(String.class));
                        myPostModel.setOwnerID(snapshot.child("OwnerID").getValue(String.class));
                        myPostModel.setProperttype(snapshot.child("properttype").getValue(String.class));
                        myPostModel.setBhktype(snapshot.child("Bhktype").getValue(String.class));
                        myPostModel.setAddress(snapshot.child("Address").getValue(String.class));
                        myPostModel.setCity(snapshot.child("City").getValue(String.class));
                        myPostModel.setProvince(snapshot.child("Province").getValue(String.class));
                        myPostModel.setPincode(snapshot.child("Pincode").getValue(String.class));
                        myPostModel.setPrice(snapshot.child("Price").getValue(String.class));
                        DataSnapshot dataSnapshot1 = (DataSnapshot) snapshot.child("photolist");
                        ArrayList<String> photolist = new ArrayList<>();
                        for (DataSnapshot snapshot2 : dataSnapshot1.getChildren()) {
                            // Access data from the snapshot
                            photolist.add(snapshot2.getValue().toString());
                            // Do something with the value
                        }
                        myPostModel.setPhotolist(photolist);
                        myPostModel.setAvailability(snapshot.child("Availability").getValue(String.class));
                        // Access data from the snapshot
                        myPostModelArrayList.add(myPostModel);
                        // Do something with the value
                    }
                }

                if (myPostModelArrayList.isEmpty()) {
                    dataLoadListener.onLoadFail();
                } else {
                    dataLoadListener.onLoad();
                }

                // This method is called when data at the specified database reference changes.
                // You can access the data using dataSnapshot.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur.
                dataLoadListener.onLoadFail();
                Globaldata.progressDialogDismiss();
            }
        });

    }


    public static void getAvailableTenantData(Activity activity, DataLoadListener dataLoadListener1) {

        dataLoadListener = dataLoadListener1;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ALLpropertylist");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    AvilableArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DataSnapshot anotherDataSnapshot = snapshot;
                        for (DataSnapshot child : anotherDataSnapshot.getChildren()) {

                            MYPostModel myPostModel = new MYPostModel();
                            myPostModel.setTenanttype(child.child("Tenanttype").getValue(String.class));
                            myPostModel.setPID(child.child("PID").getValue(String.class));
                            myPostModel.setOwnerID(child.child("OwnerID").getValue(String.class));
                            myPostModel.setProperttype(child.child("properttype").getValue(String.class));
                            myPostModel.setBhktype(child.child("Bhktype").getValue(String.class));
                            myPostModel.setAddress(child.child("Address").getValue(String.class));
                            myPostModel.setCity(child.child("City").getValue(String.class));
                            myPostModel.setProvince(child.child("Province").getValue(String.class));
                            myPostModel.setPincode(child.child("Pincode").getValue(String.class));
                            myPostModel.setPrice(child.child("Price").getValue(String.class));

                            DataSnapshot dataSnapshot1 = (DataSnapshot) child.child("photolist");


                            ArrayList<String> photolist = new ArrayList<>();

                            for (DataSnapshot snapshot2 : dataSnapshot1.getChildren()) {
                                // Access data from the snapshot
                                photolist.add(snapshot2.getValue().toString());
                                // Do something with the value
                            }
                            myPostModel.setPhotolist(photolist);


                            Log.e("getsize", photolist.size() + "");


                            myPostModel.setAvailability(child.child("Availability").getValue(String.class));
                            myPostModel.setMobilenumber(child.child("mobilenumber").getValue(String.class));

                            if (myPostModel.getAvailability().equalsIgnoreCase("ON")) {
                                AvilableArrayList.add(myPostModel);
                            }

                        }

                    }

                    dataLoadListener.onLoad();
                } else {
                    dataLoadListener.onLoadFail();
                }
                // This method is called when data at the specified database reference changes.
                // You can access the data using dataSnapshot.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur.
                dataLoadListener.onLoadFail();
            }
        });


    }


    public static void checkPermissions(Activity activity, PermissionListener permissionListener) {

        Globaldata globaldata = new Globaldata();
        globaldata.permissionListener = permissionListener;

        PermissionX.init((FragmentActivity) activity).permissions(Manifest.permission.INTERNET, Manifest.permission.CALL_PHONE).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {

                if (allGranted == true) {
                    permissionListener.onGranted();
                } else {
                    permissionListener.onDenied();
                }

            }
        });
    }

    public static void checkPermissionsstorage(Activity activity, PermissionListener permissionListener) {

        Globaldata globaldata = new Globaldata();
        globaldata.permissionListener = permissionListener;

        PermissionX.init((FragmentActivity) activity).permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {

                if (allGranted == true) {
                    permissionListener.onGranted();
                } else {
                    permissionListener.onDenied();
                }

            }
        });
    }


    public static void progressDialogShow(Activity activity) {

        progressDialogDismiss();

        progressDialog = new ProgressDialog(activity) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog_progress);
                getWindow().setBackgroundDrawable(new ColorDrawable(0));
                getWindow().setGravity(17);
                getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            }
        };
        progressDialog.setCancelable(false);
        if ((activity instanceof AppCompatActivity && !((AppCompatActivity) activity).isFinishing()) && !progressDialog.isShowing()) {
            progressDialog.show();
        }


    }

    public static void progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public static void SignOut(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client_id))//Default_web_client_id will be matched with the
                .requestEmail().build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAuth.signOut();
                PreferenceManager.clear();
                Intent intent = new Intent(activity, LoginActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

                Toast.makeText(activity, "Signed out ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface DialogListener {
        void onCreated(AlertDialog dialog);
    }

    public interface PermissionListener {
        void onGranted();

        void onDenied();
    }

    public interface DataLoadListener {
        void onLoad();

        void onLoadFail();
    }


}
