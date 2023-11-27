package accommodate.rentapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.SliderView;

import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.Model.SliderAdapter;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;


public class ViewPropertyActivity extends AppCompatActivity {

    MYPostModel myPostModel;

    TextView address, city, Province, tenanttype, propertytype, bhktype;
    Activity activity;

    Button Call, chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);
        activity = this;
        Globaldata.toolbar(activity, "Property Details");
        myPostModel = (MYPostModel) getIntent().getSerializableExtra(Datakey.MYPostModelKey);


        Call = findViewById(R.id.Call);
        chat = findViewById(R.id.chat);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        Province = findViewById(R.id.Province);
        tenanttype = findViewById(R.id.tenanttype);
        propertytype = findViewById(R.id.propertytype);
        bhktype = findViewById(R.id.bhktype);


        SetDATA(myPostModel);

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Globaldata.checkPermissions(activity, new Globaldata.PermissionListener() {
                    @Override
                    public void onGranted() {


                        Globaldata.alertDialog(activity, R.layout.callnumber, new Globaldata.DialogListener() {
                            @Override
                            public void onCreated(AlertDialog dialog) {
                                dialog.setCancelable(true);
                                TextView mobilenumber22 = dialog.findViewById(R.id.mobilenumber);
                                mobilenumber22.setText(myPostModel.getMobilenumber());

                                Button updatenumber = dialog.findViewById(R.id.updatenumber);
                                updatenumber.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + myPostModel.getMobilenumber()));
                                        startActivity(callIntent);

                                    }
                                });
                            }
                        });


                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(activity, "Permission Not granted", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("Hii..");
                stringBuilder.append("\n\nProperty Details:-\n\n" + myPostModel.getBhktype() + " " + myPostModel.getProperttype() + " For " + myPostModel.getTenanttype());
                stringBuilder.append("\n\nLocation Details:-\n\n" + myPostModel.getAddress() + "," + myPostModel.getCity() + "," + myPostModel.getProvince() + "," + myPostModel.getPincode());
                stringBuilder.append("\n\nPrice Details:-\n\n" + myPostModel.getPrice() + "  $ / Per Month");
                stringBuilder.append("\n\n\n" + "Need More Details  For Accommodate This Property....!");


                Intent intent = new Intent(activity, MessageActivity.class);
                intent.putExtra("userid", myPostModel.getOwnerID());
                intent.putExtra("copymsg", stringBuilder.toString());
                activity.startActivity(intent);
            }
        });


    }

    private void SetDATA(MYPostModel myPostModel) {
        address.setText(myPostModel.getAddress());
        city.setText(myPostModel.getCity());
        Province.setText(myPostModel.getProvince());
        tenanttype.setText(myPostModel.getTenanttype());
        propertytype.setText(myPostModel.getProperttype());
        bhktype.setText(myPostModel.getBhktype());

        SliderView sliderView = findViewById(R.id.slider);

        Log.e("getsize", myPostModel.getPhotolist().size() + "");
        SliderAdapter adapter = new SliderAdapter(this, myPostModel.getPhotolist());

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_INHERIT);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }
}