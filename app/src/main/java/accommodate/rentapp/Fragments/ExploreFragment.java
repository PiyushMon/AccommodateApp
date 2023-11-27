package accommodate.rentapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import accommodate.rentapp.Activity.FilterActivity;
import accommodate.rentapp.Adapter.AvilablehouselistAdapter;
import accommodate.rentapp.Model.Filter;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.Model.Preferences;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Globaldata;


public class ExploreFragment extends Fragment {


    SwipeRefreshLayout mSwipeRefreshLayout;

    Activity activity;
    RecyclerView mypost;

    Button filterB;
    Button sortB;
    boolean shortby = false;

    ArrayList<MYPostModel> AvilableArrayListtemp = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        activity = getActivity();
        mSwipeRefreshLayout = view.findViewById(R.id.mSwipeRefreshLayout);

        filterB = view.findViewById(R.id.filterB);
        sortB = view.findViewById(R.id.sortB);
        mypost = view.findViewById(R.id.mypost);
        mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));


        LoadData();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        filterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, FilterActivity.class));
            }
        });
        sortB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!shortby) {
                    ArrayList<MYPostModel> AvilableArrayListtemp22 = AvilableArrayListtemp;
                    MYPostModel[] itemsArr3 = new MYPostModel[AvilableArrayListtemp22.size()];
                    itemsArr3 = AvilableArrayListtemp22.toArray(itemsArr3);
                    Arrays.sort(itemsArr3, MYPostModel.priceComparator);
                    AvilableArrayListtemp22 = new ArrayList<>(Arrays.asList(itemsArr3));
                    AvilablehouselistAdapter avilablehouselistAdapter = new AvilablehouselistAdapter(activity, AvilableArrayListtemp22);
                    mypost.setAdapter(avilablehouselistAdapter);
                    shortby= true;

                    sortB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up, 0, 0, 0);
                } else {
                    AvilablehouselistAdapter avilablehouselistAdapter = new AvilablehouselistAdapter(activity, AvilableArrayListtemp);
                    mypost.setAdapter(avilablehouselistAdapter);
                    shortby= false;
                    sortB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_down, 0, 0, 0);
                }


            }
        });


        return view;


    }


    private void LoadData() {


        if (!Preferences.filters.isEmpty()) {
            ArrayList<MYPostModel> filteredItems = new ArrayList<MYPostModel>();
            List<String> Tenanttype = Preferences.filters.get(Filter.INDEX_Tenanttype).getSelected();
            List<String> Properttype = Preferences.filters.get(Filter.INDEX_Properttype).getSelected();
            List<String> Bhktype = Preferences.filters.get(Filter.INDEX_Bhktype).getSelected();
            List<String> prices = Preferences.filters.get(Filter.INDEX_Price).getSelected();
            for (MYPostModel myPostModel : Globaldata.AvilableArrayList) {
                boolean colorMatched = true;
                if (Tenanttype.size() > 0 && !Tenanttype.contains(myPostModel.getTenanttype())) {
                    colorMatched = false;
                }
                boolean ProperttypeMatched = true;
                if (Properttype.size() > 0 && !Properttype.contains(myPostModel.getProperttype().toString())) {
                    ProperttypeMatched = false;
                }
                boolean BhktypeMatched = true;
                if (Bhktype.size() > 0 && !Bhktype.contains(myPostModel.getBhktype().toString())) {
                    BhktypeMatched = false;
                }
                boolean priceMatched = true;
                if (prices.size() > 0 && !priceContains(prices, Integer.parseInt(myPostModel.getPrice()))) {
                    priceMatched = false;
                }
                if (colorMatched && ProperttypeMatched && BhktypeMatched && priceMatched) {
                    filteredItems.add(myPostModel);
                }
            }
            AvilableArrayListtemp = filteredItems;
        } else {
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

        }

        if (!AvilableArrayListtemp.isEmpty()) {
            mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            AvilablehouselistAdapter avilablehouselistAdapter = new AvilablehouselistAdapter(activity, AvilableArrayListtemp);
            mypost.setAdapter(avilablehouselistAdapter);
        }
    }

    private boolean priceContains(List<String> prices, int price) {
        boolean flag = false;
        for (String p : prices) {
            String tmpPrices[] = p.split("-");
            if (price >= Double.valueOf(tmpPrices[0]) && price <= Double.valueOf(tmpPrices[1])) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}