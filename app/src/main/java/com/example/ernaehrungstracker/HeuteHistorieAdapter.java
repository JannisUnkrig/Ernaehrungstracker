package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeuteHistorieAdapter extends RecyclerView.Adapter<HeuteHistorieAdapter.ViewHolderHH> {

    private ArrayList<Gericht> foodList;
    private ArrayList<String> foodNamesList = new ArrayList<>();
    private ArrayList<String> foodPortionenGrammList = new ArrayList<>();
    private ArrayList<Double> foodKcalList = new ArrayList<>();
    private ArrayList<Double> foodProtList = new ArrayList<>();
    private ArrayList<Double> foodKhList = new ArrayList<>();
    private ArrayList<Double> foodFettList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    HeuteHistorieAdapter(Context context, ArrayList<Gericht> foodList) {
        this.mInflater = LayoutInflater.from(context);

        this.foodList = foodList;
        for (Gericht i : foodList) {
            foodNamesList.add(i.getName());
            if (i.isInPortionen()) {
                if (i.getPortionenGramm() == 1) {
                    foodPortionenGrammList.add("");
                } else {
                    foodPortionenGrammList.add(MainActivity.doubleBeautifulizerNull(i.getPortionenGramm()) + " Portionen");
                }
            } else  {
                if (i.getPortionenGramm() == 1) {
                    foodPortionenGrammList.add("ein Gramm");
                } else {
                    foodPortionenGrammList.add(MainActivity.doubleBeautifulizerNull(i.getPortionenGramm()) + " Gramm");
                }
            }
            foodKcalList.add(i.getKcal());
            foodProtList.add(i.getProt());
            foodKhList.add(i.getKh());
            foodFettList.add(i.getFett());
        }

    }

    public interface ItemClickListener{
        void onItemClickHH(View view, int position);
    }

    public class ViewHolderHH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myNameTextView;
        TextView myPortionenGrammTextView;
        TextView myKcalTextView;
        TextView myProtTextView;
        TextView myKhTextView;
        TextView myFettTextView;

        ViewHolderHH(View itemview) {
            super(itemview);
            myNameTextView = itemview.findViewById(R.id.hhFoodName);
            myPortionenGrammTextView = itemview.findViewById(R.id.hhFoodPortionenGramm);
            myKcalTextView = itemview.findViewById(R.id.hhFoodKcal);
            myProtTextView = itemview.findViewById(R.id.hhFoodProt);
            myKhTextView = itemview.findViewById(R.id.hhFoodKh);
            myFettTextView = itemview.findViewById(R.id.hhFoodFett);

            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClickHH(view, getAdapterPosition());
        }
    }

    @Override
    public ViewHolderHH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hh_recy_row, parent, false);
        return new ViewHolderHH(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderHH holder, int position) {
        String foodNameHH = foodNamesList.get(position);
        String foodPortionenGrammHH = foodPortionenGrammList.get(position);
        double foodKcalHH = foodKcalList.get(position);
        double foodProtHH = foodProtList.get(position);
        double foodKhHH   = foodKhList.get(position);
        double foodFettHH = foodFettList.get(position);

        holder.myNameTextView.setText(foodNameHH);
        holder.myPortionenGrammTextView.setText(foodPortionenGrammHH);
        holder.myKcalTextView.setText(MainActivity.doubleBeautifulizerNull(foodKcalHH) + " kcal");
        holder.myProtTextView.setText("" + MainActivity.doubleBeautifulizerNull(foodProtHH) + " g Prot");
        holder.myKhTextView.setText("" + MainActivity.doubleBeautifulizerNull(foodKhHH) + " g KH");
        holder.myFettTextView.setText("" + MainActivity.doubleBeautifulizerNull(foodFettHH) + " g Fett");

        //if (foodDescription.equals("")) holder.myDescriptionTextView.setHeight(0);
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