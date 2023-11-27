package accommodate.rentapp.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import accommodate.rentapp.Model.MYPostModel;
import accommodate.rentapp.R;


public class MyPostAdapterhome extends RecyclerView.Adapter {

    Activity activity;
    ArrayList<MYPostModel> myPostModelArrayList;



    public interface OnItemClickListenerhome {
        void onItemClick(MYPostModel item);

    }

    OnItemClickListenerhome onItemClickListenerhome;

    public MyPostAdapterhome(Activity activity, ArrayList<MYPostModel> myPostModelArrayList, OnItemClickListenerhome onItemClickListener) {
        this.activity = activity;
        this.myPostModelArrayList = myPostModelArrayList;
        this.onItemClickListenerhome = onItemClickListener;

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
        viewHolder.Price.setText(myPostModel.getPrice() + "  $ / Per Month");


        Glide.with(activity)
                .load(myPostModelArrayList.get(position).getPhotolist().get(0))
                .centerCrop()
                .into(viewHolder.imagedata);


        viewHolder.update22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenerhome.onItemClick(myPostModel);
            }
        });

    }


    @Override
    public int getItemCount() {
        return myPostModelArrayList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView PropertyDetails, Propertylocation, Price;

        ImageView imagedata;

        Button update22;

        public MyViewHolder(View view) {
            super(view);


            PropertyDetails = view.findViewById(R.id.PropertyDetails);
            imagedata = view.findViewById(R.id.imagedata);
            Price = view.findViewById(R.id.Price);
            update22 = view.findViewById(R.id.update);
        }
    }
}
