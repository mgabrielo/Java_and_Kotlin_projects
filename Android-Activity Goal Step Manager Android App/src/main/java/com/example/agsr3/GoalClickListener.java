package com.example.agsr3;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;

public interface GoalClickListener {
    void onEClick(GoalModal goalModal,ImageButton edit_btn);
    void onDClick(GoalModal goalModal, ImageButton delete_btn);
    void onEHisClick(StatusDB statusDB,ImageButton edit_btn);
    void onDHisClick(StatusDB statusDBl, ImageButton delete_btn);
    void onCClick(GoalModal goalModal, ImageButton active_btn);

}
