package com.example.ernaehrungstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistorieAdapter extends RecyclerView.Adapter<HistorieAdapter.ViewHolderH> {

    private ArrayList<HeuteSpeicher> HSL;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    HistorieAdapter(Context context, ArrayList<HeuteSpeicher> HSL) {
        this.mInflater = LayoutInflater.from(context);

        this.HSL = HSL;
    }

    public interface ItemClickListener{
        void onItemClickH(View view, int position);
    }

    public class ViewHolderH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myDateTextView;
        TextView myGewichtTextView;
        TextView myKcalTextView;
        TextView myKcalGoalTextView;
        TextView myProtTextView;
        TextView myProtGoalTextView;
        TextView myKhTextView;
        TextView myKhGoalTextView;
        TextView myFettTextView;
        TextView myFettGoalTextView;
        TextView myStrich1h;
        TextView myStrich2h;
        TextView myStrich3h;

        ViewHolderH(View itemview) {
            super(itemview);
            myDateTextView = itemview.findViewById(R.id.hDate);
            myGewichtTextView = itemview.findViewById(R.id.hGewicht);
            myKcalTextView = itemview.findViewById(R.id.hKcal);
            myProtTextView = itemview.findViewById(R.id.hProt);
            myKhTextView = itemview.findViewById(R.id.hKh);
            myFettTextView = itemview.findViewById(R.id.hFett);
            myKcalGoalTextView = itemview.findViewById(R.id.hKcalGoal);
            myProtGoalTextView = itemview.findViewById(R.id.hProtGoal);
            myKhGoalTextView = itemview.findViewById(R.id.hKhGoal);
            myFettGoalTextView = itemview.findViewById(R.id.hFettGoal);
            myStrich1h = itemview.findViewById(R.id.strich1h);
            myStrich2h = itemview.findViewById(R.id.strich2h);
            myStrich3h = itemview.findViewById(R.id.strich3h);

            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClickH(view, getAdapterPosition());
        }
    }

    @Override
    public ViewHolderH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.h_recy_row, parent, false);
        return new ViewHolderH(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderH holder, int position) {
        HeuteSpeicher HS = HSL.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("dd. MMM yyyy");
        String dateH = formatter.format(HS.getDate());
        double gewichtH = HS.getGewicht();
        double kcalH = HS.getKcalHeute();
        double protH = HS.getProtHeute();
        double khH   = HS.getKhHeute();
        double fettH = HS.getFettHeute();
        double kcalGoalH = HS.getKcalZielHeute();
        double protGoalH = HS.getProtZielHeute();
        double khGoalH   = HS.getKhZielHeute();
        double fettGoalH = HS.getFettZielHeute();

        holder.myDateTextView.setText(dateH);

        if (gewichtH == -1) {
            holder.myGewichtTextView.setText("");
        } else {
            holder.myGewichtTextView.setText("    |   " + MainActivity.doubleBeautifulizerNull(gewichtH) + MainActivity.curMainAct.getString(R.string.kg));
        }



        if (HS.isTrackKcal()) {
            holder.myKcalTextView.setVisibility(View.VISIBLE);
            holder.myKcalGoalTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myKcalTextView.setVisibility(View.GONE);
            holder.myKcalGoalTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackProt()) {
            holder.myProtTextView.setVisibility(View.VISIBLE);
            holder.myProtGoalTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myProtTextView.setVisibility(View.GONE);
            holder.myProtGoalTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackKh()) {
            holder.myKhTextView.setVisibility(View.VISIBLE);
            holder.myKhGoalTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myKhTextView.setVisibility(View.GONE);
            holder.myKhGoalTextView.setVisibility(View.GONE);
        }
        if (HS.isTrackFett()) {
            holder.myFettTextView.setVisibility(View.VISIBLE);
            holder.myFettGoalTextView.setVisibility(View.VISIBLE);
        } else {
            holder.myFettTextView.setVisibility(View.GONE);
            holder.myFettGoalTextView.setVisibility(View.GONE);
        }


        if (HS.isTrackKcal() && HS.isTrackProt()) {
            holder.myStrich1h.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich1h.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt()) && HS.isTrackKh()) {
            holder.myStrich2h.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich2h.setVisibility(View.GONE);
        }
        if ((HS.isTrackKcal() || HS.isTrackProt() || HS.isTrackKh()) && HS.isTrackFett()) {
            holder.myStrich3h.setVisibility(View.VISIBLE);
        } else {
            holder.myStrich3h.setVisibility(View.GONE);
        }


        holder.myKcalTextView.setText(MainActivity.doubleBeautifulizerNull(kcalH));
        if (kcalGoalH != -1) {
            holder.myKcalGoalTextView.setText("/" + MainActivity.doubleBeautifulizerNull(kcalGoalH) + " " + MainActivity.curMainAct.getString(R.string.kcal));
        } else {
            holder.myKcalGoalTextView.setText(" " + MainActivity.curMainAct.getString(R.string.kcal));
        }

        holder.myProtTextView.setText("" + MainActivity.doubleBeautifulizerNull(protH));
        if (protGoalH != -1) {
            holder.myProtGoalTextView.setText("/" + MainActivity.doubleBeautifulizerNull(protGoalH) + MainActivity.curMainAct.getString(R.string.g_prot));
        } else {
            holder.myProtGoalTextView.setText(MainActivity.curMainAct.getString(R.string.g_prot));
        }

        holder.myKhTextView.setText("" + MainActivity.doubleBeautifulizerNull(khH));
        if (khGoalH != -1) {
            holder.myKhGoalTextView.setText("/" + MainActivity.doubleBeautifulizerNull(khGoalH) + MainActivity.curMainAct.getString(R.string.g_kh));
        } else {
            holder.myKhGoalTextView.setText(MainActivity.curMainAct.getString(R.string.g_kh));
        }

        holder.myFettTextView.setText("" + MainActivity.doubleBeautifulizerNull(fettH));
        if (fettGoalH != -1) {
            holder.myFettGoalTextView.setText("/" + MainActivity.doubleBeautifulizerNull(fettGoalH) + MainActivity.curMainAct.getString(R.string.g_fett));
        } else {
            holder.myFettGoalTextView.setText(MainActivity.curMainAct.getString(R.string.g_fett));
        }

        //if (foodDescription.equals("")) holder.myDescriptionTextView.setHeight(0);
    }

    @Override
    public  int getItemCount() {
        return HSL.size();
    }

    HeuteSpeicher getHS(int position) {
        return HSL.get(position);
    }

    void setmClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}