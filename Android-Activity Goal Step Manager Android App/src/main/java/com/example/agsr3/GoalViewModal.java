package com.example.agsr3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GoalViewModal extends ViewModel{
    MutableLiveData<ArrayList<GoalModal>> userGoalLiveData;
    ArrayList<GoalModal> userGoalArrayList;

    public GoalViewModal() {
        userGoalLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<GoalModal>> getUserMutableLiveData(){
        return userGoalLiveData;
    }

    public void init(){
        populateList();
        userGoalLiveData.setValue(userGoalArrayList);
    }

    public void populateList(){

        GoalModal users = new GoalModal();
        userGoalArrayList = new ArrayList<>();
        userGoalArrayList.add(users);

    }


}
