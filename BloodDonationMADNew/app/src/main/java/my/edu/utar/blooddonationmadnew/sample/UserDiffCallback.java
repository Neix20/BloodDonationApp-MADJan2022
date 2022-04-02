package my.edu.utar.blooddonationmadnew.sample;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class UserDiffCallback extends DiffUtil.Callback{

    private List<User> oldList;
    private List<User> newList;

    public UserDiffCallback(List<User> oldList, List<User> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return this.oldList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldElem = oldList.get(oldItemPosition);
        User newElem = newList.get(newItemPosition);
        return oldElem.equals(newElem);
    }
}
