package accommodate.rentapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import accommodate.rentapp.Activity.CreateNewPostActivity;
import accommodate.rentapp.Adapter.MyPostAdapter;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;
import accommodate.rentapp.Utils.Globaldata;


public class MyPostFragment extends Fragment {

    Button addpost;
    Activity activity;
    RecyclerView mypost;
    TextView Propertycount;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public MyPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);
        activity = getActivity();
        addpost = view.findViewById(R.id.addpost);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, CreateNewPostActivity.class).putExtra("edit", 0));
            }
        });

        mypost = view.findViewById(R.id.mypost);
        Propertycount = view.findViewById(R.id.Propertycount);
        mSwipeRefreshLayout = view.findViewById(R.id.mSwipeRefreshLayout);

        LoadData();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }


    private void LoadData() {
        if (!Globaldata.myPostModelArrayList.isEmpty()) {
            Propertycount.setText(Globaldata.myPostModelArrayList.size() + "");
            mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            MyPostAdapter myPostAdapter = new MyPostAdapter(activity, Globaldata.myPostModelArrayList, new MyPostAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MYPostModel item) {
                    activity.startActivity(new Intent(activity, CreateNewPostActivity.class).putExtra(Datakey.MYPostModelKey, item).putExtra("edit", 1));

                }
            });
            mypost.setAdapter(myPostAdapter);
        } else {
            Globaldata.getOwnerPropertylistData(activity, new Globaldata.DataLoadListener() {
                @Override
                public void onLoad() {
                    Propertycount.setText(Globaldata.myPostModelArrayList.size() + "");
                    mypost.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    MyPostAdapter myPostAdapter = new MyPostAdapter(activity, Globaldata.myPostModelArrayList, new MyPostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MYPostModel item) {
                            activity.startActivity(new Intent(activity, CreateNewPostActivity.class).putExtra(Datakey.MYPostModelKey, item).putExtra("edit", 1));
                        }
                    });
                    mypost.setAdapter(myPostAdapter);
                }

                @Override
                public void onLoadFail() {
                    Globaldata.progressDialogDismiss();
                    Propertycount.setText("0");
                }
            });

        }
    }
}