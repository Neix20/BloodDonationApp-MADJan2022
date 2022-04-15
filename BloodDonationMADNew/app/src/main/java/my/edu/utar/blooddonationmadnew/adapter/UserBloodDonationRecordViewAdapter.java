package my.edu.utar.blooddonationmadnew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.joda.time.DateTime;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodDonationRecord;
import my.edu.utar.blooddonationmadnew.util.Util;

public class UserBloodDonationRecordViewAdapter extends FirebaseRecyclerAdapter<BloodDonationRecord, UserBloodDonationRecordViewAdapter.BloodDonationRecordViewHolder> {

    public UserBloodDonationRecordViewAdapter(@NonNull FirebaseRecyclerOptions<BloodDonationRecord> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodDonationRecordViewHolder holder, int position, @NonNull BloodDonationRecord model) {
        holder.venue_txt.setText(model.getVenue_title());

        DateTime dt = model.getDateTime();

        holder.date_txt.setText(String.format("Date: %s", Util.getDate(dt)));
        holder.time_txt.setText(String.format("Time: %s", Util.getTime(dt)));
    }

    @NonNull
    @Override
    public BloodDonationRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blood_donation_record, parent, false);
        return new BloodDonationRecordViewHolder(mItemView, this);
    }

    class BloodDonationRecordViewHolder extends RecyclerView.ViewHolder {
        TextView venue_txt;
        TextView date_txt;
        TextView time_txt;

        UserBloodDonationRecordViewAdapter mAdapter;

        public BloodDonationRecordViewHolder(@NonNull View itemView, UserBloodDonationRecordViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            venue_txt = itemView.findViewById(R.id.venue_txt);
            date_txt = itemView.findViewById(R.id.date_txt);
            time_txt = itemView.findViewById(R.id.time_txt);

            this.mAdapter = mAdapter;
        }
    }
}
