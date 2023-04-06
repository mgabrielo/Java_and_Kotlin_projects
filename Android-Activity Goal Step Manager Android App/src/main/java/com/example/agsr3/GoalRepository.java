package com.example.agsr3;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GoalRepository {
/*
    private  Dao dao;
    private  LiveData<List<GoalModal>> allGoals;

    public GoalRepository(Application application) {

        GoalDatabase database = GoalDatabase.getInstance(application);
        dao = (Dao) database.Dao();
        allGoals = dao.getAllGoals();
    }

    public void insert(GoalModal modal){
        new InsertGoalAsyncTask(dao).execute(modal);
    }

    public void update(GoalModal modal){
        new UpdateGoalAsyncTask(dao).execute(modal);
    }

    public void delete(GoalModal modal){
        new DeleteGoalAsyncTask(dao).execute(modal);
    }

    public void deleteAllGoals(){
        new DeleteAllGoalAsyncTask(dao).execute();
    }

    public LiveData<List<GoalModal>> getAllGoals() {
        return allGoals;
    }

    private static class InsertGoalAsyncTask extends AsyncTask<GoalModal, Void, Void>{
        private Dao dao;
        private InsertGoalAsyncTask(Dao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GoalModal... modals) {
            // below line is use to insert our modal in dao.
            dao.insert(modals[0]);
            return null;
        }

    }


    private static class UpdateGoalAsyncTask extends AsyncTask<GoalModal, Void, Void> {
        private Dao dao;

        private UpdateGoalAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GoalModal... modals) {
            // below line is use to update
            // our modal in dao.
            dao.update(modals[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteGoalAsyncTask extends AsyncTask<GoalModal, Void, Void> {
        private Dao dao;

        private DeleteGoalAsyncTask (Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GoalModal... modals) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(modals[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllGoalAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllGoalAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            dao.deleteAllGoals();
            return null;
        }
    }

 */
}
