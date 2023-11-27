package accommodate.rentapp.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import accommodate.rentapp.Adapter.PhotoAdapterAdapter;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;
import gun0912.tedimagepicker.builder.TedImagePicker;


public class CreateNewPostActivity extends AppCompatActivity {

    public static String Proprty_type = "";
    public static String Tenant_type = "";
    public static String Bhk_type = "";
    public static String Avabolity_type = "";
    RadioGroup properttype, Tenanttype, nuberoffroom;
    Activity activity;
    EditText Address, City, Province, Pincode, startBudget;
    Button addProperty, Rented, Available, selectPhoto;
    int edit;
    MYPostModel myPostModel;
    FirebaseUser firebaseUser;
    RecyclerView propertyimagelist;

    static ProgressDialog mProgressDialog;
    static int countimage = 0;
    public static ArrayList<String> templistforsavdatafinal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        activity = this;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Globaldata.toolbar(activity, "Add New Post");
        Tenanttype = (RadioGroup) findViewById(R.id.tenanttype);
        properttype = (RadioGroup) findViewById(R.id.properttype);
        nuberoffroom = (RadioGroup) findViewById(R.id.nuberoffroom);
        propertyimagelist = (RecyclerView) findViewById(R.id.propertyimagelist);

        Address = (EditText) findViewById(R.id.Address);
        City = (EditText) findViewById(R.id.City);
        Province = (EditText) findViewById(R.id.Province);
        Pincode = (EditText) findViewById(R.id.Pincode);
        startBudget = (EditText) findViewById(R.id.startBudget);

        Available = (Button) findViewById(R.id.Available);
        Rented = (Button) findViewById(R.id.Rented);

        selectPhoto = (Button) findViewById(R.id.selectPhoto);

        addProperty = (Button) findViewById(R.id.addProperty);

        edit = getIntent().getIntExtra("edit", 0);

        if (edit == 1) {
            myPostModel = (MYPostModel) getIntent().getSerializableExtra(Datakey.MYPostModelKey);
            SetOLDDATA(myPostModel);
        }


        Rented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avabolity_type = "OFF";
                Rented.setBackgroundResource(R.drawable.btn_background);
                Available.setBackgroundResource(R.drawable.btn_background_no);
            }
        });
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Globaldata.checkPermissions(activity, new Globaldata.PermissionListener() {
                    @Override
                    public void onGranted() {
                        TedImagePicker.with(activity)
                                .startMultiImage(uriList -> {
                                    uploadImages((List<Uri>) uriList);
                                });
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(activity, "Permission Not granted", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        Available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avabolity_type = "ON";
                Available.setBackgroundResource(R.drawable.btn_background);
                Rented.setBackgroundResource(R.drawable.btn_background_no);
            }
        });


        Tenanttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Tenant_type = radioButton.getText().toString();
            }
        });

        properttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Proprty_type = radioButton.getText().toString();
            }
        });

        nuberoffroom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Bhk_type = radioButton.getText().toString();
            }
        });


        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Address1 = Address.getText().toString();
                String City1 = City.getText().toString();
                String Province1 = Province.getText().toString();
                String Pincode1 = Pincode.getText().toString();
                int myNum;
                if (!startBudget.getText().toString().isEmpty()) {
                    myNum = Integer.parseInt(startBudget.getText().toString());
                } else {
                    myNum = 0;
                }


                if (Tenant_type.equals("")) {
                    Toast.makeText(activity, "" + "Select Type Off Tenant", Toast.LENGTH_SHORT).show();
                } else if (Proprty_type.equals("")) {
                    Toast.makeText(activity, "" + "Select Type Off Property", Toast.LENGTH_SHORT).show();
                } else if (Bhk_type.equals("")) {
                    Toast.makeText(activity, "" + "Select Number Of Room", Toast.LENGTH_SHORT).show();
                } else if (Address1.isEmpty()) {
                    Address.requestFocus();
                    Address.setError("Please Enter Address");
                } else if (City1.isEmpty()) {
                    City.requestFocus();
                    City.setError("Please Enter City");
                } else if (Province1.isEmpty()) {
                    Province.requestFocus();
                    Province.setError("Please Enter Province");
                } else if (Pincode1.isEmpty()) {
                    Pincode.requestFocus();
                    Pincode.setError("Please Enter Pincode");
                } else if (startBudget.getText().toString().isEmpty()) {
                    startBudget.requestFocus();
                    startBudget.setError("Please Enter price ");
                } else if (Avabolity_type.equals("")) {
                    Toast.makeText(activity, "" + "Select Property Avabolity", Toast.LENGTH_SHORT).show();
                } else if (myNum == 0) {
                    startBudget.requestFocus();
                    startBudget.setError("Price  0 Are  not Allow");
                } else if (templistforsavdatafinal.size() == 0) {
                    Toast.makeText(activity, "" + "Select at least One Photo", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("Tenanttype", Tenant_type);
                    updates.put("properttype", Proprty_type);
                    updates.put("Bhktype", "" + Bhk_type);
                    updates.put("Address", "" + Address1);
                    updates.put("City", "" + City1);
                    updates.put("Province", "" + Province1);
                    updates.put("Pincode", "" + Pincode1);
                    updates.put("Availability", Avabolity_type);
                    updates.put("photolist", templistforsavdatafinal);
                    updates.put("Price", startBudget.getText().toString());
                    UpoadData(updates);
                }


            }
        });

    }

    private void uploadImages(List<Uri> imageUris) {


        countimage = 0;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("Uploading Image (" + countimage + "/" + imageUris.size() + ")");
        mProgressDialog.show();
        for (Uri imageUri : imageUris) {

            // Create a unique filename for each image
            String filename = "image_" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child("images/" + filename);
            // Upload the image
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle successful uploads

                        if (taskSnapshot.getTask().isComplete()) {

                            int countdes = countimage;
                            countdes++;
                            countimage = countdes;
                            if (countimage == imageUris.size()) {
                                mProgressDialog.dismiss();
                            } else {
                                mProgressDialog.setTitle("Uploading Image (" + countimage + "/" + imageUris.size() + ")");
                                mProgressDialog.show();
                            }


                            Log.d("Image vfgf", "Image uploaded successfully: " + countimage);
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Handle the download URL here

                                    String downloadUrl2 = downloadUri.toString();
                                    templistforsavdatafinal.add(downloadUrl2);
                                    getPhotoList(templistforsavdatafinal);

                                    Log.d("Image uploaded", "Image uploaded successfully: " + downloadUrl2);
                                    // Now you can use 'downloadUrl' as the location of your uploaded file
                                    // For example, you might want to store this URL in your database
                                }
                            });

                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(activity, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                        mProgressDialog.dismiss();
                        Log.e("Image uploaded", "", exception);
                    });

        }


    }

    private void getPhotoList(ArrayList<String> templistforsavdata) {
        propertyimagelist.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        PhotoAdapterAdapter photoAdapter = new PhotoAdapterAdapter(activity, templistforsavdata);
        propertyimagelist.setAdapter(photoAdapter);

    }

    private void SetOLDDATA(MYPostModel myPostModel) {


        if (myPostModel.getTenanttype().equalsIgnoreCase("Rent")) {
            Tenanttype.check(R.id.rent);
            Tenant_type = "Rent";
        } else if (myPostModel.getTenanttype().equalsIgnoreCase("pg")) {
            Tenanttype.check(R.id.pg);
            Tenant_type = "pg";
        }
        if (myPostModel.getBhktype().equalsIgnoreCase("1 BHK")) {
            nuberoffroom.check(R.id.bhk1);
            Bhk_type = "1 BHK";
        } else if (myPostModel.getBhktype().equalsIgnoreCase("2 BHK")) {
            nuberoffroom.check(R.id.bhk2);
            Bhk_type = "2 BHK";
        } else if (myPostModel.getBhktype().equalsIgnoreCase("3 BHK")) {
            nuberoffroom.check(R.id.bhk3);
            Bhk_type = "3 BHK";
        } else if (myPostModel.getBhktype().equalsIgnoreCase("4 BHK")) {
            nuberoffroom.check(R.id.bhk4);
            Bhk_type = "4 BHK";
        }


        if (myPostModel.getProperttype().equalsIgnoreCase("Residential Apartment")) {
            properttype.check(R.id.Residential);
            Proprty_type = "Residential Apartment";
        } else if (myPostModel.getProperttype().equalsIgnoreCase("Independent House/Villa")) {
            properttype.check(R.id.Independent);
            Proprty_type = "Independent House/Villa";
        } else if (myPostModel.getProperttype().equalsIgnoreCase("Room")) {
            properttype.check(R.id.Room);
            Proprty_type = "Room";
        } else if (myPostModel.getProperttype().equalsIgnoreCase("Sharing room")) {
            properttype.check(R.id.SharingRoom);
            Proprty_type = "Sharing room";
        } else if (myPostModel.getProperttype().equalsIgnoreCase("Basement")) {
            properttype.check(R.id.Basement);
            Proprty_type = "Basement";
        }

        if (myPostModel.getAvailability().equalsIgnoreCase("ON")) {
            Avabolity_type = "ON";
            Available.setBackgroundResource(R.drawable.btn_background);
            Rented.setBackgroundResource(R.drawable.btn_background_no);
        } else {
            Avabolity_type = "OFF";
            Rented.setBackgroundResource(R.drawable.btn_background);
            Available.setBackgroundResource(R.drawable.btn_background_no);
        }


        properttype = (RadioGroup) findViewById(R.id.properttype);
        nuberoffroom = (RadioGroup) findViewById(R.id.nuberoffroom);

        Address.setText(myPostModel.getAddress());
        City.setText(myPostModel.getCity());
        Province.setText(myPostModel.getProvince());
        Pincode.setText(myPostModel.getPincode());
        startBudget.setText(myPostModel.getPrice());
        templistforsavdatafinal = myPostModel.getPhotolist();
        getPhotoList(templistforsavdatafinal);


    }

    private void UpoadData(Map<String, Object> updates) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ALLpropertylist");
        if (edit == 1) {
            DatabaseReference newChildRef = databaseReference.child(firebaseUser.getUid()).child(myPostModel.getPID());
            Map<String, Object> updates2 = updates;
            updates2.put("PID", myPostModel.getPID());
            updates2.put("OwnerID", myPostModel.getOwnerID());
            newChildRef.setValue(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Property Update Successfully...!", Toast.LENGTH_SHORT).show();
                        finish();
//                        Globaldata.getOwnerPropertylistData(activity, new Globaldata.DataLoadListener() {
//                            @Override
//                            public void onLoad() {
//                                Globaldata.progressDialogDismiss();
//                                onBackPressed();
//                            }
//
//                            @Override
//                            public void onLoadFail() {
//                                Globaldata.progressDialogDismiss();
//                                onBackPressed();
//                            }
//                        });
                    } else {
                        Toast.makeText(activity, "Property Not Updateed...!", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Globaldata.progressDialogDismiss();
                    Toast.makeText(activity, "Property Not Inserted...!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            DatabaseReference newChildRef = databaseReference.child(firebaseUser.getUid()).push();
            Map<String, Object> updates2 = updates;

            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users");
            DatabaseReference newChildRef2 = databaseReference2.child(firebaseUser.getUid()).child("mobilenumber");

            newChildRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String mobilenumber = dataSnapshot.getValue().toString();
                    updates2.put("PID", newChildRef.getKey());
                    updates2.put("mobilenumber", mobilenumber);
                    updates2.put("OwnerID", firebaseUser.getUid());
                    newChildRef.setValue(updates2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                Toast.makeText(activity, "Property Inserted Successfully...!", Toast.LENGTH_SHORT).show();
                                finish();
//                                Globaldata.getOwnerPropertylistData(activity, new Globaldata.DataLoadListener() {
//                                    @Override
//                                    public void onLoad() {
//                                        Globaldata.progressDialogDismiss();
//                                        onBackPressed();
//                                    }
//
//                                    @Override
//                                    public void onLoadFail() {
//                                        Globaldata.progressDialogDismiss();
//                                        onBackPressed();
//                                    }
//                                });


                            } else {
                                Globaldata.progressDialogDismiss();
                                Toast.makeText(activity, "Property Not Inserted...!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Globaldata.progressDialogDismiss();
                            Toast.makeText(activity, "Property Not Inserted...!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Globaldata.progressDialogDismiss();
                    Toast.makeText(activity, "Property Not Inserted...!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}