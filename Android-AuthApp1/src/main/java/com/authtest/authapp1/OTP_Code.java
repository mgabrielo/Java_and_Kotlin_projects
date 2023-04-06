package com.authtest.authapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.authtest.authapp1.AuthData.AuthAppDb;
import com.authtest.authapp1.AuthData.AuthAppModal;
import com.authtest.authapp1.AuthRegString.HashRegString;
import com.authtest.authapp1.AuthType.EmojiAuths;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP_Code extends AppCompatActivity {


    LinearLayout otp_linear;
    Button Submit_Btn,Resend_otp_btn;
    TextView auth_txt;
    FirebaseAuth fire_code_Auth;
   // PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken the_token;
    String codeBySystem;
    String phone_from_verify,email_verify,user_iD;
    PinView u_pinView;
    String u_code;
    AuthAppModal otpAuthModal;
    AuthAppDb otpAuthDb;
    String emailSave = "emails";
    String phoneSave = "phones";
    String pines, permission;
    NetworkChangeListener otpCodeNetworkChangeListener= new NetworkChangeListener();
    ConstraintLayout otpConstrain;
    View getAppView_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_code);
        fire_code_Auth = FirebaseAuth.getInstance();
        Intent emj_allow = getIntent();
        permission = emj_allow.getStringExtra("permit");
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        u_pinView = findViewById(R.id.pinCodeView);
        otpConstrain =  findViewById(R.id.otp_constraint);
        otp_linear = findViewById(R.id.otp_linear);
        otp_linear.setVisibility(View.INVISIBLE);
        Intent data_phone_otp = getIntent();
        email_verify =data_phone_otp.getStringExtra("email_otp");
        phone_from_verify =data_phone_otp.getStringExtra("phone_otp");
        Log.d("phone_print", "onCreate: " + phone_from_verify);
        auth_txt =  findViewById(R.id.otp_auth_txt);
        Submit_Btn = findViewById(R.id.auth_send_btn);
        Resend_otp_btn = findViewById(R.id.otp_resend);
         pines = u_pinView.getText().toString();
         getAppView_1 = otpConstrain;
         Submit_Btn.setEnabled(false);





        if(TextUtils.isEmpty(permission)){
            u_pinView.setText("");
            send_verification_to_user(phone_from_verify);
            Submit_Btn.setEnabled(true);
             savingData();
            LoadData();
            UpdateData();
            Submit_Btn.setVisibility(View.VISIBLE);
            Resend_otp_btn.setVisibility(View.VISIBLE);
        }else if(!TextUtils.isEmpty(permission)){

            Submit_Btn.setEnabled(true);
            //startActivity(new Intent(getApplicationContext(), OTP_Code.class));
            u_pinView.setText("");
            emailSave= email_verify;
            phoneSave = phone_from_verify;
            Submit_Btn.setVisibility(View.GONE);
            Resend_otp_btn.setVisibility(View.VISIBLE);
        }else{
            startActivity(new Intent(getApplicationContext(), LogIn.class));
        }



        Resend_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Submit_Btn.setEnabled(true);
                u_pinView.setText("");
                String pinCheck = u_pinView.getText().toString();

                if (pinCheck.isEmpty() && !TextUtils.isEmpty(permission)) {
                    u_pinView.setError("please enter otp code");
                    LoadData();
                    UpdateData();
                    checkNetWk();
                    resend_verification_to_user(phone_from_verify);
                    Submit_Btn.setVisibility(View.VISIBLE);

                } else if (!pinCheck.isEmpty() && !TextUtils.isEmpty(permission)) {
                    LoadData();
                    UpdateData();
                    checkNetWk();
                    resend_verification_to_user(phone_from_verify);
                    Submit_Btn.setVisibility(View.VISIBLE);
                } else {
                    if (pinCheck.isEmpty() && TextUtils.isEmpty(permission)) {
                        checkNetWk();
                        resend_verification_to_user(phone_from_verify);
                    } else if (!pinCheck.isEmpty() && TextUtils.isEmpty(permission)) {
                        checkNetWk();
                        resend_verification_to_user(phone_from_verify);
                    }else{
                        startActivity(new Intent(getApplicationContext(), LogIn.class));
                    }
                }
                Toast.makeText(OTP_Code.this, "Code Resend Complete", Toast.LENGTH_SHORT).show();


            }
        });

      // LoadData();
      //  UpdateData();


    }

    private void checkNetWk() {
        if (!ConnectReceiver.itsOnline(getApplicationContext())) {

            Snackbar snackbar = Snackbar.make(getAppView_1, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

            snackbar.setDuration(20000);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    reConNet_1();
                    if (ConnectReceiver.itsOnline(getApplicationContext())) {
                        Snackbar snack_bar_2 = Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                        snack_bar_2.setDuration(4000);
                        snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snack_bar_2.show();
                    }
                }
            });

            snackbar.show();
        }else{

            Log.d("checkNet", "checkNetWk: in progress");
        }
    }

    private void send_verification_to_user(String phone_from_verify) {


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fire_code_Auth)
                        .setPhoneNumber(phone_from_verify)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(uCallbacks)         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void resend_verification_to_user(String phone_from_verify) {



        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fire_code_Auth)
                        .setPhoneNumber(phone_from_verify)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(uCallbacks).setForceResendingToken(the_token)  // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks uCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem =s;
                    the_token = forceResendingToken;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    u_code = phoneAuthCredential.getSmsCode();
                    if(u_code != null){
                        u_pinView.setText(u_code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OTP_Code.this, "verification failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verify_code(String u_code) {
        if(!codeBySystem.isEmpty()) {
            PhoneAuthCredential u_credential = PhoneAuthProvider.getCredential(codeBySystem, u_code);
            u_signInUsingCredential(u_credential);
        }else{
            Toast.makeText(this, "Verification code unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void u_signInUsingCredential(PhoneAuthCredential u_credential) {

        fire_code_Auth.signInWithCredential(u_credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            otp_linear.setVisibility(View.VISIBLE);

                            String new_gen_pass =  phone_from_verify.substring(phone_from_verify.length() - 10);
                            String Auto_GP = HashRegString.main(new_gen_pass);
                            fire_code_Auth.signInWithEmailAndPassword(email_verify,Auto_GP).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){


                                        otpAuthDb = AuthAppDb.getInstance(getApplicationContext());
                                        otpAuthModal = new AuthAppModal(email_verify,phone_from_verify);
                                        String phone_value = otpAuthDb.authAppDAO().getUserPhone(email_verify);
                                        if(TextUtils.isEmpty(phone_value)){
                                            otpAuthDb.authAppDAO().insert(otpAuthModal);
                                        }else{
                                            otpAuthDb.authAppDAO().update(phone_from_verify,email_verify);
                                        }
                                        Toast.makeText(OTP_Code.this, "verification success", Toast.LENGTH_SHORT).show();
                                         LoadData();
                                         UpdateData();
                                        startActivity(new Intent(getApplicationContext(), EmojiAuths.class));
                                        otp_linear.setVisibility(View.GONE);

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    otp_linear.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Please Try again: " + e, Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            Resend_otp_btn.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Submit_Btn.setVisibility(View.VISIBLE);
                                Resend_otp_btn.setVisibility(View.VISIBLE);
                                Toast.makeText(OTP_Code.this, "verification failed", Toast.LENGTH_SHORT).show();
                                Log.d("error_tag", "on_failed:  verification failed");
                            }
                        }
                    }
                });

    }

    private void savingData() {
        SharedPreferences sharedPrefer = getApplicationContext().getSharedPreferences("sharing", MODE_PRIVATE);
        SharedPreferences.Editor edition = sharedPrefer.edit();
        edition.putString("task list 21", phone_from_verify);
        edition.putString("task list 22", email_verify);

        edition.apply();


    }



    private void LoadData() {
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("sharing", MODE_PRIVATE);
        phoneSave = sharedPrefs.getString("task list 21", "");
        emailSave = sharedPrefs.getString("task list 22", "");


    }

    private void UpdateData() {
        phone_from_verify = phoneSave;
       email_verify = emailSave;
    }



    public void callNextScreenFromOTP(View view) {
        if(!ConnectReceiver.itsOnline(getApplicationContext())) {

            Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

            snackbar.setDuration(20000);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    reConNet_1();
                    if(ConnectReceiver.itsOnline(getApplicationContext())){
                        Snackbar snack_bar_2 =  Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                        snack_bar_2.setDuration(4000);
                        snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snack_bar_2.show();
                    }
                }
            });

            snackbar.show();
        }else{
            otp_linear.setVisibility(View.VISIBLE);
            String t_code = u_pinView.getText().toString().trim();
            if (TextUtils.isEmpty(t_code)) {
                u_pinView.setError("please insert pin code");
                Resend_otp_btn.setVisibility(View.VISIBLE);
            } else if (t_code.length() < 6){
                u_pinView.setError("Incomplete OTP Code");
            }else {
                u_pinView.setError(null);
                otp_linear.setVisibility(View.VISIBLE);
                Submit_Btn.setVisibility(View.GONE);
                verify_code(t_code);
                savingData();
            }
        }
    }

    private void reConNet_1() {
        otpCodeNetworkChangeListener.snackOn(getAppView_1);
        IntentFilter intentFilFill_1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver( otpCodeNetworkChangeListener,intentFilFill_1);
    }


    public void otpCode_button_to_logInScreen(View view) {

        String BackLog = "AllowLog";
        Intent pass_otp_log = new Intent(getApplicationContext(),LogIn.class);
        pass_otp_log.putExtra("LogBack", BackLog);
        startActivity(pass_otp_log);
    }

    @Override
    public void onBackPressed() {
        otpCode_button_to_logInScreen(getCurrentFocus());
    }


    @Override
    protected void onStart() {
        otpCodeNetworkChangeListener.snackOn(getAppView_1);
        IntentFilter intentFilFill_1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(otpCodeNetworkChangeListener,intentFilFill_1);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(otpCodeNetworkChangeListener);
        super.onStop();
    }
}

