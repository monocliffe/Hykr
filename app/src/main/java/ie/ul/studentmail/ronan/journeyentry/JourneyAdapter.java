package ie.ul.studentmail.ronan.journeyentry;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JourneyAdapter extends RecyclerView.Adapter implements JourneyViewHolder.OnItemSelectedListener {

    private final List<SelectableJourney> mValues;
    private boolean isMultiSelectionEnabled;
    private JourneyViewHolder.OnItemSelectedListener listener;


    JourneyAdapter(JourneyViewHolder.OnItemSelectedListener listener,
                   List<Journey> journeys, boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (Journey journey: journeys) {
            mValues.add(new SelectableJourney(journey, false));
        }
    }

    @Override
    @NonNull
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checked_item, parent, false);

        return new JourneyViewHolder(itemView, this);
    }

    @Override
    @SuppressWarnings("NullableProblems")
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

    List<Journey> getSelectedItems() {

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





