package ie.ul.studentmail.ronan.journeyentry;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.recyclerview.widget.RecyclerView;

public class JourneyViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    CheckedTextView textView;
    SelectableJourney mJourney;
    OnItemSelectedListener itemSelectedListener;


    public JourneyViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        textView = (CheckedTextView) view.findViewById(R.id.checked_text_item);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mJourney.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mJourney);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundColor(Color.LTGRAY);
        } else {
            textView.setBackground(null);
        }
        mJourney.setSelected(value);
        textView.setChecked(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(SelectableJourney item);
    }

}