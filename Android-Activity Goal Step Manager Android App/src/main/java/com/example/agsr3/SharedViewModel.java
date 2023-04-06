package com.example.agsr3;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {


    MutableLiveData<ArrayList<StatusDB>> userLiveData;
    ArrayList<StatusDB> userArrayList;

    public SharedViewModel() {
        userLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<StatusDB>> getUserMutableLiveData(){
        return userLiveData;
    }

    public void init(){
        populateList();
        userLiveData.setValue(userArrayList);
    }

    public void populateList(){

        StatusDB user = new StatusDB();
        userArrayList = new ArrayList<>();
        userArrayList.add(user);

    }
}
