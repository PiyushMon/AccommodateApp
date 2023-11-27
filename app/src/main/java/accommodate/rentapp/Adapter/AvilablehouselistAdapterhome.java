package accommodate.rentapp.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;


public class AvilablehouselistAdapterhome extends RecyclerView.Adapter {

    Activity activity;
    ArrayList<MYPostModel> myPostModelArrayList;


    public interface OnItemClickListener {
        void onItemClick(MYPostModel item);

    }

    OnItemClickListener onItemClickListener;

    public AvilablehouselistAdapterhome(Activity activity, ArrayList<MYPostModel> myPostModelArrayList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.myPostModelArrayList = myPostModelArrayList;
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_layout_avilablehousehome, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        MYPostModel myPostModel = myPostModelArrayList.get(position);
        viewHolder.PropertyDetails.setText("" + myPostModel.getBhktype() + " " + myPostModel.getProperttype() + " For " + myPostModel.getTenanttype());
        viewHolder.Propertylocation.setText("" + myPostModel.getAddress() + "," + myPostModel.getCity() + "," + myPostModel.getProvince() + "," + myPostModel.getPincode());

        viewHolder.Price.setText(myPostModel.getPrice() + "  $ / Per Month");

        Glide.with(activity).load(myPostModel.getPhotolist().get(0)).error(R.drawable.ic_user).into(viewHolder.imagedata);




        viewHolder.cardbox.setOnClickListener(new View.OnClickListener() {
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

        TextView PropertyDetails, Price,Propertylocation;
        ImageView imagedata;
        CardView cardbox;

        public MyViewHolder(View view) {
            super(view);

            PropertyDetails = view.findViewById(R.id.PropertyDetails);
            Propertylocation = view.findViewById(R.id.Propertylocation);
            cardbox = view.findViewById(R.id.cardbox);
            Price = view.findViewById(R.id.Price);
            imagedata = view.findViewById(R.id.imagedata);
        }
    }
}
