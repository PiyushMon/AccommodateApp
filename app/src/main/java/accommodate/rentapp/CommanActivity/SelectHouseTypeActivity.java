package accommodate.rentapp.CommanActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import accommodate.rentapp.Activity.DashbordActivity;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;


public class SelectHouseTypeActivity extends AppCompatActivity {

    Button Tenant;
    Button Owner;
    Button next;

    String Selectintype = "no";
    Activity activity;
    EditText etmobilenumber;
    CountryCodePicker ccp;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_house_type);
        activity = this;
        mAuth = FirebaseAuth.getInstance();

        Tenant = findViewById(R.id.Tenant);
        Owner = findViewById(R.id.Owner);
        next = findViewById(R.id.next);
        etmobilenumber = findViewById(R.id.etmobilenumber);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(etmobilenumber);

        Tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectintype = Datakey.Tenant;
                Tenant.setBackgroundResource(R.drawable.btn_background);
                Owner.setBackgroundResource(R.drawable.btn_background_no);

            }
        });
        Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectintype = Datakey.Owner;
                Tenant.setBackgroundResource(R.drawable.btn_background_no);
                Owner.setBackgroundResource(R.drawable.btn_background);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobilenumber = ccp.getFullNumberWithPlus();


                if (mobilenumber.isEmpty()) {
                    etmobilenumber.requestFocus();
                    etmobilenumber.setError("Enter Mobile Number");
                } else if (Selectintype.equalsIgnoreCase("no")) {
                    Toast.makeText(activity, "Select Who are You", Toast.LENGTH_SHORT).show();
                } else {
                    updateUserTypeData(Selectintype, mobilenumber);
                }


            }
        });
    }


    private void updateUserTypeData(String selectintype, String mobilenumber) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("usertype", selectintype);
        hashMap.put("mobilenumber", mobilenumber);
        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    PreferenceManager.editor(Datakey.SelectHouseType, selectintype);
                    PreferenceManager.editor(Datakey.MobileNumber, mobilenumber);
                    if (selectintype.equalsIgnoreCase(Datakey.Tenant)) {
                        showMainActivity();
                    } else if (selectintype.equalsIgnoreCase(Datakey.Owner)) {
                        showOwnerActivity();
                    }
                }

            }
        });


    }


    private void showMainActivity() {
        ;
        Globaldata.getAvailableTenantData(activity, new Globaldata.DataLoadListener() {
            @Override
            public void onLoad() {
                Intent intent = new Intent(activity, DashbordActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoadFail() {
                Intent intent = new Intent(activity, DashbordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showOwnerActivity() {
        Globaldata.getOwnerPropertylistData(activity, new Globaldata.DataLoadListener() {
            @Override
            public void onLoad() {
                Intent intent = new Intent(activity, DashbordActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoadFail() {
                Intent intent = new Intent(activity, DashbordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}