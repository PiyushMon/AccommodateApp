package accommodate.rentapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import accommodate.rentapp.Adapter.FilterAdapter;
import accommodate.rentapp.Model.Filter;
import accommodate.rentapp.Model.Preferences;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Globaldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterActivity extends AppCompatActivity {

    RecyclerView filterRV;
    RecyclerView filterValuesRV;
    FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Globaldata.toolbar(FilterActivity.this, "Filter Property");
        initControls();
    }

    private void initControls() {
        filterRV = findViewById(R.id.filterRV);
        filterValuesRV = findViewById(R.id.filterValuesRV);
        filterRV.setLayoutManager(new LinearLayoutManager(this));
        filterValuesRV.setLayoutManager(new LinearLayoutManager(this));

        List<String> INDEX_Tenanttype = Arrays.asList(new String[]{"PG", "Rent"});
        if (!Preferences.filters.containsKey(Filter.INDEX_Tenanttype)) {
            Preferences.filters.put(Filter.INDEX_Tenanttype, new Filter("Tenant Type", INDEX_Tenanttype, new ArrayList()));
        }
        List<String> Properttype = Arrays.asList(new String[]{"Residential Apartment", "Independent House/Villa", "Room", "Sharing room", "Basement", "20"});
        if (!Preferences.filters.containsKey(Filter.INDEX_Properttype)) {
            Preferences.filters.put(Filter.INDEX_Properttype, new Filter("Type Off Property", Properttype, new ArrayList()));
        }
        List<String> Bhktype = Arrays.asList(new String[]{"1 BHK", "2 BHK", "3 BHK", "4 BHK"});
        if (!Preferences.filters.containsKey(Filter.INDEX_Bhktype)) {
            Preferences.filters.put(Filter.INDEX_Bhktype, new Filter("BHK", Bhktype, new ArrayList()));
        }


        List<String> prices = Arrays.asList(new String[]{"0-100", "101-200", "201-300", "301-400", "401-500"});
        if (!Preferences.filters.containsKey(Filter.INDEX_Price)) {
            Preferences.filters.put(Filter.INDEX_Price, new Filter("Price", prices, new ArrayList()));
        }

        filterAdapter = new FilterAdapter(getApplicationContext(), Preferences.filters, filterValuesRV);
        filterRV.setAdapter(filterAdapter);

        Button clearB = findViewById(R.id.clearB);
        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.filters.get(Filter.INDEX_Tenanttype).setSelected(new ArrayList());
                Preferences.filters.get(Filter.INDEX_Properttype).setSelected(new ArrayList());
                Preferences.filters.get(Filter.INDEX_Bhktype).setSelected(new ArrayList());
                Preferences.filters.get(Filter.INDEX_Price).setSelected(new ArrayList());
                finish();
            }
        });

        Button applyB = findViewById(R.id.applyB);
        applyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
