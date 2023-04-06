package com.example.agsr3;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{


    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    RecyclerViewAdapter H_recyclerViewAdapter;
    TextView HistoryTxt, his_text;
    RecyclerView H_recyclerView;
    List<StatusDB> statusDBS = new ArrayList<>();
    RoomDatabaseClass db_Status_Class;
    StatusDB db_stat;
    SharedViewModel viewModel;
    LinearLayoutManager H_layoutManager;
    private boolean sats = false;
    private boolean switchOnOff;

    Switch switch_off_delete;

    String string;
    Boolean aBoolean;
    int ruff;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private String mParam3;

    public HistoryFragment() {
        // Required empty public constructor
    }


    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }




    ActivityResultLauncher<Intent> launchAddGoalActivity2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 878) {
                        Intent data = result.getData();
                        StatusDB updateGoal  =  (StatusDB) data.getSerializableExtra("goals");
                        db_Status_Class.statusDao().update(updateGoal.getUid(),updateGoal.getActSect(),updateGoal.getStepSect(),updateGoal.getExtraSects(),updateGoal.getDateSects());
                        statusDBS.clear();
                        statusDBS.addAll(db_Status_Class.statusDao().getAllStats());
                        H_recyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "History Updated", Toast.LENGTH_SHORT).show();

                    } else if (result.getResultCode() == 676) {

                    }

                }
            });





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        if (savedInstanceState != null) {
            // Restore last state for checked position.
            switchOnOff = savedInstanceState.getBoolean("curChoice", false);
        }


        H_recyclerView = view.findViewById(R.id.HistoryRecycleView);

        loadData();

        switch_off_delete =  view.findViewById(R.id.switch_item);



            switch_off_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                    if (b == switchOnOff ) {
                            H_recyclerViewAdapter.areFieldsChecked = false;
                            H_recyclerViewAdapter.notifyDataSetChanged();



                    } else   {
                        H_recyclerViewAdapter.areFieldsChecked = true;
                        H_recyclerViewAdapter.notifyDataSetChanged();

                    }

                    saveData();

                }
            });




        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();


                db_stat = new StatusDB();
        getParentFragmentManager().setFragmentResultListener("request_Key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String reef = result.getString("bundle_Key");
                String raff  = result.getString("bundle_Keys");
                String riff  = result.getString("bundle_Kes");
                ruff = result.getInt("bundle_rof", 0);

                db_stat.setActSect(reef);
                db_stat.setStepSect(raff);
                db_stat.setExtraSects(riff);
                String dates = formatter.format(date);
                db_stat.setDateSects(dates);
                db_Status_Class = RoomDatabaseClass.getInstance(getActivity());
                if(ruff != db_stat.uid) {
                    db_Status_Class.statusDao().insert(db_stat);
                    statusDBS.clear();
                    statusDBS.addAll(db_Status_Class.statusDao().getAllStats());
                } else {
                   // db_Status_Class.statusDao().update(ruff,reef,raff,riff,dates);
                    statusDBS.clear();
                    statusDBS.addAll(db_Status_Class.statusDao().getAllStats());
                }

            }
        });



        /*db_Status_Class = RoomDatabaseClass.getInstance(getActivity());
        db_Status_Class.statusDao().insert(db_stat);
        statusDBS.clear();
        statusDBS.addAll(db_Status_Class.statusDao().getAllStats());*/
       // statusDBS = db_Status_Class.statusDao().getAllStats();

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);



        H_recyclerView.setHasFixedSize(true);
        H_layoutManager = new LinearLayoutManager(getActivity());
        H_recyclerView.setLayoutManager(H_layoutManager);
        H_recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), statusDBS, goalClickListener );
        H_recyclerView.setAdapter(H_recyclerViewAdapter);

        // updateHisRV(statusDBS);



        updateViews();

        return view;
    }



   /* private void updateHisRV(List<StatusDB> statusDBS) {

    }*/


    private Observer<ArrayList<StatusDB>> userListUpdateObserver = new Observer<ArrayList<StatusDB>>() {
        @Override
        public void onChanged(ArrayList<StatusDB> userArrayList) {
            H_recyclerView.setHasFixedSize(true);
            H_layoutManager = new LinearLayoutManager(requireActivity());
            H_recyclerView.setLayoutManager(H_layoutManager);
            H_recyclerViewAdapter = new RecyclerViewAdapter(requireActivity(), statusDBS, goalClickListener );
            saveData();
            H_recyclerView.setAdapter(H_recyclerViewAdapter);

            // homeAdapter = new HomeAdapter(requireActivity(),userArrayList);
        }
    };



    private final GoalClickListener goalClickListener = new GoalClickListener() {
        @Override
        public void onEClick(GoalModal goalModal, ImageButton edit_btn) {

        }

        @Override
        public void onDClick(GoalModal goalModal, ImageButton delete_btn) {




        }

        @Override
        public void onEHisClick(StatusDB statusDB, ImageButton edit_btn) {

            Intent intent = new Intent(getActivity(), HistoryRVTask.class);
            intent.putExtra("old_goals",statusDB);
            launchAddGoalActivity2.launch(intent);
            saveData();


        }

        @Override
        public void onDHisClick(StatusDB statusDBl, ImageButton delete_btn) {
            db_stat = new StatusDB();
            db_stat = statusDBl;
            showPopup(delete_btn);
            saveData();
        }

        @Override
        public void onCClick(GoalModal goalModal, ImageButton active_btn) {


        }



    };

    private void saveData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(statusDBS);
        editor.putString("task list",json);
        editor.putBoolean("switch 1", switch_off_delete.isChecked());
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preference", MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean("switch 1", false);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<StatusDB>>() {}.getType();
        statusDBS = gson.fromJson(json, type);

        if (statusDBS == null){
            statusDBS = new ArrayList<>();
        }

    }


    private void updateViews(){
        switch_off_delete.setChecked(switchOnOff);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("curChoice", switchOnOff);
    }


    private void showPopup(ImageButton delete_btn) {

        PopupMenu popupMenu =  new PopupMenu(getActivity(), delete_btn);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.rv_menu_delete_popup);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                db_Status_Class.statusDao().delete(db_stat);
                statusDBS.remove(db_stat);
                H_recyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Goal Deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;

        }
    }



}