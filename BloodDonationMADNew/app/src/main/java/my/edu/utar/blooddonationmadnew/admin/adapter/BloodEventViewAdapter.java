package my.edu.utar.blooddonationmadnew.admin.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.R;
import my.edu.utar.blooddonationmadnew.admin.data.BloodEvent;
import my.edu.utar.blooddonationmadnew.sample.TestEditActivity;
import my.edu.utar.blooddonationmadnew.sample.User;
import my.edu.utar.blooddonationmadnew.sample.UserViewAdapter;

public class BloodEventViewAdapter extends FirebaseRecyclerAdapter<BloodEvent, BloodEventViewAdapter.BloodEventViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    public final static String TAG = BloodEventViewAdapter.class.getSimpleName();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BloodEventViewAdapter(@NonNull FirebaseRecyclerOptions<BloodEvent> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }


    @Override
    protected void onBindViewHolder(@NonNull BloodEventViewHolder holder, int position, @NonNull BloodEvent model) {
        holder.title_item_txtView.setText(model.getTitle());
        holder.address_item_txtView.setText(model.getAddress());
    }

    @NonNull
    @Override
    public BloodEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_event_list_item, parent, false);
        return new BloodEventViewHolder(mItemView, this);
    }


    class BloodEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        TextView title_item_txtView;
        TextView address_item_txtView;

        BloodEventViewAdapter mAdapter;

        public BloodEventViewHolder(@NonNull View itemView, BloodEventViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            title_item_txtView = itemView.findViewById(R.id.title_item_txtView);
            address_item_txtView = itemView.findViewById(R.id.address_item_txtView);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View view) {
            // Create Intent


        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Long Press Create Context Menu
        }
    }
}
