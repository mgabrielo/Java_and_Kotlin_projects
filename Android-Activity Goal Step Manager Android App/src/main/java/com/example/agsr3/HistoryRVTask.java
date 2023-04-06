package com.example.agsr3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryRVTask extends AppCompatActivity {


    private EditText actSectEdt, stepSectEdt, ExtraStep_edt;
    TextView His_Date;
    private Button His_goalSaveBtn, His_UpdateGoalBtn, Date_Btn;
    TextInputLayout His_textInputLayout;
    AutoCompleteTextView His_autoCompleteTextView;

    RoomDatabaseClass Db_Hist_Class;
    StatusDB status_his_db;
    boolean isOldGoals =false;
    boolean areAllFieldsChecked = false;
    private int ExtraInitialStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_rvtask);

        actSectEdt = findViewById(R.id.history_ActSect);
        stepSectEdt = findViewById(R.id.history_StepCount);
        ExtraStep_edt = findViewById(R.id.history_ExtraStepCount);
        His_Date  = findViewById(R.id.history_Date);
        His_UpdateGoalBtn  = findViewById(R.id.history_UpdateGoal);
        His_goalSaveBtn = findViewById(R.id.history_SaveRoomGoal);
        His_textInputLayout = findViewById(R.id.history_menu_drop);
        His_autoCompleteTextView = findViewById(R.id.history_drop_items);
        Date_Btn = findViewById(R.id.DateBtn);


        Init();

        Date_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });


        String [] items ={"Walk","Jog","Run","Marathon","Stairs"};
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.item_list, items);
        His_autoCompleteTextView.setAdapter(itemsAdapter);
        His_autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actSectEdt.setText((String) parent.getItemAtPosition(position));
            }
        });


        status_his_db = new StatusDB();
        try {
            status_his_db = (StatusDB) getIntent().getSerializableExtra("old_goals");
            actSectEdt.setText(status_his_db.getActSect());
            stepSectEdt.setText(status_his_db.getStepSect());
            ExtraStep_edt.setText(status_his_db.getExtraSects());
            His_Date.setText(status_his_db.getDateSects());

            isOldGoals =true;
            His_UpdateGoalBtn.setVisibility(View.VISIBLE);
            His_goalSaveBtn.setVisibility(View.INVISIBLE);
            His_UpdateGoalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String actNew = actSectEdt.getText().toString();
                    String stepNew = stepSectEdt.getText().toString();
                    String extraNew = ExtraStep_edt.getText().toString();
                    String dateNew = His_Date.getText().toString();

                    areAllFieldsChecked = CheckAllField();
                    if (actNew.isEmpty() || stepNew.isEmpty()  || extraNew.isEmpty() && areAllFieldsChecked ) {
                        Toast.makeText(HistoryRVTask.this, "Please enter the valid goal details.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                    Date date = new Date();

                    if (areAllFieldsChecked) {
                        status_his_db.setActSect(actNew);
                        status_his_db.setStepSect(stepNew);
                        status_his_db.setExtraSects(extraNew);
                        status_his_db.setDateSects(dateNew);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("goals", status_his_db);
                    setResult(878, intent);
                    finish();


                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
        //DeleteGoalBtn.setVisibility(View.VISIBLE);
    }

    private void openDatePicker() {

        Calendar calender = Calendar.getInstance();

        int YEAR = calender.get(Calendar.YEAR);
        int MONTH =  calender.get(Calendar.MONTH);
        int DATE = calender.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                His_Date.setText(dayOfMonth + "-"+ month +"-"+ year );
            }
        } , YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void Init() {
        ExtraStep_edt.setText(Integer.toString(ExtraInitialStep));

    }

    private boolean  CheckAllField() {

        if (actSectEdt.length() == 0) {
            actSectEdt.setError("This field is required");
            return false;
        }

        if (stepSectEdt.length() == 0) {
            stepSectEdt.setError("This field is required");
            return false;
        }

        if (ExtraStep_edt.length() == 0) {
            ExtraStep_edt.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;

    }

}