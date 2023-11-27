package accommodate.rentapp.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import accommodate.rentapp.Activity.ViewPropertyActivity;
import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Datakey;


public class AvilablehouselistAdapter extends RecyclerView.Adapter {

    Activity activity;
    static ArrayList<MYPostModel> myPostModelArrayList;


    public AvilablehouselistAdapter(Activity activity, ArrayList<MYPostModel> myPostModelArrayList) {
        this.activity = activity;
        this.myPostModelArrayList = myPostModelArrayList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_layout_avilablehouse, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        MYPostModel myPostModel = myPostModelArrayList.get(position);
        viewHolder.PropertyDetails.setText("" + myPostModel.getBhktype() + " " + myPostModel.getProperttype() + " For " + myPostModel.getTenanttype());
        viewHolder.Propertylocation.setText("" + myPostModel.getAddress() + "," + myPostModel.getCity() + "," + myPostModel.getProvince() + "," + myPostModel.getPincode());
        viewHolder.Price.setText(myPostModel.getPrice() + "  $ / Per Month");


        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ViewPropertyActivity.class).putExtra(Datakey.MYPostModelKey, myPostModel));
            }
        });

    }






    @Override
    public int getItemCount() {
        return myPostModelArrayList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView PropertyDetails, Propertylocation, Price;
        Button update;

        public MyViewHolder(View view) {
            super(view);
            PropertyDetails = view.findViewById(R.id.PropertyDetails);
            Propertylocation = view.findViewById(R.id.Propertylocation);
            Price = view.findViewById(R.id.Price);
            update = view.findViewById(R.id.update);
        }
    }
}
