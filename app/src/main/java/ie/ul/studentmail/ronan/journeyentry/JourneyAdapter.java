package ie.ul.studentmail.ronan.journeyentry;

/*import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.MyViewHolder> {
    private List<Journey> journeyList;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public JourneyAdapter(List<Journey> myDataset) {
        journeyList = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public JourneyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.textView.setText(journeyList.get(position).toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

            return journeyList.size();
    }
}*/


import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JourneyAdapter extends RecyclerView.Adapter implements JourneyViewHolder.OnItemSelectedListener {

    private final List<SelectableJourney> mValues;
    private boolean isMultiSelectionEnabled = false;
    JourneyViewHolder.OnItemSelectedListener listener;


    public JourneyAdapter(JourneyViewHolder.OnItemSelectedListener listener,
                             List<Journey> journeys, boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (Journey journey: journeys) {
            mValues.add(new SelectableJourney(journey, false));
        }
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checked_item, parent, false);

        return new JourneyViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        String name;
        JourneyViewHolder holder = (JourneyViewHolder) viewHolder;
        SelectableJourney selectableItem = mValues.get(position);
        String[] startTimeDeets = selectableItem.getJourneyStart().split(" ");
        String[] endTimeDeets = selectableItem.getJourneyEnd().split(" ");
        name=selectableItem.getName()+"\n";
        if(startTimeDeets[0].equals(endTimeDeets[0]))
            name += "Date: " + startTimeDeets[0];
        else
            name += "Dates: " + startTimeDeets[0] + " to " + endTimeDeets[0];
        name += "\n" + startTimeDeets[1] + " - " + endTimeDeets[1];
        holder.textView.setText(name);
        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mJourney = selectableItem;
        holder.setChecked(holder.mJourney.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Journey> getSelectedItems() {

        List<Journey> selectedItems = new ArrayList<>();
        for (SelectableJourney item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    @Override
    public void onItemSelected(SelectableJourney item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableJourney selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }


}





