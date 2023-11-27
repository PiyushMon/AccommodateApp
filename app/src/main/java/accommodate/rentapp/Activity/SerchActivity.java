package accommodate.rentapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import accommodate.rentapp.Adapter.AvilablehouselistAdapter;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;

public class SerchActivity extends AppCompatActivity {

    Activity activity;
    RecyclerView mypost;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<MYPostModel> AvilableArrayListtemp = new ArrayList<>();

    String passkeyhome;
    EditText editserch;
    AvilablehouselistAdapter avilablehouselistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch);

        activity = this;
        Globaldata.toolbar(activity, "Search Accommodate ");
        mypost = findViewById(R.id.mypost);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        editserch = findViewById(R.id.editserch);
        mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        passkeyhome = getIntent().getStringExtra(Datakey.PasskeyHome);

        LoadData();

        editserch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    filterData(s.toString().toLowerCase());
                } else {
                    if (!AvilableArrayListtemp.isEmpty()) {
                        mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                        avilablehouselistAdapter = new AvilablehouselistAdapter(activity, AvilableArrayListtemp);
                        mypost.setAdapter(avilablehouselistAdapter);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void filterData(CharSequence query) {

        ArrayList<MYPostModel> filteredResults = new ArrayList<>();
        for (MYPostModel model : AvilableArrayListtemp) {
            if (model.getTenanttype().toLowerCase().contains(query) ||model.getBhktype().toLowerCase().contains(query) ||model.getProperttype().toLowerCase().contains(query) ||model.getPrice().toLowerCase().contains(query) ||model.getAddress().toLowerCase().contains(query) || model.getCity().toLowerCase().contains(query) || model.getProvince().toLowerCase().contains(query)|| model.getPincode().toLowerCase().contains(query)) {
                filteredResults.add(model);
            }
            // Add your filtering conditions here based on multiple model values
//            if (model.getTenanttype().toLowerCase().contains(query)
//                    || model.getProperttype().toLowerCase().contains(query)  || model.getAddress().toLowerCase().contains(query) || model.getCity().toLowerCase().contains(query)|| model.getProvince().toLowerCase().contains(query)|| model.getPincode().toLowerCase().contains(query)|| model.getAvailability().toLowerCase().contains(query)|| model.getMobilenumber().toLowerCase().contains(query)) {
//                filteredResults.add(model);
//            }
        }

        avilablehouselistAdapter = new AvilablehouselistAdapter(activity, filteredResults);
        mypost.setAdapter(avilablehouselistAdapter);

    }


    private void LoadData() {

        if (passkeyhome.equalsIgnoreCase("all")) {
            if (Globaldata.AvilableArrayList.isEmpty()) {
                Globaldata.getAvailableTenantData(activity, new Globaldata.DataLoadListener() {
                    @Override
                    public void onLoad() {
                        AvilableArrayListtemp = Globaldata.AvilableArrayList;
                    }

                    @Override
                    public void onLoadFail() {
                        AvilableArrayListtemp = Globaldata.AvilableArrayList;
                    }
                });
            } else {
                AvilableArrayListtemp = Globaldata.AvilableArrayList;
            }
        } else {
            ArrayList<MYPostModel> filteredItems = new ArrayList<MYPostModel>();
            for (MYPostModel myPostModel : Globaldata.AvilableArrayList) {
                boolean TenantMatched = true;
                if (!passkeyhome.contains(myPostModel.getTenanttype())) {
                    TenantMatched = false;
                }

                if (TenantMatched) {
                    filteredItems.add(myPostModel);
                }
            }
            AvilableArrayListtemp = filteredItems;
        }


        if (!AvilableArrayListtemp.isEmpty()) {
            mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            avilablehouselistAdapter = new AvilablehouselistAdapter(activity, AvilableArrayListtemp);
            mypost.setAdapter(avilablehouselistAdapter);
        }
    }
}