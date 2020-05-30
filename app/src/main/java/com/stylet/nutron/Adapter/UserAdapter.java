package com.stylet.nutron.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stylet.nutron.Model.Patient;
import com.stylet.nutron.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context context;
    List<Patient> patient_list;

    public UserAdapter(Context context, List<Patient> patient_list) {
        this.context = context;
        this.patient_list = patient_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Patient patient = patient_list.get(position);
        holder.setUserData(patient.getUserimage(), patient.getUsername());

    }

    @Override
    public int getItemCount() {
        return patient_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userimage;
        private TextView username, diagnosis;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.patient_image);
            username = itemView.findViewById(R.id.patient_username);
            diagnosis = itemView.findViewById(R.id.patient_diagnosis);

        }

        private void setUserData(String patient_image, String patient_username){
            username.setText(patient_username);
            Glide.with(context).load(patient_image).into(userimage);

        }
    }


}
