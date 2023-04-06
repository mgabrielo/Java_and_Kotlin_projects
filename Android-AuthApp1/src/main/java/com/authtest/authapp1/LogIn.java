package com.authtest.authapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.authtest.authapp1.AuthData.AuthAppDb;
import com.authtest.authapp1.AuthData.AuthAppModal;
import com.authtest.authapp1.AuthRegString.HashRegString;
import com.authtest.authapp1.AuthType.PhoneUpdate;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;


public class LogIn extends AppCompatActivity {

    private static String DEEP_LINK = "https://authapp1-85cbd.web.app/firebasedude";
    String d_log_email= "text1";
    String  d_log_password = "text2";
    String  d_log_link = "text3";
    EditText email_login;
    Button log_Next_btn, Register_redirect, prePhoneBtn;
    TextView Register_txt;
    ProgressBar progress_Login;
    FirebaseAuth f_newAuth;
    FirebaseUser log_fire_user;
    CountryCodePicker countryPhone;
    FirebaseFirestore f_new_store;
    String log_permission,email,ActionMail, phoneUpVerify,emailLink,AuthGenP,prePhone,code_m,mailer;
    AuthAppDb logAuthDb;
    AuthAppModal logAuthModal;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ConstraintLayout logViewer;
    View getAppView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        logViewer =  findViewById(R.id.login_view);
        getAppView = logViewer;
        f_newAuth = FirebaseAuth.getInstance();
        log_fire_user = f_newAuth.getCurrentUser();
        logAuthDb = AuthAppDb.getInstance(getApplicationContext());
        logAuthModal = new AuthAppModal("abc@email.com","+123456789");
        String phone_value = logAuthDb.authAppDAO().getUserPhone("abc@email.com");
        logAuthDb.authAppDAO().insert(logAuthModal);

        callSetUpDynamicLink();
        Intent allow = getIntent();
        log_permission = allow.getStringExtra("LogBack");
        phoneUpVerify = allow.getStringExtra("phoneUpSuccess");

        if(TextUtils.isEmpty(log_permission)){
            LoadData();
        }else{

            startActivity(new Intent(getApplicationContext(), LogIn.class));
        }


        if(!TextUtils.isEmpty(phoneUpVerify)){
            Snackbar snackPhoneSuccess =  Snackbar.make(getAppView, "Phone Update Successful, You can attempt LogIn", BaseTransientBottomBar.LENGTH_LONG);
            snackPhoneSuccess.setDuration(4000);

            snackPhoneSuccess.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackPhoneSuccess.show();
        }

        email_login = findViewById(R.id.the_email_Login);
        progress_Login = findViewById(R.id.login_progress);
        String emailLog = email_login.getText().toString();
        if(emailLog.isEmpty()){
            progress_Login.setVisibility(View.GONE);
        }
        log_Next_btn = findViewById(R.id.Login_Next_btn);
        Register_redirect = findViewById(R.id.Register_redirect);
        prePhoneBtn = findViewById(R.id.update_prevPhone);
        Register_txt = findViewById(R.id.register_text);
        progress_Login.setVisibility(View.GONE);

        f_new_store = FirebaseFirestore.getInstance();





        log_Next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress_Login.setVisibility(View.VISIBLE);
                if(!ConnectReceiver.itsOnline(getApplicationContext())){
                    Toast.makeText(LogIn.this, "Not Online", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);
                    progress_Login.setVisibility(View.GONE);
                    snackbar.setDuration(20000);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            progress_Login.setVisibility(View.VISIBLE);
                            reConNet();
                            progress_Login.setVisibility(View.GONE);
                            if(ConnectReceiver.itsOnline(getApplicationContext())){
                                progress_Login.setVisibility(View.GONE);
                                Snackbar snack_bar_2 =  Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                                snack_bar_2.setDuration(4000);
                                snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                snack_bar_2.show();
                            }
                        }
                    });

                    snackbar.show();
                }else{
                    progress_Login.setVisibility(View.GONE);
                    String m_Email_login = email_login.getText().toString().trim();

                    if (TextUtils.isEmpty(m_Email_login)) {
                        email_login.setError("Email is required");
                        return;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(m_Email_login).matches()) {
                        email_login.setError("Invalid Format");
                        return;
                    } else {
                        email_login.setError(null);
                    }
                    progress_Login.setVisibility(View.VISIBLE);
                    //login user
                    userInformation();
                    progress_Login.setVisibility(View.GONE);

                }

            }
        });
        LoadData();
        UpdateData();


        Register_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });



    }



    private void userInformation() {
        progress_Login.setVisibility(View.VISIBLE);

        email = email_login.getText().toString().trim();
        f_newAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    progress_Login.setVisibility(View.GONE);
                    Toast.makeText(LogIn.this, "This Email is not Registered to an Account", Toast.LENGTH_SHORT).show();
                }else {
                    progress_Login.setVisibility(View.VISIBLE);

                    // Toast.makeText(LogIn.this, " User exist, Please enter Phone Number", Toast.LENGTH_SHORT).show();
                    savData();
                    logAuthDb = AuthAppDb.getInstance(getApplicationContext());
                    String userPhoneNum = logAuthDb.authAppDAO().getUserPhone(email);

                    if (!TextUtils.isEmpty(userPhoneNum)){
                        String mail_link = d_log_link;
                        Intent pass_to_otp = new Intent(getApplicationContext(),OTP_Code.class);
                        pass_to_otp.putExtra("phone_otp",userPhoneNum);
                        pass_to_otp.putExtra("email_otp",email);
                        pass_to_otp.putExtra("link_otp",mail_link);
                        startActivity(pass_to_otp);

                    }else {
                        Intent send_to_otp = new Intent(getApplicationContext(),OTP.class);
                        send_to_otp.putExtra("email_for_otp",email);
                        startActivity(send_to_otp);
                        // startActivity(new Intent(getApplicationContext(), OTP.class));
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                progress_Login.setVisibility(View.GONE);
            }
        });

    }


    private void savData() {
        SharedPreferences sharedPrefer = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPrefer.edit();
        edit.putString("task list 12", email_login.getText().toString());
        edit.putString("task list 14", emailLink);
        edit.apply();
    }

    private void reConNet(){
        networkChangeListener.snackOn(getAppView);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
    }


    private void LoadData() {
        SharedPreferences sharedPrefer = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        d_log_email = sharedPrefer.getString("task list 12", "");
        d_log_password = sharedPrefer.getString("task list 13", "");
        d_log_link =     sharedPrefer.getString("task list 14", "");

    }

    private void UpdateData() {
        email_login.setText(d_log_email);
        //password_login.setText(d_log_password);
    }


    @Override
    protected void onStart() {
        networkChangeListener.snackOn(getAppView);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


    public void logBtn_to_EmailAuthLink(View view) {

        if(!ConnectReceiver.itsOnline(getApplicationContext())){
            Toast.makeText(LogIn.this, "Not Online", Toast.LENGTH_SHORT).show();
            Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

            snackbar.setDuration(20000);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    reConNet();
                    if(ConnectReceiver.itsOnline(getApplicationContext())){
                        Snackbar snack_bar_2 =  Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                        snack_bar_2.setDuration(4000);
                        snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snack_bar_2.show();
                    }
                }
            });

            snackbar.show();
        }else {


            ActionMail = email_login.getText().toString().trim();
            if (TextUtils.isEmpty(ActionMail)) {
                email_login.setError("Email is required");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(ActionMail).matches()) {
                email_login.setError("Invalid Format");
                return;
            } else {
                email_login.setError(null);
            }

            f_newAuth.fetchSignInMethodsForEmail(ActionMail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.getResult().getSignInMethods().size() == 0) {
                        Toast.makeText(LogIn.this, "This Email is not Registered to an Account", Toast.LENGTH_SHORT).show();
                    } else {
                        checker_mail();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });


            savData();

        }

    }

    private void callSetUpDynamicLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Uri deepLink = null;
                if(pendingDynamicLinkData != null){
                    deepLink = pendingDynamicLinkData.getLink();
                }
                Log.d("DeepDynamic", "callGetDynamicLink, deepLink " + deepLink);

                handleSuccess();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DeepDynamicErr", "getDynamicLink:onFailure", e);

            }
        });
    }

    private void checker_mail() {
        progress_Login.setVisibility(View.VISIBLE);
        f_newAuth.fetchSignInMethodsForEmail(ActionMail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    progress_Login.setVisibility(View.GONE);
                    Toast.makeText(LogIn.this, " User Does Not Exist, Please Register", Toast.LENGTH_SHORT).show();
                }else {
                    progress_Login.setVisibility(View.VISIBLE);
                    ActionCodeSettings codeSettings = ActionCodeSettings.newBuilder().setUrl(DEEP_LINK)
                            .setHandleCodeInApp(true).setAndroidPackageName(
                                    "com.example.authapp1",
                                    true,
                                    "1").setDynamicLinkDomain("authapp1.page.link").build();



                    f_newAuth.sendSignInLinkToEmail(ActionMail,codeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            boolean emailWasSent = task.isSuccessful();
                            if (emailWasSent) {
                                progress_Login.setVisibility(View.GONE);
                                Toast.makeText(LogIn.this, "Verification Link is sent to Your Email", Toast.LENGTH_SHORT).show();
                            }else{
                                Exception e = task.getException();
                                progress_Login.setVisibility(View.GONE);
                                Toast.makeText(LogIn.this, "EMail NOT sent out" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }






    private void handleSuccess() {

        Intent intent = getIntent();

        if (intent == null || intent.getData() == null)

            return;
        emailLink = intent.getData().toString();

        if(f_newAuth.isSignInWithEmailLink(emailLink)){
            progress_Login.setVisibility(View.VISIBLE);
             mailer = d_log_email;

            AuthAppDb logDb =AuthAppDb.getInstance(getApplicationContext());
            String phoning = logDb.authAppDAO().getUserPhone(mailer);
            if(!TextUtils.isEmpty(phoning)){
            String new_gen_pass =  phoning.substring(phoning.length() - 10);
            AuthGenP = HashRegString.main(new_gen_pass);

            System.out.println("mailer: "+mailer +" "+ "AuthGenP : "+ AuthGenP);

                f_newAuth.signInWithEmailAndPassword(mailer, AuthGenP).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress_Login.setVisibility(View.GONE);

                            FirebaseUser fireBasePhone = f_newAuth.getCurrentUser();
                            if(fireBasePhone != null) {
                                String email_sender = email_login.getText().toString().trim();
                                String fireBasePhone_id = f_newAuth.getCurrentUser().getUid();
                                Intent pass_to_phoneUpdate= new Intent(getApplicationContext(), PhoneUpdate.class);
                                pass_to_phoneUpdate.putExtra("phoneUp_id",fireBasePhone_id);
                                pass_to_phoneUpdate.putExtra("phoneUp_email",email_sender);
                                startActivity(pass_to_phoneUpdate);
                            }else{
                                Toast.makeText(LogIn.this, "Invalid Details Try Again", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LogIn.class));
                            }

                    }
                });
            }else{
                progress_Login.setVisibility(View.GONE);
                 getPhoneDialog();
            }

        }else{
            Toast.makeText(LogIn.this, "Invalid Link Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPhoneDialog() {
        EditText pre_edt_phone;
        CountryCodePicker pre_code_pick;
        TextView pre_Cancel_Txt;
        Button pre_Button_Sub;

        ViewGroup viewGroup = findViewById(android.R.id.content);
        AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
        View prev_view = LayoutInflater.from(LogIn.this).inflate(R.layout.previous_phone_dialog, viewGroup,false);
        builder.setCancelable(false);
        builder.setView(prev_view);

        //prev_view.setBackgroundColor(R.color.teal_700);

        pre_Button_Sub = prev_view.findViewById(R.id.prev_btn_submit);
        pre_Cancel_Txt = prev_view.findViewById(R.id.prev_btn_cancel);
        pre_code_pick = prev_view.findViewById(R.id.prev_country_code);
        pre_edt_phone = prev_view.findViewById(R.id.prev_edtTXt);
        AlertDialog alertDialog = builder.create();
        pre_Button_Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ConnectReceiver.itsOnline(getApplicationContext())){
                    Toast.makeText(LogIn.this, "Not Online", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

                    snackbar.setDuration(20000);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            reConNet();
                            if(ConnectReceiver.itsOnline(getApplicationContext())){
                                Snackbar snack_bar_2 =  Snackbar.make(view, "You Are Connected ", BaseTransientBottomBar.LENGTH_LONG);
                                snack_bar_2.setDuration(4000);
                                snack_bar_2.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                snack_bar_2.show();
                            }
                        }
                    });

                    snackbar.show();
                }else {


                    String prove_phone = pre_edt_phone.getText().toString();
                    // Log.d("onclick","editext value is: "+ editTextInput);
                    if (TextUtils.isEmpty(prove_phone)) {
                        pre_edt_phone.setError("please insert phone number");
                    } else if (prove_phone.length() < 10) {
                        pre_edt_phone.setError("Phone number incomplete");
                        Toast.makeText(LogIn.this, "Phone number incomplete", Toast.LENGTH_SHORT).show();
                    } else if (prove_phone.length() > 11) {
                        pre_edt_phone.setError("Phone number invalid");
                        Toast.makeText(LogIn.this, "Phone number invalid", Toast.LENGTH_SHORT).show();
                    } else if (prove_phone.length() == 11 || prove_phone.length() == 10) {
                        String get_pre_phone = pre_edt_phone.getText().toString().trim();


                        if (!TextUtils.isEmpty(prePhone) && prove_phone.length() == 11 ) {
                            prePhone = get_pre_phone.substring(1);
                            code_m = "+" + pre_code_pick.getSelectedCountryCode() + prePhone;
                            Toast.makeText(LogIn.this, "This: " + code_m, Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(prePhone) &&  prove_phone.length() == 10 ) {

                            code_m = "+" + pre_code_pick.getSelectedCountryCode() + prove_phone;
                            Toast.makeText(LogIn.this, "This: " + code_m, Toast.LENGTH_SHORT).show();
                        }


                        String prev_gen_pass = code_m.substring(code_m.length() - 10);
                        AuthGenP = HashRegString.main(prev_gen_pass);

                        f_newAuth.signInWithEmailAndPassword(mailer,AuthGenP).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    FirebaseUser fireBasePhone = f_newAuth.getCurrentUser();
                                    if (fireBasePhone != null) {
                                        String email_sender = email_login.getText().toString().trim();
                                        String fireBasePhone_id = f_newAuth.getCurrentUser().getUid();
                                        Intent pass_to_phoneUpdate = new Intent(getApplicationContext(), PhoneUpdate.class);
                                        pass_to_phoneUpdate.putExtra("phoneUp_id", fireBasePhone_id);
                                        pass_to_phoneUpdate.putExtra("phoneUp_email", email_sender);
                                        pass_to_phoneUpdate.putExtra("phoneUp_link", emailLink);
                                        startActivity(pass_to_phoneUpdate);
                                    } else {
                                        Toast.makeText(LogIn.this, "Invalid Details Try Again", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LogIn.class));
                                    }
                                
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("vent", "onClick:  what is here" + e);
                            }
                        });


                    } else {
                        pre_edt_phone.setError(null);
                    }

                }

            }
        });

        pre_Cancel_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }



}