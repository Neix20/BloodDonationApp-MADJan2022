//package my.edu.utar.blooddonationmadnew.sample;
//
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import my.edu.utar.blooddonationmadnew.R;
//import my.edu.utar.blooddonationmadnew.admin.data.User;
//
//public class UserViewAdapter extends FirebaseRecyclerAdapter<User, UserViewAdapter.UserViewHolder> {
//
//    private DatabaseReference dbRef;
//    private final String TABLE_NAME = "users";
//
//    public final static String TAG = UserViewAdapter.class.getSimpleName();
//
//    public UserViewAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
//        super(options);
//        dbRef = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
//    }
//
//    @NonNull
//    @Override
//    public UserViewAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int pos){
//        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        return new UserViewHolder(mItemView, this);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull UserViewHolder viewHolder, int position, @NonNull User elem) {
//        // Bind User Element to List Item
//        viewHolder.id_txtView.setText(elem.getId());
//        viewHolder.email_txtView.setText(elem.getEmail());
//        viewHolder.pwd_txtView.setText(elem.getPassword());
//        viewHolder.type_txtView.setText(elem.getType());
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
//        TextView id_txtView;
//        TextView email_txtView;
//        TextView pwd_txtView;
//        TextView type_txtView;
//
//        UserViewAdapter mAdapter;
//
//
//        public UserViewHolder(View itemView, UserViewAdapter mAdapter) {
//            super(itemView);
//            id_txtView = itemView.findViewById(R.id.id_txtView);
//            email_txtView = itemView.findViewById(R.id.name_txtView);
//            pwd_txtView = itemView.findViewById(R.id.pwd_txtView);
//            type_txtView = itemView.findViewById(R.id.phoneNo_txtView);
//
//            this.mAdapter = mAdapter;
//
//            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            // Get the position of the item that was clicked.
//            int pos = getLayoutPosition();
//
//            // Edit Item
//        }
//
//
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            int pos = getAdapterPosition();
//
//            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete"); // groupId, itemId, order, title
//        }
//    }
//}
