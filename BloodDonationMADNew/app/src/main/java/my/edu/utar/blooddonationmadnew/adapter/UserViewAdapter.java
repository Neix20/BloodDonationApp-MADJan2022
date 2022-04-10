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
import my.edu.utar.blooddonationmadnew.ui.AdminCheckUserProfileActivity;

public class UserViewAdapter extends FirebaseRecyclerAdapter<User, UserViewAdapter.UserViewHolder> {

    private DatabaseReference dbRef;
    private final String TABLE_NAME = "users";

    public final static String TAG = AdminBloodEventViewAdapter.class.getSimpleName();

    public UserViewAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.name_txtView.setText(model.getName());
        holder.bloodType_TxtView.setText(model.getBloodType());
        holder.phoneNo_TxtView.setText(model.getPhoneNumber());
    }


    @NonNull
    @Override
    public UserViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_list_item, parent, false);
        return new UserViewAdapter.UserViewHolder(mItemView, this);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        TextView name_txtView;
        TextView bloodType_TxtView;
        TextView phoneNo_TxtView;

        UserViewAdapter mAdapter;

        public UserViewHolder(@NonNull View itemView, UserViewAdapter mAdapter) {
            super(itemView);

            // Bind Java Object to XML Element
            name_txtView = itemView.findViewById(R.id.name_txtView);
            bloodType_TxtView = itemView.findViewById(R.id.bloodType_edit_txtView);
            phoneNo_TxtView=itemView.findViewById(R.id.phoneNo_edit_txtView);

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
                            //Todo
                            User tmpUser = userSnapshot.getValue(User.class);
                            Intent intent = new Intent(view.getContext(), AdminCheckUserProfileActivity.class);
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

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int pos = getAdapterPosition();

            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete");
        }

    }
}
