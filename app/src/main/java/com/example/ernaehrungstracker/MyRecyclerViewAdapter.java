package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Gericht> foodList;
    private ArrayList<String> foodNamesList = new ArrayList<>();
    private ArrayList<String> foodDescriptionsList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyRecyclerViewAdapter(Context context, ArrayList<Gericht> foodList) {
        this.mInflater = LayoutInflater.from(context);

        this.foodList = foodList;
        for (Gericht i : foodList) {
            foodNamesList.add(i.getName());
            foodDescriptionsList.add(i.getDescription());
        }

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

}
