package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistorischeGerichteAdapter extends RecyclerView.Adapter<HistorischeGerichteAdapter.ViewHolderHG> {

    private HeuteSpeicher HS;
    private ArrayList<Gericht> foodList;
    private ArrayList<String> foodPortionenGrammList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Boolean displayDetails;

    HistorischeGerichteAdapter(Context context, HeuteSpeicher HS, Boolean displayDetails) {
        this.mInflater = LayoutInflater.from(context);
        this.HS = HS;
        this.displayDetails = displayDetails;
        this.foodList = HS.getGegesseneGerichte();
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
        View myNaehrwerteLayout;
        TextView myStrich1hg;
        TextView myStrich2hg;
        TextView myStrich3hg;

        ViewHolderHG(View itemview) {
            super(itemview);
            myNameTextView = itemview.findViewById(R.id.hgFoodName);
            myPortionenGrammTextView = itemview.findViewById(R.id.hgFoodPortionenGramm);
            myKcalTextView = itemview.findViewById(R.id.hgFoodKcal);
            myProtTextView = itemview.findViewById(R.id.hgFoodProt);
            myKhTextView = itemview.findViewById(R.id.hgFoodKh);
            myFettTextView = itemview.findViewById(R.id.hgFoodFett);
            myNaehrwerteLayout = itemview.findViewById(R.id.hgNaehrwerteLayout);
            myStrich1hg = itemview.findViewById(R.id.strich1hg);
            myStrich2hg = itemview.findViewById(R.id.strich2hg);
            myStrich3hg = itemview.findViewById(R.id.strich3hg);

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


        if (HS.isTrackKcal()) {
            holder.myKcalTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myKcalTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackProt()) {
            holder.myProtTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myProtTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackKh()) {
            holder.myKhTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myKhTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackFett()) {
            holder.myFettTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myFettTextView.setVisibility(View.GONE);
        }


        if (HS.isTrackKcal() && HS.isTrackProt()) {
            holder.myStrich1hg.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich1hg.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt()) && HS.isTrackKh()) {
            holder.myStrich2hg.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich2hg.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt() || HS.isTrackKh()) && HS.isTrackFett()) {
            holder.myStrich3hg.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich3hg.setVisibility(View.GONE);
        }


        if (displayDetails) {
            holder.myKcalTextView.setText(MainActivity.doubleBeautifulizerNull(foodKcalHG) + " kcal");
            holder.myProtTextView.setText(MainActivity.doubleBeautifulizerNull(foodProtHG) + " g Prot");
            holder.myKhTextView.setText(  MainActivity.doubleBeautifulizerNull(foodKhHG) + " g KH");
            holder.myFettTextView.setText(MainActivity.doubleBeautifulizerNull(foodFettHG) + " g Fett");
        } else {
            holder.myNaehrwerteLayout.setVisibility(View.GONE);
        }
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