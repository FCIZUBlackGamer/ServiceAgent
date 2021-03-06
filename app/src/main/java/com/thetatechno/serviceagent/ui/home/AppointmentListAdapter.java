package com.thetatechno.serviceagent.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thetatechno.serviceagent.R;
import com.thetatechno.serviceagent.model.pojo.Appointement;
import com.thetatechno.serviceagent.ui.activities.appointmentdetails.AppointmentDetailsActivity;
import com.thetatechno.serviceagent.ui.activities.main.MainAgentActivity;
import com.thetatechno.serviceagent.ui.listeners.OnCallButtonClickedListener;
import com.thetatechno.serviceagent.utils.Constants;
import com.thetatechno.serviceagent.utils.PreferenceController;
import com.thetatechno.serviceagent.utils.StringUtil;


import java.util.List;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.myViewHolder> {
    Context mContext;
    List<Appointement> myItems;
    private final String APPOINTMENT = "appointment";

    public AppointmentListAdapter(Context context) {
        mContext = context;
    }

    public void setItems(List<Appointement> items) {
        myItems = items;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.customer_list_item, parent, false);
        return new myViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Appointement appointement = myItems.get(position);
        holder.mRNTxt.setText(appointement.getMRN() != null ? appointement.getMRN() : "");
        String languageName = PreferenceController.getInstance(mContext).get(PreferenceController.LANGUAGE);
        if (languageName.contains("العربية") || languageName.contains(Constants.ARABIC)) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        } else  {
            holder.patientNameTxt.setText(StringUtil.toCamelCase(appointement.getEnglishName()));
        }

        if (appointement.getImagePath() != null && !appointement.getImagePath().isEmpty() && appointement.getSexCode() != null && appointement.getSexCode().contains("F"))
            Glide.with(mContext)
                    .load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + appointement.getImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.ic_girl)
                    .into(holder.patientAvtarImage);
        else if (appointement.getImagePath() != null && !appointement.getImagePath().isEmpty() && appointement.getSexCode() != null && appointement.getSexCode().contains("M")) {
            Glide.with(mContext)
                    .load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + appointement.getImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.man)
                    .into(holder.patientAvtarImage);
        } else {
            if (appointement.getSexCode() != null && appointement.getSexCode().contains(Constants.FEMALE)) {

                holder.patientAvtarImage.setImageResource(R.drawable.ic_girl);
            } else {
                holder.patientAvtarImage.setImageResource(R.drawable.man);
            }
        }

        if (appointement.getArrivalTime().isEmpty() && appointement.getCallingTime().isEmpty() && appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_chair);
        }
        if (!appointement.getArrivalTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_arrive);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        }
        if (!appointement.getCallingTime().isEmpty() && appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_call);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        } else if (!appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_start);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), AppointmentDetailsActivity.class);
                intent.putExtra(APPOINTMENT, (Parcelable) appointement);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView mRNTxt;
        TextView patientNameTxt;
        ImageView patientStateImage;
        ImageView patientAvtarImage;
        TextView scheduledTimeTxt;
        ImageView scheduledIcon;

        public myViewHolder(View itemView) {
            super(itemView);
            mRNTxt = itemView.findViewById(R.id.customer_id_txt);
            patientNameTxt = itemView.findViewById(R.id.customer_name_text);
            patientStateImage = itemView.findViewById(R.id.customer_state_img);
            patientAvtarImage = itemView.findViewById(R.id.customerAvatar);
            scheduledTimeTxt = itemView.findViewById(R.id.txt_scheduled_time_in_card);
            scheduledIcon = itemView.findViewById(R.id.imageView);
        }

    }

}
