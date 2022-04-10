package my.edu.utar.blooddonationmadnew.adapter;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import my.edu.utar.blooddonationmadnew.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.ui.AdminEditBloodEventActivity;
import my.edu.utar.blooddonationmadnew.ui.UserCheckBloodEventActivity;

public class UserBloodEventViewAdapter extends FirebaseRecyclerAdapter<BloodEvent, UserBloodEventViewAdapter.UserBloodEventViewHolder> {
    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    public final static String TAG = UserBloodEventViewAdapter.class.getSimpleName();

    public UserBloodEventViewAdapter(@NonNull FirebaseRecyclerOptions<BloodEvent> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserBloodEventViewHolder holder, int position, @NonNull BloodEvent model) {
        holder.id_txtView.setText(model.getId());
        holder.title_txtView.setText(model.getTitle());
        holder.address_txtView.setText(model.getAddress());
    }

    @NonNull
    @Override
    public UserBloodEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blood_event, parent, false);
        return new UserBloodEventViewAdapter.UserBloodEventViewHolder(mItemView, this);
    }

    class UserBloodEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView id_txtView;
        TextView title_txtView;
        TextView address_txtView;

        UserBloodEventViewAdapter mAdapter;

        public UserBloodEventViewHolder(@NonNull View itemView, UserBloodEventViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            id_txtView = itemView.findViewById(R.id.id_txtView);
            title_txtView = itemView.findViewById(R.id.title_txtView);
            address_txtView = itemView.findViewById(R.id.address_txtView);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            // Create Intent
            String id = id_txtView.getText().toString().trim();

            Intent intent = new Intent(view.getContext(), UserCheckBloodEventActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }
    }
}
