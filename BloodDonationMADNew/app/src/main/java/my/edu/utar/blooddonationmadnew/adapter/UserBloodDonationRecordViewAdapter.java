package my.edu.utar.blooddonationmadnew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.data.BloodDonationRecord;

public class UserBloodDonationRecordViewAdapter extends FirebaseRecyclerAdapter<BloodDonationRecord, UserBloodDonationRecordViewAdapter.BloodDonationRecordViewHolder> {

    public UserBloodDonationRecordViewAdapter(@NonNull FirebaseRecyclerOptions<BloodDonationRecord> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodDonationRecordViewHolder holder, int position, @NonNull BloodDonationRecord model) {
        holder.venue_txt.setText(model.getVenue_title());
    }

    @NonNull
    @Override
    public BloodDonationRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blood_donation_record, parent, false);
        return new BloodDonationRecordViewHolder(mItemView, this);
    }

    class BloodDonationRecordViewHolder extends RecyclerView.ViewHolder {
        TextView venue_txt;

        UserBloodDonationRecordViewAdapter mAdapter;

        public BloodDonationRecordViewHolder(@NonNull View itemView, UserBloodDonationRecordViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            venue_txt = itemView.findViewById(R.id.venue_txt);

            this.mAdapter = mAdapter;
        }
    }
}
