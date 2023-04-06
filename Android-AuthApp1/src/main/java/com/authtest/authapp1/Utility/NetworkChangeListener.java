package com.authtest.authapp1.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.authtest.authapp1.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetworkChangeListener extends BroadcastReceiver {


    private View netChangeView;


    @Override
    public void onReceive(Context context, Intent intent) {
        if(!ConnectReceiver.isInternetAvailable(context)){
                snackOn(netChangeView);

            Snackbar snackbar =  Snackbar.make(netChangeView, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

            snackbar.setDuration(20000);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    if(ConnectReceiver.itsOnline(context)){
                        Snackbar snack_bar_2 =  Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                        snack_bar_2.setDuration(4000);
                        snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snack_bar_2.show();
                    }else {
                        onReceive(context, intent);
                    }
                }
            });

            snackbar.show();
        }else{
            System.out.println("you are connected");
        }

    }


    public void snackOn( View view){
        netChangeView = view;

    }




}
