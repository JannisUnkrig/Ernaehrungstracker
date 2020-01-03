package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Gericht> foodList;
    private ArrayList<Gericht> foodListFull;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyRecyclerViewAdapter(Context context, ArrayList<Gericht> foodList) {
        this.mInflater = LayoutInflater.from(context);

        this.foodList = foodList;
        foodListFull = new ArrayList<>(foodList);
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myNameTextView;
        TextView myDescriptionTextView;


        ViewHolder(View itemview) {
            super(itemview);
            myNameTextView = itemview.findViewById(R.id.foodName);
            myDescriptionTextView = itemview.findViewById(R.id.foodDescription);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recy_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String foodName = foodList.get(position).getName();
        String foodDescription = foodList.get(position).getDescription();

        holder.myNameTextView.setText(foodName);
        holder.myDescriptionTextView.setText(foodDescription);
        if (foodDescription.equals("")) holder.myDescriptionTextView.setHeight(0);

    }

    @Override
    public  int getItemCount() {
        return foodList.size();
    }

    Gericht getFood(int position) {
        return foodList.get(position);
    }

    void setmClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return foodFilter;
    }

    private Filter foodFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Gericht> filteredList = new LinkedList<>();

            //keine sucheingabe
            if (constraint == null || constraint.length() == 0) {

                if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.NEUTRAL) {
                    filteredList.addAll(foodListFull);
                }
                else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.HIGH) {
                    if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 0 ) {
                        filteredList.addAll(foodListFull);
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 1 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKcalNote() == Note.HIGH) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 2 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getProtNote() == Note.HIGH) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 3 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKhNote() == Note.HIGH) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 4 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getFettNote() == Note.HIGH) filteredList.add(gericht);
                        }
                    }
                }
                else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.LOW) {
                    if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 0 ) {
                        filteredList.addAll(foodListFull);
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 1 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKcalNote() == Note.LOW) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 2 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getProtNote() == Note.LOW) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 3 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKhNote() == Note.LOW) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 4 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getFettNote() == Note.LOW) filteredList.add(gericht);
                        }
                    }
                }

            //sucheingabe vorhanden
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.NEUTRAL) {
                    for (Gericht gericht : foodListFull) {
                        if (gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                    }
                }
                else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.HIGH) {
                    if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 0 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 1 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKcalNote() == Note.HIGH && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 2 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getProtNote() == Note.HIGH && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 3 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKhNote() == Note.HIGH && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 4 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getFettNote() == Note.HIGH && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                }
                else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.gesucht == Note.LOW) {
                    if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 0 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 1 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKcalNote() == Note.LOW && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 2 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getProtNote() == Note.LOW && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 3 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getKhNote() == Note.LOW && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                    else if (GerichtAuswaehlenRecyclerViewActivity.curGARVA.naehrwert == 4 ) {
                        for (Gericht gericht : foodListFull) {
                            if (gericht.getFettNote() == Note.LOW && gericht.getName().toLowerCase().contains(filterPattern)) filteredList.add(gericht);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodList.clear();
            foodList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
