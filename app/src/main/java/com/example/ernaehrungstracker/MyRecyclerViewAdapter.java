package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> foodNamesList;
    private List<String> foodDescriptionsList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyRecyclerViewAdapter(Context context, List<String> foodNames, List<String> foodDescriptions) {
        this.mInflater = LayoutInflater.from(context);
        this.foodNamesList = foodNames;
        this.foodDescriptionsList = foodDescriptions;
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
        String foodName = foodNamesList.get(position);
        String foodDescription = foodDescriptionsList.get(position);

        holder.myNameTextView.setText(foodName);
        holder.myDescriptionTextView.setText(foodDescription);

    }

    @Override
    public  int getItemCount() {
        return foodNamesList.size();
    }

    String getItem(int id) {
        return foodNamesList.get(id);
    }

    void setmClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}
