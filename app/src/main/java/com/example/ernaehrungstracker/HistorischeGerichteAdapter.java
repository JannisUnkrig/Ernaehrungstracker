package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistorischeGerichteAdapter extends RecyclerView.Adapter<HistorischeGerichteAdapter.ViewHolderHG> {

    private ArrayList<Gericht> foodList;
    private ArrayList<String> foodPortionenGrammList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    HistorischeGerichteAdapter(Context context, ArrayList<Gericht> foodList) {
        this.mInflater = LayoutInflater.from(context);

        this.foodList = foodList;
        for (Gericht i : foodList) {
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
        }

    }

    public interface ItemClickListener{
        void onItemClickHG(View view, int position);
    }

    public class ViewHolderHG extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myNameTextView;
        TextView myPortionenGrammTextView;
        TextView myKcalTextView;
        TextView myProtTextView;
        TextView myKhTextView;
        TextView myFettTextView;

        ViewHolderHG(View itemview) {
            super(itemview);
            myNameTextView = itemview.findViewById(R.id.hgFoodName);
            myPortionenGrammTextView = itemview.findViewById(R.id.hgFoodPortionenGramm);
            myKcalTextView = itemview.findViewById(R.id.hgFoodKcal);
            myProtTextView = itemview.findViewById(R.id.hgFoodProt);
            myKhTextView = itemview.findViewById(R.id.hgFoodKh);
            myFettTextView = itemview.findViewById(R.id.hgFoodFett);

            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClickHG(view, getAdapterPosition());
        }
    }

    @Override
    public ViewHolderHG onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hg_recy_row, parent, false);
        return new ViewHolderHG(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderHG holder, int position) {
        Gericht gericht = foodList.get(position);
        String foodNameHG = gericht.getName();
        String foodPortionenGrammHG = foodPortionenGrammList.get(position);
        double foodKcalHG = gericht.getKcal();
        double foodProtHG = gericht.getProt();
        double foodKhHG   = gericht.getKh();
        double foodFettHG = gericht.getFett();

        holder.myNameTextView.setText(foodNameHG);
        holder.myPortionenGrammTextView.setText(foodPortionenGrammHG);
        holder.myKcalTextView.setText(MainActivity.doubleBeautifulizerNull(foodKcalHG) + " kcal");
        holder.myProtTextView.setText(MainActivity.doubleBeautifulizerNull(foodProtHG) + " g Prot");
        holder.myKhTextView.setText(  MainActivity.doubleBeautifulizerNull(foodKhHG)   + " g KH");
        holder.myFettTextView.setText(MainActivity.doubleBeautifulizerNull(foodFettHG) + " g Fett");

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