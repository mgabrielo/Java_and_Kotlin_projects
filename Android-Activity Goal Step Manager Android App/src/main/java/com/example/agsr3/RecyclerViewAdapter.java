package com.example.agsr3;


import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<StatusViewHolder>{


    Context context;
    List<StatusDB> statusDBList;
    GoalClickListener listener;

    public static boolean isHidden = false;

   boolean areFieldsChecked = false;

    public RecyclerViewAdapter(Context context, List<StatusDB> statusDBList, GoalClickListener listener) {
        this.context = context;
        this.statusDBList = statusDBList;
        this.listener = listener;


        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.item_history_recycler_view, parent, false);
        StatusViewHolder statusViewHolder = new StatusViewHolder(view);
        return statusViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        holder.ActSects.setText(statusDBList.get(position).getActSect());
        holder.StepSects.setText(statusDBList.get(position).getStepSect());
        holder.Extra_step.setText(statusDBList.get(position).getExtraSects());
        holder.DateSects.setText(statusDBList.get(position).getDateSects());
       holder.rv_HisProgressTXt.setText(statusDBList.get(position).getProgressbarText());
        // holder.rvHisProgressBar.setProgress();




        if (areFieldsChecked == statusDBList.get(position).isPins() ) {
            holder.rvEditBtn.setVisibility(View.GONE);

        }
        else if(areFieldsChecked != statusDBList.get(position).isPins()) {
            holder.rvEditBtn.setVisibility(View.VISIBLE);


        }

        holder.rvEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEHisClick(statusDBList.get(holder.getAdapterPosition()), holder.rvEditBtn);
            }
        });
        holder.rvDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDHisClick(statusDBList.get(holder.getAdapterPosition()), holder.rvDeleteBtn);
            }
        });


    }

    private boolean isAllGoalsHidden() {
  if(areFieldsChecked){
            return true;
        }else return false;
    }



    @Override
    public int getItemCount() {
        return statusDBList.size();
    }



}
class StatusViewHolder extends  RecyclerView.ViewHolder{


    TextView ActSects,StepSects,DateSects,Extra_step, rv_HisProgressTXt;
    ImageButton rvEditBtn, rvDeleteBtn;
    ProgressBar rvHisProgressBar;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);

        ActSects =itemView.findViewById(R.id.rv_his_ActSelect);
        StepSects = itemView.findViewById(R.id.rv_his_StepCount);
        DateSects = itemView.findViewById(R.id.rv_his_MyDate);
        rvEditBtn = itemView.findViewById(R.id.rv_his_edit_btn);
        Extra_step =  itemView.findViewById(R.id.rv_his_ExtraCount);
        rvDeleteBtn = itemView.findViewById(R.id.rv_his_delete_btn);
        rvHisProgressBar = itemView.findViewById(R.id.rv_his_progress_bar_2);
        rv_HisProgressTXt = itemView.findViewById(R.id.rv_his_progress_bar_2_text);

    }



}
