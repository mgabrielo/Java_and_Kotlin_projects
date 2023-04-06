package com.example.agsr3;



import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class NewGoalsActivity extends AppCompatActivity{

    private EditText actSelectEdt, stepCountEdt, myDateEdt;
    private Button goalSaveBtn, UpdateGoalBtn, DeleteGoalBtn;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;

    GoalDatabase gDatabases;
    GoalModal gModals;
    boolean isOldGoal =false;
    boolean isAllFieldsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goals);

        actSelectEdt = findViewById(R.id.EdtIdActSelect);
        stepCountEdt = findViewById(R.id.EdtIdStepCount);
        myDateEdt = findViewById(R.id.EdtIdMyDate);
        goalSaveBtn = findViewById(R.id.RoomGoal);
        UpdateGoalBtn = findViewById(R.id.UpdateGoal);
        DeleteGoalBtn = findViewById(R.id.DeleteGoal);
        textInputLayout = findViewById(R.id.menu_drop);
        autoCompleteTextView = findViewById(R.id.drop_items);

        String [] items ={"Walk","Jog","Run","Marathon","Stairs"};
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.item_list, items);
        autoCompleteTextView.setAdapter(itemsAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actSelectEdt.setText((String) parent.getItemAtPosition(position));
            }
        });







        gModals = new GoalModal();
        try {
            gModals = (GoalModal) getIntent().getSerializableExtra("old_goal");
            actSelectEdt.setText(gModals.getActSelect());
            stepCountEdt.setText(gModals.getStepSelect());
            isOldGoal =true;
            UpdateGoalBtn.setVisibility(View.VISIBLE);
            goalSaveBtn.setVisibility(View.INVISIBLE);
            UpdateGoalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String actNew = actSelectEdt.getText().toString();
                    String stepNew = stepCountEdt.getText().toString();

                    isAllFieldsChecked = CheckAllFields();
                    if (actNew.isEmpty() || stepNew.isEmpty()  && isAllFieldsChecked ) {
                        Toast.makeText(NewGoalsActivity.this, "Please enter the valid goal details.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                    Date date = new Date();

                    if (isAllFieldsChecked ) {
                        gModals.setActSelect(actNew);
                        gModals.setStepSelect(stepNew);
                        //gModals.setDateSelects(dateNew);
                        gModals.setDateSelects(formatter.format(date));
                    }
                        Intent intent = new Intent();
                        intent.putExtra("goal", gModals);
                        setResult(676, intent);
                        finish();


                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
        //DeleteGoalBtn.setVisibility(View.VISIBLE);


            goalSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     String actNew = actSelectEdt.getText().toString();
                     String stepNew = stepCountEdt.getText().toString();
                     String dateNew = myDateEdt.getText().toString();

                    isAllFieldsChecked = CheckAllFields();

                    if (actNew.isEmpty() || stepNew.isEmpty()  && isAllFieldsChecked ) {
                        Toast.makeText(NewGoalsActivity.this, "Please enter the valid goal details.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                    Date date = new Date();

                    if (isAllFieldsChecked ) {

                        if (!isOldGoal) {
                            gModals = new GoalModal();

                            gModals.setActSelect(actNew);
                            gModals.setStepSelect(stepNew);
                            //gModals.setDateSelects(dateNew);
                            gModals.setDateSelects(formatter.format(date));
                        }
                    }
                            Intent intent = new Intent();
                            intent.putExtra("goal", gModals);
                            setResult(787, intent);
                            finish();




                }
            });


        }



    private boolean CheckAllFields() {

        if (actSelectEdt.length() == 0) {
            actSelectEdt.setError("This field is required");
            return false;
        }

       else if (stepCountEdt.length() == 0) {
            stepCountEdt.setError("This field is required");
            return false;
        }else {
            // after all validation return true.
            return true;
        }
    }




}