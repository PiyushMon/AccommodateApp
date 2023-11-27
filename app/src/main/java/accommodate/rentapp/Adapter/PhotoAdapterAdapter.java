package accommodate.rentapp.Adapter;

import static android.content.ContentValues.TAG;
import static accommodate.rentapp.Activity.CreateNewPostActivity.templistforsavdatafinal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import accommodate.rentapp.R;
import accommodate.rentapp.Utils.Globaldata;

public class PhotoAdapterAdapter extends RecyclerView.Adapter<PhotoAdapterAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> templistforsavdata;


    public PhotoAdapterAdapter(Activity activity, ArrayList<String> templistforsavdata) {
        this.templistforsavdata = templistforsavdata;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.photo_item, parent, false);
        return new PhotoAdapterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(activity).load(templistforsavdata.get(position)).into(holder.profile_image);


        holder.deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Removephoto(templistforsavdata.get(position), position);
            }
        });


    }

    private void Removephoto(String templistforsavdata22, int position) {
        Globaldata.progressDialogShow(activity);
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(templistforsavdata22);
        photoRef.delete().addOnSuccessListener(aVoid -> {
            // File deleted successfully
            Globaldata.progressDialogDismiss();
            templistforsavdata.remove(position);
            templistforsavdatafinal =templistforsavdata;
            PhotoAdapterAdapter.this.notifyDataSetChanged();
            Log.d(TAG, "File deleted successfully");
        }).addOnFailureListener(exception -> {
            Globaldata.progressDialogDismiss();
            // An error occurred while deleting the file
            Log.e(TAG, "Error deleting file", exception);
        });
    }

    @Override
    public int getItemCount() {
        return templistforsavdata.size();
    }

    //check for last message


    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView profile_image;
        ImageView deletephoto;


        public ViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            deletephoto = itemView.findViewById(R.id.deletephoto);


        }
    }
}
