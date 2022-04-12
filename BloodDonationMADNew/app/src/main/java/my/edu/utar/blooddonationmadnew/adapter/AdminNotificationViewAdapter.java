package my.edu.utar.blooddonationmadnew.adapter;

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
import my.edu.utar.blooddonationmadnew.data.Notification;

public class AdminNotificationViewAdapter extends FirebaseRecyclerAdapter<Notification, AdminNotificationViewAdapter.NotificationViewHolder> {
    private final String TABLE_NAME = "Notification";
    private DatabaseReference dbRef;

    public AdminNotificationViewAdapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
        holder.title_item_txtView.setText(model.getTitle());
        holder.body_item_txtView.setText(model.getBody());
        holder.noti_id = model.getId();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationViewHolder(mItemView, this);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView title_item_txtView;
        TextView body_item_txtView;
        String noti_id;

        AdminNotificationViewAdapter mAdapter;

        public NotificationViewHolder(@NonNull View itemView, AdminNotificationViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            title_item_txtView = itemView.findViewById(R.id.title_item_txtView);
            body_item_txtView = itemView.findViewById(R.id.body_item_txtView);

            this.mAdapter = mAdapter;

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Long Press Create Context Menu
            int pos = getAdapterPosition();

            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete");
        }
    }
}
