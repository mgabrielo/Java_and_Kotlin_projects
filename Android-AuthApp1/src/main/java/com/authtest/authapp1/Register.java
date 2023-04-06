package com.authtest.authapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.EmojiSpan;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.text.Spannable;
import android.text.TextUtils;

import android.util.Log;
import android.util.Patterns;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import com.authtest.authapp1.AuthData.AuthAppDb;
import com.authtest.authapp1.AuthData.AuthAppModal;
import com.authtest.authapp1.AuthEmojiFilter.CustomEditText;
import com.authtest.authapp1.AuthRegString.HashRegString;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
;

import java.util.regex.Pattern;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class Register extends AppCompatActivity {

    private static final Pattern password_pattern =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{5,}" +               //at least 5 characters
                    "$"
            );

    public static final String TAG = "TAG";
    EditText First_name,Last_name,the_Email,the_Phone;
    AppCompatEditText the_Password,confirm_password;
    Button Register_Btn, Log_redirect;
    TextView Login_Txt;
    FirebaseAuth f_Auth;
    FirebaseFirestore f_store;
    LinearLayout reg_linear;
    AwesomeValidation awesomeValidation;
    String userID;
    String code_x;
    String emojiUTF_hex;
    String AutoGenPass;
    int count_pwd_emoji;
    String main_phone;
    CountryCodePicker countryCodePicker;
    NetworkChangeListener regNetworkChangeListener = new NetworkChangeListener();
    View getAppView_3;

    CoordinatorLayout reg_con;
    AuthAppModal authAppModal;
    AuthAppDb authAppDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        First_name = findViewById(R.id.first_name);
        Last_name = findViewById(R.id.last_name);
        the_Email = findViewById(R.id.the_email);
        the_Phone = findViewById(R.id.user_phone);
        countryCodePicker = findViewById(R.id.country_codex);
        the_Password = findViewById(R.id.the_password);
        confirm_password =  findViewById(R.id.confirm_the_password);
        Register_Btn =  findViewById(R.id.register_btn);
        Log_redirect =  findViewById(R.id.Log_Redirect);
        Login_Txt = findViewById(R.id.login_text);
        reg_con = findViewById(R.id.reg_con);
        getAppView_3 =reg_con;

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        config.setReplaceAll(true);
        EmojiCompat.init(config);

        CustomEditText customRegEditText = new CustomEditText(this);
        customRegEditText.init(the_Password);
        customRegEditText.init(confirm_password);

        f_Auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        reg_linear = findViewById(R.id.register_linear);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
       // awesomeValidation.addValidation(this,R.id.the_password,".{6,}", R.string.wrongpassword);
        awesomeValidation.addValidation(this,R.id.the_password,R.id.confirm_the_password,R.string.passwordincorrect);


        Register_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ConnectReceiver.itsOnline(getApplicationContext())){
                    Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

                    snackbar.setDuration(20000);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            reConNet_3();
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
                    registerProcess();
                }

            }
        });



        Log_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));
            }
        });
    }








    private void registerProcess() {
        String Fst_name = First_name.getText().toString().trim();
        String Lst_name = Last_name.getText().toString().trim();
        String m_Email = the_Email.getText().toString().trim();
        String phone_num = the_Phone.getText().toString().trim();
        String m_Password =the_Password.getText().toString().trim();
        String con_pass = confirm_password.getText().toString().trim();
        String blockSet2 = "★☆○●□■♤♠♡♥◇◆♧♣▪";

        String eCovert = the_Password.getText().toString();
        String unicodeHexEmoji = "U+";
        StringBuilder sb = new StringBuilder();
        byte[] utf16Bytes = eCovert.getBytes(StandardCharsets.UTF_16);
        for (byte b : utf16Bytes ) {
            sb.append(String.format("%02x", b));
        }

        unicodeHexEmoji += sb;
        byte[] utfHexBytes = getByteFromHex(unicodeHexEmoji.replace("U+",""));

        emojiUTF_hex = new String(utfHexBytes, StandardCharsets.UTF_16);


        if(TextUtils.isEmpty(Fst_name)){

            return;
        }else{
            First_name.setError(null);
        }

        if(TextUtils.isEmpty(Lst_name)){
            Last_name.setError("Name field is required");
            return;
        }else{
            Last_name.setError(null);
        }

        if(TextUtils.isEmpty(m_Email)){
            the_Email.setError("Email is required");
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(m_Email).matches()){
            the_Email.setError("Please Enter Valid Email");
            return;
        }else{
            the_Email.setError(null);
        }

        if(TextUtils.isEmpty(phone_num)){
            the_Phone.setError("Phone number is required");
            Toast.makeText(Register.this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }else if(phone_num.length() < 10){
            the_Phone.setError("Phone number incomplete");
            Toast.makeText(Register.this, "Phone number incomplete", Toast.LENGTH_SHORT).show();
            return;
        } else if(phone_num.length() > 11){
            the_Phone.setError("Phone number invalid");
            Toast.makeText(Register.this, "Phone number invalid", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone_num.length() == 11) {
            String u_phone = the_Phone.getText().toString().trim();
            main_phone =  u_phone.substring(1);
        }else{
            the_Phone.setError(null);
        }

        count_pwd_emoji = getNewEmojiCount(m_Password);

        if(TextUtils.isEmpty(m_Password)){
            the_Password.setError("Email is required");

        }else if (blockSet2.contains(""+m_Password)) {
            the_Password.setError("character not allowed");
            the_Password.setText("");
            confirm_password.setText("");
            Toast.makeText(Register.this, "Only Emojis Characters Allowed", Toast.LENGTH_SHORT).show();
        }else if(count_pwd_emoji < 4){
            the_Password.setError("Minimum of 4 Emoji Password");
            Toast.makeText(Register.this, "Minimum of 4 Emoji Password", Toast.LENGTH_SHORT).show();
        } else if(count_pwd_emoji > 8){
            the_Password.setError("Maximum of 8 Emoji Password");
            Toast.makeText(Register.this, "Maximum of 8 Emoji Password", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(con_pass) && awesomeValidation.validate()){
            Toast.makeText(Register.this, "form validation successful", Toast.LENGTH_SHORT).show();

            if(!TextUtils.isEmpty(main_phone)) {
                code_x   = "+" + countryCodePicker.getSelectedCountryCode() + main_phone;

            }else{

                code_x = "+" + countryCodePicker.getSelectedCountryCode() + phone_num;
            }

            reg_linear.setVisibility(View.VISIBLE);
            String gen_pass =  code_x.substring(code_x.length() - 10);
            AutoGenPass = HashRegString.main(gen_pass);
            System.out.println("glory" + AutoGenPass);

            //register user

            f_Auth.createUserWithEmailAndPassword(m_Email,AutoGenPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        authAppDb = AuthAppDb.getInstance(getApplicationContext());
                        authAppModal = new AuthAppModal(m_Email,code_x);
                        String p_value = authAppDb.authAppDAO().getUserPhone(m_Email);
                        if(TextUtils.isEmpty(p_value)){
                            authAppDb.authAppDAO().insert(authAppModal);
                        }else{
                            authAppDb.authAppDAO().update(code_x,m_Email);
                        }

                        FirebaseUser fire_base_user = f_Auth.getCurrentUser();

                        Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                        userID= f_Auth.getCurrentUser().getUid();
                        DocumentReference documentReference = f_store.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("first_name",Fst_name);
                        user.put("last_name",Lst_name);
                        user.put("your_email",m_Email);
                        user.put("your_phone",code_x);
                        user.put("your_pass",emojiUTF_hex);
                        user.put("confirm_pass",AutoGenPass);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "OnSuccess: User profile is created for: " + userID );

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "OnFailure: " + e.getMessage());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),LogIn.class));
                        reg_linear.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    reg_linear.setVisibility(View.GONE);
                }
            });

        }else{
            Toast.makeText(Register.this, "form validation incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    private void reConNet_3() {
        regNetworkChangeListener.snackOn(getAppView_3);
        IntentFilter intentFilFill_3 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver( regNetworkChangeListener,intentFilFill_3);
    }

    public byte[] getByteFromHex(String hexString){
        //To convert hex string to byte array, you need to first get the length
        //of the given string and include it while creating a new byte array.
        byte[] val = new byte[hexString.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }

    public static int getNewEmojiCount(CharSequence charSequence) {
        int count = 0;
        CharSequence processed = EmojiCompat.get().process(charSequence, 0, charSequence.length() -1, Integer.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL);
        if (processed instanceof Spannable) {
            Spannable spannable = (Spannable) processed;
            count = spannable.getSpans(0, spannable.length() - 1, EmojiSpan.class).length;
        }
        return count;
    }


    @Override
    protected void onStart() {
        regNetworkChangeListener.snackOn(getAppView_3);
        IntentFilter intentFilFill_3 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(regNetworkChangeListener,intentFilFill_3);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(regNetworkChangeListener);
        super.onStop();
    }


}