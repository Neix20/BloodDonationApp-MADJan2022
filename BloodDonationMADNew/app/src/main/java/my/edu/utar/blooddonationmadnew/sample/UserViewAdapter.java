package my.edu.utar.blooddonationmadnew.sample;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.blooddonationmadnew.R;

public class UserViewAdapter extends FirebaseRecyclerAdapter<User, UserViewAdapter.UserViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    public final static String TAG = UserViewAdapter.class.getSimpleName();

    public UserViewAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @NonNull
    @Override
    public UserViewAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int pos){
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new UserViewHolder(mItemView, this);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder viewHolder, int position, @NonNull User elem) {
        // Bind User Element to List Item
        viewHolder.id_txtView.setText(elem.getId());
        viewHolder.email_txtView.setText(elem.getEmail());
        viewHolder.pwd_txtView.setText(elem.getPassword());
        viewHolder.type_txtView.setText(elem.getType());
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        TextView id_txtView;
        TextView email_txtView;
        TextView pwd_txtView;
        TextView type_txtView;

        UserViewAdapter mAdapter;

        public UserViewHolder(View itemView, UserViewAdapter mAdapter) {
            super(itemView);
            id_txtView = itemView.findViewById(R.id.id_txtView);
            email_txtView = itemView.findViewById(R.id.email_txtView);
            pwd_txtView = itemView.findViewById(R.id.pwd_txtView);
            type_txtView = itemView.findViewById(R.id.type_txtView);

            this.mAdapter = mAdapter;

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int pos = getLayoutPosition();

            // Edit Item
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int ind = 0;

                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        if(ind == pos){
                            User tmpUser = userSnapshot.getValue(User.class);
                            Intent intent = new Intent(view.getContext(), TestEditActivity.class);
                            intent.putExtra("id", tmpUser.getId());
                            view.getContext().startActivity(intent);
                        }
                        ind++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "The read failed: " + error.getCode());
                }
            });
        }


        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int pos = getAdapterPosition();

            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete"); // groupId, itemId, order, title
        }
    }
}
