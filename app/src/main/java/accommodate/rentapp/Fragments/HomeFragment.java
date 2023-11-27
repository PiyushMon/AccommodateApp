package accommodate.rentapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import accommodate.rentapp.Activity.SerchActivity;
import accommodate.rentapp.Activity.ViewPropertyActivity;
import accommodate.rentapp.Adapter.AvilablehouselistAdapterhome;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    ViewPager2 bestinbugetlist;
    Activity activity;

    TextView undertext;

    AvilablehouselistAdapterhome myPostAdapterhome;

    private int currentPage = 0;
    ArrayList<MYPostModel> myPostModelArrayList;
    private Timer timer;

    LinearLayout rent, pghome;
    Button Serch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        bestinbugetlist = (ViewPager2) view.findViewById(R.id.bestinbugetlist);
        rent = (LinearLayout) view.findViewById(R.id.rent);
        pghome = (LinearLayout) view.findViewById(R.id.pghome);
        Serch = (Button) view.findViewById(R.id.Serch);


        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SerchActivity.class).putExtra(Datakey.PasskeyHome ,"Rent"));
            }
        });

        pghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SerchActivity.class).putExtra(Datakey.PasskeyHome ,"PG"));
            }
        });

        Serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SerchActivity.class).putExtra(Datakey.PasskeyHome ,"all"));
            }
        });


        undertext = view.findViewById(R.id.undertext);

        LoadbestList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void LoadbestList() {
        getDaTa();
        bestinbugetlist.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);


        myPostAdapterhome = new AvilablehouselistAdapterhome(activity, myPostModelArrayList, new AvilablehouselistAdapterhome.OnItemClickListener() {
            @Override
            public void onItemClick(MYPostModel item) {
                activity.startActivity(new Intent(activity, ViewPropertyActivity.class).putExtra(Datakey.MYPostModelKey, item));
            }
        });

        bestinbugetlist.setAdapter(myPostAdapterhome);
        bestinbugetlist.setClipToPadding(true);
        bestinbugetlist.setClipChildren(true);
        bestinbugetlist.setOffscreenPageLimit(myPostModelArrayList.size());
        bestinbugetlist.getChildAt(0).setOverScrollMode(0);


        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == myPostAdapterhome.getItemCount() - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                bestinbugetlist.setCurrentItem(currentPage, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2000, 3000); //


        bestinbugetlist.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
            }

            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


    }


    private void getDaTa() {
        ArrayList<MYPostModel> filteredItems = new ArrayList<MYPostModel>();


        List<String> prices = Arrays.asList(new String[]{"0-100", "101-200", "201-300", "301-400"});
        for (MYPostModel myPostModel : Globaldata.AvilableArrayList) {
            boolean priceMatched = true;
            if (prices.size() > 0 && !priceContains(prices, Integer.parseInt(myPostModel.getPrice()))) {
                priceMatched = false;
            }
            if (priceMatched) {
                filteredItems.add(myPostModel);
            }
        }

        if (filteredItems.size() < 5) {
            undertext.setText("Best In budget For You");
            myPostModelArrayList = Globaldata.AvilableArrayList;
        } else {
            myPostModelArrayList = filteredItems;
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