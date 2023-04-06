package com.example.agsr3;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoalListAdapter extends RecyclerView.Adapter<GoalsViewHolder> {
    Context context;
    List<GoalModal> goalModalList;
    GoalClickListener listener;
    GoalModal gasModal;
    GoalDatabase gasDatabase;
   private final ActiveButtonListener ActButtonListener;
    boolean isaFieldsChecked = false;
    long yD;
    long idX;
    int positionX;
    int firstVisibleItem;

    public GoalListAdapter(Context context, List<GoalModal> goalModalList, GoalClickListener listener, ActiveButtonListener ActButtonListener) {
        this.context = context;
        this.goalModalList = goalModalList;
        this.listener = listener;
        this.ActButtonListener = ActButtonListener;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
        holder.bind(goalModalList.get(position), ActButtonListener);
        holder.ActSect.setText(goalModalList.get(position).getActSelect());
        holder.StepSect.setText(goalModalList.get(position).getStepSelect());
        holder.DateSect.setText(goalModalList.get(position).getDateSelects());

        isaFieldsChecked = isAGoalsHidden();

        if (isaFieldsChecked == goalModalList.get(position).isPinned()) {
            holder.rvEditBtn.setVisibility(View.GONE);

        }
        else if (isaFieldsChecked != goalModalList.get(position).isPinned()){
            holder.rvEditBtn.setVisibility(View.VISIBLE);

        }





        holder.rvEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEClick(goalModalList.get(holder.getAdapterPosition()), holder.rvEditBtn);
            }
        });
        holder.rvDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDClick(goalModalList.get(holder.getAdapterPosition()), holder.rvDeleteBtn);
            }
        });

        holder.rv_active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCClick(goalModalList.get(holder.getAdapterPosition()), holder.rv_active_btn);
              ActButtonListener.onActBtn(goalModalList.get(holder.getAdapterPosition()), holder.rv_active_btn);

                positionX = holder.getAdapterPosition();
                firstVisibleItem = 0;

                swapItems(positionX,firstVisibleItem);



                 idX  = goalModalList.get(holder.getAdapterPosition()).getId();

                //holder.rv_show_btn.setVisibility(View.VISIBLE);







            }
        });




    }


    private void swapItems(  int positionX, int firstVisibleItem ) {
      Collections.swap(goalModalList, positionX, firstVisibleItem);
      notifyItemMoved(positionX, firstVisibleItem);


    }

    private boolean isAGoalsHidden() {

        if(isaFieldsChecked){
            return true;

        }else return false;
    }


    @Override
    public long getItemId(int position) {
        return gasModal.getId();
    }

    @Override
    public int getItemCount() {
        return goalModalList.size();
    }
}
class GoalsViewHolder extends  RecyclerView.ViewHolder{

    CardView card_container;
    TextView ActSect,StepSect,DateSect;
    ImageButton rvEditBtn, rvDeleteBtn, rv_active_btn;
    Button rv_show_btn;
    public GoalsViewHolder(@NonNull View itemView) {
        super(itemView);
        card_container = itemView.findViewById(R.id.rvCard);
        ActSect =itemView.findViewById(R.id.idTVActSelect);
        StepSect = itemView.findViewById(R.id.idTVStepCount);
        DateSect = itemView.findViewById(R.id.idTVMyDate);
        rvEditBtn = itemView.findViewById(R.id.rv_edit_btn);
        rvDeleteBtn = itemView.findViewById(R.id.rv_delete_btn);
        rv_active_btn = itemView.findViewById(R.id.rv_active_btn);
        rv_show_btn = itemView.findViewById(R.id.idTVShowActive);
    }


    public void bind(GoalModal goalModal, ActiveButtonListener actButtonListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actButtonListener.onActBtn(goalModal,rv_active_btn);
            }
        });
    }
}
