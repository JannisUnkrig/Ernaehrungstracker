package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeuteHistorieAdapter extends RecyclerView.Adapter<HeuteHistorieAdapter.ViewHolderHH> {

    private HeuteSpeicher HS;
    private ArrayList<Gericht> foodList;
    private ArrayList<String> foodNamesList = new ArrayList<>();
    private ArrayList<String> foodPortionenGrammList = new ArrayList<>();
    private ArrayList<Double> foodKcalList = new ArrayList<>();
    private ArrayList<Double> foodProtList = new ArrayList<>();
    private ArrayList<Double> foodKhList = new ArrayList<>();
    private ArrayList<Double> foodFettList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Boolean displayDetails;

    HeuteHistorieAdapter(Context context, HeuteSpeicher HS, Boolean displayDetails) {
        this.mInflater = LayoutInflater.from(context);
        this.displayDetails = displayDetails;
        this.HS = HS;

        this.foodList = HS.getGegesseneGerichte();
        for (Gericht i : foodList) {
            foodNamesList.add(i.getName());
            if (i.isInPortionen()) {
                if (i.getPortionenGramm() == 1) {
                    foodPortionenGrammList.add("");
                } else {
                    foodPortionenGrammList.add(MainActivity.doubleBeautifulizerNull(i.getPortionenGramm()) + " " + MainActivity.curMainAct.getResources().getString(R.string.portionen));
                }
            } else  {
                if (i.getPortionenGramm() == 1) {
                    foodPortionenGrammList.add(MainActivity.curMainAct.getResources().getString(R.string.ein_gramm));
                } else if(i.getPortionenGramm() == 100) {
                    foodPortionenGrammList.add("");
                } else {
                    foodPortionenGrammList.add(MainActivity.doubleBeautifulizerNull(i.getPortionenGramm()) + " " + MainActivity.curMainAct.getResources().getString(R.string.gramm));
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
        View myNaehrwerteLayout;
        TextView myStrich1hh;
        TextView myStrich2hh;
        TextView myStrich3hh;

        ViewHolderHH(View itemview) {
            super(itemview);
            myNameTextView = itemview.findViewById(R.id.hhFoodName);
            myPortionenGrammTextView = itemview.findViewById(R.id.hhFoodPortionenGramm);
            myKcalTextView = itemview.findViewById(R.id.hhFoodKcal);
            myProtTextView = itemview.findViewById(R.id.hhFoodProt);
            myKhTextView = itemview.findViewById(R.id.hhFoodKh);
            myFettTextView = itemview.findViewById(R.id.hhFoodFett);
            myNaehrwerteLayout = itemview.findViewById(R.id.hhNaehrwerteLayout);
            myStrich1hh = itemview.findViewById(R.id.strich1hh);
            myStrich2hh = itemview.findViewById(R.id.strich2hh);
            myStrich3hh = itemview.findViewById(R.id.strich3hh);

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
            holder.myStrich1hh.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich1hh.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt()) && HS.isTrackKh()) {
            holder.myStrich2hh.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich2hh.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt() || HS.isTrackKh()) && HS.isTrackFett()) {
            holder.myStrich3hh.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich3hh.setVisibility(View.GONE);
        }


        if (displayDetails) {
            holder.myKcalTextView.setText(MainActivity.doubleBeautifulizerNull(foodKcalHH) + " " + MainActivity.curMainAct.getResources().getString(R.string.kcal));
            holder.myProtTextView.setText(MainActivity.doubleBeautifulizerNull(foodProtHH) + " " + MainActivity.curMainAct.getResources().getString(R.string.g_prot));
            holder.myKhTextView.setText(  MainActivity.doubleBeautifulizerNull(foodKhHH  ) + " " + MainActivity.curMainAct.getResources().getString(R.string.g_kh));
            holder.myFettTextView.setText(MainActivity.doubleBeautifulizerNull(foodFettHH) + " " + MainActivity.curMainAct.getResources().getString(R.string.g_fett));
        } else {
            holder.myNaehrwerteLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public  int getItemCount() {
        return foodList.size();
    }

    void setmClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}