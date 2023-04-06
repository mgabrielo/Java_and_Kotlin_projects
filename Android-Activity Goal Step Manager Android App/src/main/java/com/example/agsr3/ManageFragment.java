package com.example.agsr3;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.AndroidViewModel;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ManageFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

RecyclerView recyclerView;
GoalListAdapter goalListAdapter;
List<GoalModal> gModal = new ArrayList<>();
GoalDatabase gDatabase;
GoalViewModal gViewModal;
GoalModal selectedGoal;
Switch switch_goal_edt;
private TextView default_goal, default_step;
  LinearLayoutManager layoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private boolean mSwitchOnOff;
    private String default_goal_text;
    private String default_step_text;

        Parcelable mListState;

    public ManageFragment() {
        // Required empty public constructor
    }

    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String param1, String param2
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    mParam1 = getArguments().getString(ARG_PARAM1);
    mParam2 = getArguments().getString(ARG_PARAM2);*/

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        return fragment;
    }







    ActivityResultLauncher<Intent> launchAddGoalActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 787) {
                        Intent data = result.getData();
                            GoalModal getNewGoal = (GoalModal) data.getSerializableExtra("goal");
                            gDatabase.daoAccess().insert(getNewGoal);
                            gModal.clear();
                            gModal.addAll(gDatabase.daoAccess().getAll());
                            goalListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Goal saved", Toast.LENGTH_SHORT).show();

                    } else if (result.getResultCode() == 676) {
                        Intent data = result.getData();
                            GoalModal updateGoal  =  (GoalModal) data.getSerializableExtra("goal");
                            gDatabase.daoAccess().update(updateGoal.getId(),updateGoal.getActSelect(),updateGoal.getStepSelect());
                            gModal.clear();
                            gModal.addAll(gDatabase.daoAccess().getAll());
                            goalListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Goal Updated", Toast.LENGTH_SHORT).show();
                    }

                }
            });








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        loadData();
        FloatingActionButton fbAddGoal = view.findViewById(R.id.float_Add_Goal);
       default_goal = (TextView) view.findViewById(R.id.default_uneditable_goal);
       default_step =   (TextView) view.findViewById(R.id.default_step);
        recyclerView = view.findViewById(R.id.idRecycleView);
        switch_goal_edt = view.findViewById(R.id.switchItem2);
        gDatabase = GoalDatabase.getInstance(getActivity());
        gModal = gDatabase.daoAccess().getAll();


        switch_goal_edt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if ( b == mSwitchOnOff ) {
                    goalListAdapter.isaFieldsChecked =false;
                    mSwitchOnOff = false;
                    goalListAdapter.notifyDataSetChanged();

                    saveData();

                } else  {
                    goalListAdapter.isaFieldsChecked = true;
                    mSwitchOnOff = true;
                    goalListAdapter.notifyDataSetChanged();


                }



            }
        });


        gViewModal = new ViewModelProvider(requireActivity()).get(GoalViewModal.class);
        gViewModal.getUserMutableLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);



        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        goalListAdapter = new GoalListAdapter(getActivity(), gModal, goalClickListener ,activeButtonListener );
        recyclerView.setAdapter(goalListAdapter);
        saveData();


        //updateRV(gModal);


        if( goalListAdapter.isaFieldsChecked == mSwitchOnOff) {
            goalListAdapter.isaFieldsChecked =false;
            goalListAdapter.notifyDataSetChanged();

        }else{
            goalListAdapter.isaFieldsChecked =true;
            goalListAdapter.notifyDataSetChanged();
        }


        fbAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewGoalsActivity.class);
                launchAddGoalActivity.launch(intent);
                saveData();
            }
        });

        updateViews();
        return view;
    }

    private void init() {
       if ( mSwitchOnOff = false){
           goalListAdapter.isaFieldsChecked =true;
       }else{
           goalListAdapter.isaFieldsChecked = false;
       }
    }



   /* private void updateRV(List<GoalModal> gModal) {
    }*/


    private Observer<ArrayList<GoalModal>> userListUpdateObserver = new Observer<ArrayList<GoalModal>>() {
        @Override
        public void onChanged(ArrayList<GoalModal> userGoalArrayList) {
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(requireActivity());
            recyclerView.setLayoutManager(layoutManager);
            goalListAdapter = new GoalListAdapter(requireActivity(), gModal,goalClickListener,activeButtonListener);
            saveData();
            recyclerView.setAdapter(goalListAdapter);

            // homeAdapter = new HomeAdapter(requireActivity(),userArrayList);
        }
    };


    private void saveData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       Gson gson = new Gson();
       String json = gson.toJson(gModal);
        editor.putString("task list 2",json);
        editor.putString("task list 3",default_goal.getText().toString());
        editor.putString("task list 4",default_step.getText().toString());
        editor.putBoolean("switch 2", switch_goal_edt.isChecked());
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        mSwitchOnOff = sharedPreferences.getBoolean("switch 2", false);
        default_goal_text = sharedPreferences.getString("task list 3", "");
        default_step_text = sharedPreferences.getString("task list 4", "");
       Gson gson = new Gson();
        String json = sharedPreferences.getString("task list 2", null);
        Type type = new TypeToken<ArrayList<GoalModal>>() {}.getType();
       gModal = gson.fromJson(json, type);

        if (gModal == null){
           gModal = new ArrayList<>();
      }

    }


    private void updateViews(){
        switch_goal_edt.setChecked(mSwitchOnOff);
        default_goal.setText(default_goal_text);
        default_step.setText(default_step_text);
    }



        private final GoalClickListener goalClickListener = new GoalClickListener() {
            @Override
            public void onEClick(GoalModal goalModal, ImageButton edit_btn) {
                Intent intent = new Intent(getActivity(), NewGoalsActivity.class);
                intent.putExtra("old_goal",goalModal);
                launchAddGoalActivity.launch(intent);
            }

            @Override
            public void onDClick(GoalModal goalModal, ImageButton delete_btn) {

                selectedGoal = new GoalModal();
                selectedGoal = goalModal;
                showPopup(delete_btn);


            }

            @Override
            public void onEHisClick(StatusDB statusDB, ImageButton edit_btn) {

            }

            @Override
            public void onDHisClick(StatusDB statusDBl, ImageButton delete_btn) {

            }

            @Override
            public void onCClick(GoalModal goalModal, ImageButton active_btn) {
                selectedGoal = new GoalModal();
                selectedGoal = goalModal;
                    default_goal.setText(selectedGoal.getActSelect());
                    default_step.setText(selectedGoal.getStepSelect());
                saveData();
            }


        };


    private final ActiveButtonListener activeButtonListener = new ActiveButtonListener() {
        @Override
        public void onActBtn(GoalModal golModal, ImageButton active_btn) {



            Bundle result = new Bundle();
            result.putString("bundleKey", golModal.getActSelect());
            result.putString("bundleKeys", golModal.getStepSelect());
            result.putInt("bundle_id", golModal.getId());
            getParentFragmentManager().setFragmentResult("requestKey", result);


          /* Fragment frag_m  = StatusFragment.newInstance(golModal.getActSelect(),golModal.getStepSelect());



            FragmentTransaction transact = getActivity().getSupportFragmentManager().beginTransaction();
            transact.replace(R.id.frameLayout, frag_m, "status_fragment");
            transact.addToBackStack(null);
            transact.commit();*/


        }
    };

    private void showPopup(ImageButton delete_btn) {

        PopupMenu popupMenu =  new PopupMenu(getActivity(), delete_btn);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.rv_menu_delete_popup);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                gDatabase.daoAccess().delete(selectedGoal);
                gModal.remove(selectedGoal);
                goalListAdapter.notifyDataSetChanged();
                Toast.makeText( getActivity(),"Goal Deleted",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }


   /*@Override
    public void onActBtn(GoalModal golModal) {



    }*/



    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);

        }
    }






}
