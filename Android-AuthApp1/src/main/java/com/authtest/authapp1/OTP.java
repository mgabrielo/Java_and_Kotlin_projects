package com.authtest.authapp1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    public static final String TAG = "TAGS";
    EditText Verify_Phone_Edt;
    TextView otp_txt;
    Button Verify_Send;
    ProgressBar otp_progress;
    LinearLayout otp_num_progress;
    CountryCodePicker codePicks;
    FirebaseAuth fire_verify_Auth;
    FirebaseFirestore fire_verify_store;
    String  the_email, my_phone,code_z;
    NetworkChangeListener otpNetworkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        Verify_Phone_Edt = findViewById(R.id.v_phone);
        otp_num_progress = findViewById(R.id.otp_num_progress);
        String get_phone = Verify_Phone_Edt.getText().toString().trim();
        if(get_phone.isEmpty()){
            otp_num_progress.setVisibility(View.GONE);
        }
        Verify_Send = findViewById(R.id.verify_send_btn);
        codePicks = findViewById(R.id.country_code_send);
        otp_progress = findViewById(R.id.otp_progress);
        otp_txt = findViewById(R.id.otp_sending_txt);

        otp_num_progress.setVisibility(View.GONE);
        fire_verify_Auth = FirebaseAuth.getInstance();
        fire_verify_store = FirebaseFirestore.getInstance();


        Intent data_email = getIntent();
        the_email = data_email.getStringExtra("email_for_otp");

        Verify_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String con_ver_phone = Verify_Phone_Edt.getText().toString().trim();
                    if(TextUtils.isEmpty(con_ver_phone) ){
                        Verify_Phone_Edt.setError("please insert phone number");
                    }else if(con_ver_phone.length() < 10){
                        Verify_Phone_Edt.setError("Phone number incomplete");
                        Toast.makeText(OTP.this, "Phone number incomplete", Toast.LENGTH_SHORT).show();
                    }else if(con_ver_phone.length() > 11){
                        Verify_Phone_Edt.setError("Phone number invalid");
                        Toast.makeText(OTP.this, "Phone number invalid", Toast.LENGTH_SHORT).show();

                    } else if (con_ver_phone.length() == 11 ){
                        String your_phone = Verify_Phone_Edt.getText().toString().trim();
                        my_phone =   your_phone.substring(1);
                        Toast.makeText(OTP.this, "new phone: "+my_phone, Toast.LENGTH_SHORT).show();
                        code_z   = "+" + codePicks.getSelectedCountryCode() +  my_phone;
                        otp_num_progress.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onClick: Phone Number is -->" + code_z);
                        Intent pass_num_otp = new Intent(getApplicationContext(),OTP_Code.class);
                        pass_num_otp.putExtra("phone_otp", code_z);
                        pass_num_otp.putExtra("email_otp", the_email);
                        startActivity(pass_num_otp);
                    }else if(con_ver_phone.length() == 10) {
                        otp_num_progress.setVisibility(View.VISIBLE);
                        code_z = "+" + codePicks.getSelectedCountryCode()+ con_ver_phone;
                        Toast.makeText(OTP.this, "new phone: "+ code_z, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: Phone Number is -->" + code_z);
                        Intent pass_num_otp = new Intent(getApplicationContext(),OTP_Code.class);
                        pass_num_otp.putExtra("phone_otp", code_z);
                        pass_num_otp.putExtra("email_otp", the_email);
                        startActivity(pass_num_otp);
                    }



                    }


        });

    }


    public void otp_button_to_logInScreen(View view){
        startActivity(new Intent(getApplicationContext(),LogIn.class));
    }

    @Override
    public void onBackPressed() {
        otp_button_to_logInScreen(getCurrentFocus());
    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(otpNetworkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(otpNetworkChangeListener);
        super.onStop();
    }

}