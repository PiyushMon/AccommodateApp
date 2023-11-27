package accommodate.rentapp.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;


public class MyPostAdapter extends RecyclerView.Adapter {

    Activity activity;
    ArrayList<MYPostModel> myPostModelArrayList;

    public interface OnItemClickListener {
        void onItemClick(MYPostModel item);

    }

    OnItemClickListener onItemClickListener;

    public MyPostAdapter(Activity activity, ArrayList<MYPostModel> myPostModelArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.myPostModelArrayList = myPostModelArrayList;
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(activity).inflate(R.layout.item_layout_mypost, parent, false);
        return new MyViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        MYPostModel myPostModel = myPostModelArrayList.get(position);
        viewHolder.PropertyDetails.setText("" + myPostModel.getBhktype() + " " + myPostModel.getProperttype() + " For " + myPostModel.getTenanttype());
        viewHolder.Propertylocation.setText("" + myPostModel.getAddress() + "," + myPostModel.getCity() + "," + myPostModel.getProvince() + "," + myPostModel.getPincode());
        viewHolder.Price.setText(myPostModel.getPrice() + "  $ / Per Month");


        if (myPostModel.getAvailability().equalsIgnoreCase("ON")) {
            viewHolder.avalablelabale.setText("Available For Rent");
            viewHolder.avalablelabale.setTextColor(Color.parseColor("#4C834F"));
        } else {
            viewHolder.avalablelabale.setText("Already Rented");
            viewHolder.avalablelabale.setTextColor(Color.parseColor("#D30E00"));
        }


        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(myPostModel);
            }
        });

    }


    @Override
    public int getItemCount() {
        return myPostModelArrayList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView PropertyDetails, Propertylocation, Price, avalablelabale;

        Button update;

        public MyViewHolder(View view) {
            super(view);

            PropertyDetails = view.findViewById(R.id.PropertyDetails);
            Propertylocation = view.findViewById(R.id.Propertylocation);
            Price = view.findViewById(R.id.Price);
            avalablelabale = view.findViewById(R.id.avalablelabale);
            update = view.findViewById(R.id.update);
        }
    }
}
