package com.authtest.authapp1.AuthType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.EmojiSpan;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.authtest.authapp1.AuthEmojiFilter.CustomEditText;
import com.authtest.authapp1.LogIn;
import com.authtest.authapp1.MainActivity;
import com.authtest.authapp1.OTP;
import com.authtest.authapp1.OTP_Code;
import com.authtest.authapp1.R;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class EmojiAuths extends AppCompatActivity {

    FirebaseAuth fire_Auth_emoji;
    Button submit_emoji_pwd;
    FirebaseFirestore fire_store_emoji;
    String m_userid,pass_mail;
    FirebaseUser fire_user_emoji;
    AppCompatEditText emoji_Pwd;
    TextView emojiText, emoji_view;
    LinearLayout emoji_linear, emoji_linear_two;
    String emojiUTF_hexPwd, emojiUTF_hexTV;
    int count_of_emoji;
    String unicodeHexEmojiPwd;
    String unicodeHexEmojiTxt;
    NetworkChangeListener emojiNetworkChangeListener = new NetworkChangeListener();
    ConstraintLayout emoji_cost;
    View getAppView_2;
    EmojIconActions em_act_1;
    ImageView vet_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_auths);

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        config.setReplaceAll(true);
            EmojiCompat.init(config);
        emoji_cost =findViewById(R.id.emoji_con);
        emoji_Pwd = findViewById(R.id.emoji_password);
        submit_emoji_pwd = findViewById(R.id.emoji_btn_confirm);
        emojiText = findViewById(R.id.emoji_txt);
        emoji_linear = findViewById(R.id.emoji_linear);
        emoji_linear_two = findViewById(R.id.emoji_linear2);
        emoji_linear_two.setVisibility(View.GONE);
        emoji_view = findViewById(R.id.textView3);
        emoji_view.setVisibility(View.GONE);
        CustomEditText customEmjEditText = new CustomEditText(this);
        customEmjEditText.init(emoji_Pwd);
        fire_Auth_emoji= FirebaseAuth.getInstance();
        fire_store_emoji =FirebaseFirestore.getInstance();
        fire_user_emoji = fire_Auth_emoji.getCurrentUser();
        getAppView_2 = emoji_cost;





        String emojiRequest = emojiText.getText().toString();

        if(emojiRequest.isEmpty()){
            emoji_view.setVisibility(View.INVISIBLE);
            emoji_linear.setVisibility(View.VISIBLE);
        }



        if(fire_user_emoji != null) {
            m_userid = fire_Auth_emoji.getCurrentUser().getUid();
            DocumentReference documentRefer = fire_store_emoji.collection("users").document(m_userid);
                documentRefer.addSnapshotListener(this, (value, error) -> {
                    if (value != null ) {
                        emojiText.setText(value.getString("your_pass"));
                        emoji_view.setVisibility(View.VISIBLE);
                        emoji_linear.setVisibility(View.GONE);

                    }else {
                        emoji_view.setVisibility(View.VISIBLE);
                        emoji_linear.setVisibility(View.INVISIBLE);
                    }

                });
        }
        else{
            startActivity(new Intent(getApplicationContext(), LogIn.class));
        }


        submit_emoji_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ConnectReceiver.itsOnline(getApplicationContext())) {
                    Snackbar snackbar =  Snackbar.make(view, "You Are Not Connected To Internet", BaseTransientBottomBar.LENGTH_LONG);

                    snackbar.setDuration(20000);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            reConNet_2();
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
                    emojiProcess();
                }
            }
        });

    }



    private void emojiProcess() {

        String confirmEmjPwd = emoji_Pwd.getText().toString();
        String confirmEmjTv = emojiText.getText().toString();


        unicodeHexEmojiPwd = "U+";
        StringBuilder EmjPwdSb = new StringBuilder();
        byte[] Edt_utf16Bytes = confirmEmjPwd.getBytes(StandardCharsets.UTF_16);
        for (byte a : Edt_utf16Bytes ) {
            EmjPwdSb.append(String.format("%02x", a));
        }

        unicodeHexEmojiPwd += EmjPwdSb;
        byte[] utfHexBytesEmjEdt = getNewByteFromHex(unicodeHexEmojiPwd.replace("U+",""));

        emojiUTF_hexPwd = new String(utfHexBytesEmjEdt, StandardCharsets.UTF_16);


        unicodeHexEmojiTxt = "U+";
        StringBuilder EmjTVsb = new StringBuilder();
        byte[] Tv_utf16Bytes = confirmEmjTv.getBytes(StandardCharsets.UTF_16);
        for (byte c : Tv_utf16Bytes ) {
            EmjTVsb.append(String.format("%02x", c));
        }

        unicodeHexEmojiTxt += EmjTVsb;
        byte[] utfHexBytesEmjTV = getNewByteFromHex(unicodeHexEmojiTxt.replace("U+",""));

        emojiUTF_hexTV = new String(utfHexBytesEmjTV, StandardCharsets.UTF_16);



        String blockSet = "★☆○●□■♤♠♡♥◇◆♧♣▪";
        if(TextUtils.isEmpty(confirmEmjPwd)){
            emoji_Pwd.setError("Emoji Password is required");
            Toast.makeText(EmojiAuths.this, "Emoji Password is required", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(confirmEmjPwd) && !emojiUTF_hexPwd.equals(emojiUTF_hexTV)){
            emoji_Pwd.setError("Emoji Password does not Match");
            Toast.makeText(EmojiAuths.this, "Emoji Password does not Match", Toast.LENGTH_SHORT).show();
            count_of_emoji = getEmojiCount(confirmEmjPwd);
            System.out.println("hollow: "+count_of_emoji);
            if (count_of_emoji < 4){
                emoji_Pwd.setError("Minimum of 4 Emoji Password");
                Toast.makeText(EmojiAuths.this, "Minimum of 4 Emoji Password", Toast.LENGTH_SHORT).show();
            }else if (count_of_emoji > 8) {
                emoji_Pwd.setError("Maximum of 8 Emoji Password");
                Toast.makeText(EmojiAuths.this, "Maximum of 8 Emoji Password", Toast.LENGTH_SHORT).show();
            }
        }else if(emojiUTF_hexPwd.equals(emojiUTF_hexTV)){
            emoji_linear_two.setVisibility(View.VISIBLE);
            submit_emoji_pwd.setVisibility(View.GONE);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            emoji_linear_two.setVisibility(View.GONE);
        }else if (blockSet.contains(""+confirmEmjPwd)) {
            emoji_Pwd.setError("character not allowed");
            Toast.makeText(EmojiAuths.this, "character not allowed", Toast.LENGTH_SHORT).show();
            emoji_Pwd.setText("");
        }
    }


    private void reConNet_2() {
        emojiNetworkChangeListener.snackOn(getAppView_2);
        IntentFilter intentFilFill_2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver( emojiNetworkChangeListener,intentFilFill_2);
    }

    public static int getEmojiCount(CharSequence charSequence) {
        int count = 0;
        CharSequence processed = EmojiCompat.get().process(charSequence, 0, charSequence.length() -1, Integer.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL);
        if (processed instanceof Spannable) {
            Spannable spannable = (Spannable) processed;
            count = spannable.getSpans(0, spannable.length() - 1, EmojiSpan.class).length;
        }
        return count;
    }

    public byte[] getNewByteFromHex(String hexString){
        //To convert hex string to byte array, you need to first get the length
        //of the given string and include it while creating a new byte array.
        byte[] m_value = new byte[hexString.length() / 2];
        for (int i = 0; i < m_value.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            m_value[i] = (byte) j;
        }
        return m_value;
    }


    public void emojiPass_button_to_logInScreen(View view) {
        String all = "Allow";
        Intent pass_emj_otp = new Intent(getApplicationContext(),OTP_Code.class);
        pass_emj_otp.putExtra("permit", all);
        startActivity(pass_emj_otp);
      // startActivity(new Intent(getApplicationContext(), OTP_Code.class));
    }

    @Override
    public void onBackPressed() {
       emojiPass_button_to_logInScreen(getCurrentFocus());
    }

    public void emoji_Return(View view) {
    }

    @Override
    protected void onStart() {
        emojiNetworkChangeListener.snackOn(getAppView_2);
        IntentFilter intentFilFill_2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(emojiNetworkChangeListener,intentFilFill_2);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(emojiNetworkChangeListener);
        super.onStop();
    }


}