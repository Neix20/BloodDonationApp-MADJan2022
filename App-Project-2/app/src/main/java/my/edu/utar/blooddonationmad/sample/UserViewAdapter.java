package my.edu.utar.blooddonationmad.sample;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.blooddonationmad.R;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.UserViewHolder>{

    public final static String TAG = UserViewAdapter.class.toString();

    private List<User> mUserList;
    private LayoutInflater mInflater;

    public UserViewAdapter(Context mContext){
        this.mUserList = new ArrayList<User>();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public UserViewAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int pos){
        View mItemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new UserViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(UserViewAdapter.UserViewHolder viewHolder, int pos){
        User elem = mUserList.get(pos);

        // Bind User Element to List Item
        viewHolder.id_txtView.setText(elem.getId());
        viewHolder.email_txtView.setText(elem.getEmail());
        viewHolder.pwd_txtView.setText(elem.getPassword());
        viewHolder.type_txtView.setText(elem.getType());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void updateList(List<User> userList) {
        UserDiffCallback diffCallback = new UserDiffCallback(this.mUserList, userList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mUserList.clear();
        this.mUserList.addAll(userList);

        diffResult.dispatchUpdatesTo(this);
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

            User elem = mUserList.get(pos);

            Toast.makeText(view.getContext(), elem.toString(), Toast.LENGTH_LONG).show();

            // Spawn Edit page Activity
//            Intent intent = new Intent(view.getContext(), EditPwdActivity.class);
//            intent.putExtra("id", pwd.getId());
//            view.getContext().startActivity(intent);
        }


        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int pos = getAdapterPosition();
            User elem = mUserList.get(pos);
            MenuItem delete = contextMenu.add(Menu.NONE, 1, pos, "Delete"); // groupId, itemId, order, title
        }
    }
}
