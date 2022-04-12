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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.utar.blooddonationmadnew.R;

import my.edu.utar.blooddonationmadnew.data.User;
import my.edu.utar.blooddonationmadnew.ui.UserCheckUserProfileActivity;

public class UserUserViewAdapter extends FirebaseRecyclerAdapter<User, UserUserViewAdapter.UserViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    public final static String TAG = UserBloodEventViewAdapter.class.getSimpleName();

    public UserUserViewAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.icon_item_txt.setText(Character.toUpperCase(model.getName().charAt(0)) + "");
        holder.id_txt.setText(model.getId());
        holder.name_txt.setText(model.getName());
        holder.bloodType_txt.setText(model.getBloodType());
        holder.phoneNo_txt.setText(model.getPhoneNumber());
    }


    @NonNull
    @Override
    public UserUserViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new UserUserViewAdapter.UserViewHolder(mItemView, this);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView icon_item_txt;
        TextView id_txt;
        TextView name_txt;
        TextView bloodType_txt;
        TextView phoneNo_txt;

        UserUserViewAdapter mAdapter;

        public UserViewHolder(@NonNull View itemView, UserUserViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            icon_item_txt = itemView.findViewById(R.id.icon_item_txt);
            id_txt = itemView.findViewById(R.id.id_txt);
            name_txt = itemView.findViewById(R.id.name_txt);
            bloodType_txt = itemView.findViewById(R.id.bloodType_txt);
            phoneNo_txt=itemView.findViewById(R.id.phoneNo_txt);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            String id = id_txt.getText().toString().trim();

            // Edit Item
            Intent intent = new Intent(view.getContext(), UserCheckUserProfileActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }

    }
}
