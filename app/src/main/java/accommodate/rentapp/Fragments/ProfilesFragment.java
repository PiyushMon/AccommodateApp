package accommodate.rentapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import accommodate.rentapp.Utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;

;

public class ProfilesFragment extends Fragment {


    CircleImageView profilephoto;
    Activity activity;
    TextView username;
    TextView Email;
    Button logount;
    EditText mobilenumber;
    ImageView edit;

    public ProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        activity = getActivity();
        profilephoto = view.findViewById(R.id.profilephoto);
        username = view.findViewById(R.id.username);
        Email = view.findViewById(R.id.Email);
        logount = view.findViewById(R.id.logount);
        mobilenumber = view.findViewById(R.id.mobilenumber);
        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globaldata.alertDialog(activity, R.layout.editnumber, new Globaldata.DialogListener() {
                    @Override
                    public void onCreated(AlertDialog dialog) {
                        dialog.setCancelable(true);
                        TextView mobilenumber22 = dialog.findViewById(R.id.mobilenumber);
                        mobilenumber22.setText("Old Number :- " + PreferenceManager.getMobileNumber());

                        EditText etmobilenumber = dialog.findViewById(R.id.etmobilenumber);

                        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
                        ccp.registerCarrierNumberEditText(etmobilenumber);
                        Button updatenumber = dialog.findViewById(R.id.updatenumber);
                        updatenumber.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String mobilenumber22ddd = ccp.getFullNumberWithPlus();


                                if (mobilenumber22ddd.isEmpty()) {
                                    etmobilenumber.requestFocus();
                                    etmobilenumber.setError("Enter Mobile Number");
                                } else {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("mobilenumber", mobilenumber22ddd);
                                    reference.updateChildren(hashMap);
                                    PreferenceManager.editor(Datakey.MobileNumber, mobilenumber22ddd);
                                    mobilenumber.setText(PreferenceManager.getMobileNumber());
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        });



        Glide.with(activity).load(PreferenceManager.getUserprofile()).error(R.drawable.ic_user).into(profilephoto);
        username.setText(PreferenceManager.getUsername());
        Email.setText(PreferenceManager.getUserEmail());
        mobilenumber.setText(PreferenceManager.getMobileNumber());
        logount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globaldata.SignOut(activity);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mobilenumber.setText(PreferenceManager.getMobileNumber());
    }
}