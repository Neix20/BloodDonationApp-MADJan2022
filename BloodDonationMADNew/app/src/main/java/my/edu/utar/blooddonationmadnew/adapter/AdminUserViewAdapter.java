package my.edu.utar.blooddonationmadnew.adapter;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.utar.blooddonationmadnew.R;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.ui.AdminEditUserActivity;

public class AdminUserViewAdapter extends FirebaseRecyclerAdapter<User, AdminUserViewAdapter.UserViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    public final static String TAG = AdminBloodEventViewAdapter.class.getSimpleName();

    public AdminUserViewAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        Log.e(TAG, Character.toUpperCase(model.getName().charAt(0)) + "");
        holder.id_txt.setText(model.getId());
        holder.name_txt.setText(model.getName());
        holder.bloodType_txt.setText(model.getBloodType());
        holder.phoneNo_txt.setText(model.getPhoneNumber());
    }


    @NonNull
    @Override
    public AdminUserViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new AdminUserViewAdapter.UserViewHolder(mItemView, this);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        TextView icon_item_txt;
        TextView id_txt;
        TextView name_txt;
        TextView bloodType_txt;
        TextView phoneNo_txt;

        AdminUserViewAdapter mAdapter;

        public UserViewHolder(@NonNull View itemView, AdminUserViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            icon_item_txt = itemView.findViewById(R.id.icon_item_txt);
            id_txt = itemView.findViewById(R.id.id_txt);
            name_txt = itemView.findViewById(R.id.name_txt);
            bloodType_txt = itemView.findViewById(R.id.bloodType_txt);
            phoneNo_txt=itemView.findViewById(R.id.phoneNo_txt);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            String id = id_txt.getText().toString().trim();

            // Edit Item
            Intent intent = new Intent(view.getContext(), AdminEditUserActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int pos = getAdapterPosition();

            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete");
        }

    }
}
