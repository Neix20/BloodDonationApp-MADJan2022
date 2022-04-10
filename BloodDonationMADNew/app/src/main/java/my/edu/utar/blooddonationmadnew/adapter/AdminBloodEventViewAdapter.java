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

public class AdminBloodEventViewAdapter extends FirebaseRecyclerAdapter<BloodEvent, AdminBloodEventViewAdapter.AdminBloodEventViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "BloodEvents";

    public final static String TAG = AdminBloodEventViewAdapter.class.getSimpleName();

    public AdminBloodEventViewAdapter(@NonNull FirebaseRecyclerOptions<BloodEvent> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdminBloodEventViewHolder holder, int position, @NonNull BloodEvent model) {
        holder.id_txtView.setText(model.getId());
        holder.title_txtView.setText(model.getTitle());
        holder.address_txtView.setText(model.getAddress());
    }

    @NonNull
    @Override
    public AdminBloodEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blood_event, parent, false);
        return new AdminBloodEventViewHolder(mItemView, this);
    }


    class AdminBloodEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        TextView id_txtView;
        TextView title_txtView;
        TextView address_txtView;

        AdminBloodEventViewAdapter mAdapter;

        public AdminBloodEventViewHolder(@NonNull View itemView, AdminBloodEventViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            id_txtView = itemView.findViewById(R.id.id_txtView);
            title_txtView = itemView.findViewById(R.id.title_txtView);
            address_txtView = itemView.findViewById(R.id.address_txtView);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View view) {
            // Create Intent
            String id = id_txtView.getText().toString().trim();

            Intent intent = new Intent(view.getContext(), AdminEditBloodEventActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Long Press Create Context Menu
            int pos = getAdapterPosition();

            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete");
        }
    }
}
