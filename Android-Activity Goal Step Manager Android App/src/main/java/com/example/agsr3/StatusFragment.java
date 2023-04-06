package com.example.agsr3;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StatusFragment extends Fragment  {

    EditText textView1,textView2;
    int numAddSteep;
    int yan;
    int res;
   TextView progress_txt;
    EditText addStepEdt;
    ProgressBar progressBar2;
    AppCompatButton progressBtn, addStepBtn;
    List<StatusDB> statusDBS = new ArrayList<>();
    StatusDB statDB;
    RoomDatabaseClass db_Status_Class;
    RecyclerViewAdapter H_recyclerViewAdapter;
    long rest = 0;
    String percent ="%";
    int y =0;
    int rof;
    String d_goal_txt= "text1";
    String  d_step_txt = "text2";
    String  d_step_add = "text3";
    public static final String SHARED_PREFS = "sharedPrefs";



    private int valueProgress = 0;
    private int addInitialExtraStep = 0;

    boolean isOldGoals =false;
    boolean areFieldsChecked = false;
    private boolean started = false;
    private Handler handler = new Handler();
   boolean state_play_button;
    boolean temp=true;
    String progress_news;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);




        textView1 =view.findViewById(R.id.actStat);
        textView2 = view.findViewById(R.id.stepStat);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
              String ref = result.getString("bundleKey");
              String raf  = result.getString("bundleKeys");
              rof  = result.getInt("bundle_id");
                textView1.setText(ref);
                textView2.setText(raf);
                savDat();
            }
        });





        addStepEdt = view.findViewById(R.id.addStepEdtTxt);
        addStepBtn = view.findViewById(R.id.addStepBtn);
        progressBar2 = view.findViewById(R.id.progress_bar_2);
        progress_txt = view.findViewById(R.id.progress_bar_2_text);
        progressBtn = view.findViewById(R.id.progress_btn);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("space_name", MODE_PRIVATE);
        valueProgress = sharedPreferences.getInt("progress", 0);







        addStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStepper();
                String goalStp = textView2.getText().toString();
                String addNewSteps = addStepEdt.getText().toString();
                int numGoalStp = Integer.parseInt(goalStp);
                int numAddedStp = Integer.parseInt(addNewSteps);
                 res = numGoalStp + numAddedStp;
                textView2.setText(Integer.toString(res));

                savDat();

            }
        });

          updateProgress();


        Init();
        progressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String actNews = textView1.getText().toString();
                String stepNews = textView2.getText().toString();
                String extraNews = addStepEdt.getText().toString();
                progress_news = progress_txt.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                savDat();
                areFieldsChecked = CheckFields();
                if (actNews.isEmpty() || stepNews.isEmpty() || extraNews.isEmpty() && areFieldsChecked) {
                    Toast.makeText(getActivity(), "Please enter value for all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (areFieldsChecked) {
                    Bundle result = new Bundle();
                    result.putString("bundle_Key", actNews);
                    result.putString("bundle_Keys", stepNews);
                    result.putString("bundle_Kes", extraNews);
                    result.putInt("bundle_rof", rof);

                    getParentFragmentManager().setFragmentResult("request_Key", result);


                   /* Fragment frag_m = HistoryFragment.newInstance();
                    FragmentTransaction transact = getActivity().getSupportFragmentManager().beginTransaction();
                    transact.replace(R.id.frameLayout, frag_m, "history_fragment");
                    transact.addToBackStack(null);
                    transact.commit();*/
                }

                if(state_play_button) {
                    state_play_button = false;
                    temp =false;
                    progressBtn.setText("Resume");

                }else{
                    state_play_button = true;
                    temp = true;
                    thread.start();
                    progressBtn.setText("Start");
                }

               String goalSteep = textView2.getText().toString();
                String addSteps = addStepEdt.getText().toString();
                int numGoalSteep = Integer.parseInt(goalSteep);
                int numAddedSteep = Integer.parseInt(addSteps);
              rest = (long) (numGoalSteep + numAddedSteep)* 10;

                y = 100 +   yan ;




            }

                   // if(valueProgress)

        });



        LoadDat();

        UpdateDat();


            //db_Status_Class.statusDao().pin(rof,progress_news);


        textView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                valueProgress =0;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        return view;
    }



    private void Init() {
        addStepEdt.setText(Integer.toString(addInitialExtraStep));

    }




    private void savDat() {
        SharedPreferences sharedPrefer = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPrefer.edit();
        edit.putString("task list 6", textView1.getText().toString());
        edit.putString("task list 7", textView2.getText().toString());
        edit.putString("task list 8", addStepEdt.getText().toString());
        edit.apply();


    }



    private void LoadDat() {
        SharedPreferences sharedPrefer = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
            d_goal_txt = sharedPrefer.getString("task list 6", "");
            d_step_txt = sharedPrefer.getString("task list 7", "");
            d_step_add = sharedPrefer.getString("task list 8","");

    }

    private void UpdateDat() {
        textView1.setText(d_goal_txt);
        textView2.setText(d_step_txt);
        addStepEdt.setText(d_step_add);
    }



    private void updateProgress(){
        progressBar2.setProgress(valueProgress);
        progress_txt.setText(valueProgress+"%");


    }

    public void addStepper(){
        if (valueProgress <= 100){
            String addSteps = addStepEdt.getText().toString();
             numAddSteep = Integer.parseInt(addSteps);
            String goal_Step = textView2.getText().toString();
            int num_goal_Step  = Integer.parseInt(goal_Step);
             double fen = ( (double) numAddSteep/(num_goal_Step+numAddSteep))*100;
             yan  = (int) fen ;
            valueProgress +=  yan ;
            updateProgress();
        }
    }


    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (valueProgress <= y && temp) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        progressBar2.setProgress(valueProgress);
                        progress_txt.setText(new StringBuilder().append(valueProgress).append(percent).toString());


                    }
                });
                try {
                    Thread.sleep(rest);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                valueProgress++;



            }


        }
    });

    private boolean  CheckFields() {

        if (textView1.length() == 0) {
            textView1.setError("This field is required");
            return false;
        }

        if (textView2.length() == 0) {
            textView2.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("space_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("progress", valueProgress).apply();

    }


    @Override
    public void onResume() {
        super.onResume();
        Init();
        if(valueProgress >= 100){
            valueProgress = 0;
        }else if( valueProgress == y){
            valueProgress = y;
        }

    }
}