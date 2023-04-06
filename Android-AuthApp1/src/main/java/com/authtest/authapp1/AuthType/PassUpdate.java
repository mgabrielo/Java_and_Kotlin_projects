package com.authtest.authapp1.AuthType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.EmojiSpan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.authtest.authapp1.AuthEmojiFilter.CustomEditText;
import com.authtest.authapp1.LogIn;
import com.authtest.authapp1.MainActivity;
import com.authtest.authapp1.R;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class PassUpdate extends AppCompatActivity {


    FirebaseAuth fire_up_Auth;
    FirebaseFirestore fire_up_store;
    String updating_userid,updating_email,updating_pass, unicodeUpdateHexPwd, updateEmojiUTF_hexPwd;
    String oldUniUpdateHexPwd, oldEmojiUTFHexTv;
    String get_oldPass;
    Button Submit_update;
    AppCompatEditText up_edit_pass;
    TextView emoji_display, emj_txt;
    int update_count_of_emoji;
    NetworkChangeListener upNetworkChangeListener = new NetworkChangeListener();
    View getAppView_5;
    ConstraintLayout pass_constrain;
    ImageView vet_2;
    EmojIconActions em_act_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_update);

        EmojiCompat.Config update_config = new BundledEmojiCompatConfig(this);
        update_config.setReplaceAll(true);
        EmojiCompat.init(update_config);

        fire_up_Auth = FirebaseAuth.getInstance();
        fire_up_store = FirebaseFirestore.getInstance();
        emj_txt = findViewById(R.id.display_old_pass_Txt);
        emoji_display = findViewById(R.id.display_old_pass_emoji);
        Submit_update = findViewById(R.id.submit_new_password);
        up_edit_pass =  findViewById(R.id.new_password);
        pass_constrain = findViewById(R.id.passUp_con);
        getAppView_5 = pass_constrain;

        Intent data_update = getIntent();
        updating_userid = data_update.getStringExtra("passID");
        updating_email = data_update.getStringExtra("passMailer");
        updating_pass = data_update.getStringExtra("passChange");
        //emoji_display.setText(updating_pass);
        CustomEditText customEmjEditText = new CustomEditText(this);
        customEmjEditText.init(up_edit_pass);

        DocumentReference documentRefer = fire_up_store.collection("users").document(updating_userid);
        documentRefer.addSnapshotListener(this, (value, error) -> {
            if (value != null ) {
                emoji_display.setText(value.getString("your_pass"));

                 get_oldPass = value.getString("your_pass");
            }

        });


        Submit_update.setOnClickListener(new View.OnClickListener() {
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
                            reConNet_5();
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
                    updatePassProcess();
                }


            }
        });

    }



    private void updatePassProcess() {
        String get_pass = up_edit_pass.getText().toString();


        unicodeUpdateHexPwd = "U+";
        StringBuilder get_EmjPwdSb = new StringBuilder();
        byte[] get_edt_utf16Bytes = get_pass.getBytes(StandardCharsets.UTF_16);
        for (byte x : get_edt_utf16Bytes ) {
            get_EmjPwdSb.append(String.format("%02x", x));
        }

        unicodeUpdateHexPwd += get_EmjPwdSb;
        byte[] utfHexBytesEmjEdt = getNewUpdateByteFromHex( unicodeUpdateHexPwd.replace("U+",""));

        updateEmojiUTF_hexPwd = new String(utfHexBytesEmjEdt, StandardCharsets.UTF_16);

        oldUniUpdateHexPwd = "U+";
        StringBuilder oldEmjPwdSb = new StringBuilder();
        byte[] oldEdt_utf16Bytes = get_oldPass.getBytes(StandardCharsets.UTF_16);
        for (byte z : oldEdt_utf16Bytes ) {
            oldEmjPwdSb.append(String.format("%02x", z));
        }

        oldUniUpdateHexPwd += oldEmjPwdSb;
        byte[] old_utfHexBytesEmjEdt = getNewUpdateByteFromHex(oldUniUpdateHexPwd.replace("U+",""));

        oldEmojiUTFHexTv = new String(old_utfHexBytesEmjEdt, StandardCharsets.UTF_16);

        Log.d("oldPwd", "onClick: "+ oldEmojiUTFHexTv);


        if(TextUtils.isEmpty(get_pass)){
            up_edit_pass.setError("Emoji Password is required");
            Toast.makeText(PassUpdate.this, "New Emoji Password is required", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.equals(updateEmojiUTF_hexPwd,oldEmojiUTFHexTv)) {

            update_count_of_emoji = getUpdateEmojiCount(get_pass);
            if(update_count_of_emoji < 4){
                up_edit_pass.setError("Minimum of 4 Emoji Password");
                Toast.makeText(PassUpdate.this, "Minimum of 4 Emoji Password", Toast.LENGTH_SHORT).show();
            }else if(update_count_of_emoji > 8) {
                up_edit_pass.setError("Maximum of 8 Emoji Password");
                Toast.makeText(PassUpdate.this, "Maximum of 8 Emoji Password", Toast.LENGTH_SHORT).show();
            }else{
                emoji_display.setVisibility(View.GONE);
                DocumentReference d_Ref = fire_up_store.collection("users").document(updating_userid);
                Map<String, Object> map_new_password = new HashMap<>();
                map_new_password.put("your_pass", updateEmojiUTF_hexPwd);

                d_Ref.update(map_new_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PassUpdate.this, "Password Update Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PassUpdate.this, "password not updated, Try again", Toast.LENGTH_SHORT).show();
                    }
                });


            }

        }else if(updateEmojiUTF_hexPwd.equals(oldEmojiUTFHexTv)) {

            up_edit_pass.setError("Please Insert New Password for Update");
            Toast.makeText(PassUpdate.this, "Please Insert New Password for Update", Toast.LENGTH_SHORT).show();
        }
    }

    private void reConNet_5() {
        upNetworkChangeListener.snackOn(getAppView_5);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(upNetworkChangeListener,intentFilter);
    }

    public static int getUpdateEmojiCount(CharSequence charSequence) {
        int new_count = 0;
        CharSequence processed = EmojiCompat.get().process(charSequence, 0, charSequence.length() -1, Integer.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL);
        if (processed instanceof Spannable) {
            Spannable spannable = (Spannable) processed;
            new_count = spannable.getSpans(0, spannable.length() - 1, EmojiSpan.class).length;
        }
        return new_count;
    }

    public byte[] getNewUpdateByteFromHex(String hexString) {
        byte[] new_value = new byte[hexString.length() / 2];
        for (int r = 0; r < new_value.length; r++) {
            int index = r * 2;
            int s = Integer.parseInt(hexString.substring(index, index + 2), 16);
            new_value[r] = (byte) s;
        }
        return new_value;
    }

    public void cancelPassUpdate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_really_cancel_update));
        builder.setTitle(getResources().getString(R.string.cancel_update));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.rejected), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.accepted), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

               startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(PassUpdate.this, R.string.pass_update_cancelled,
                        Toast.LENGTH_LONG).show();
                finish();


            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }



    public void update_button_to_Dashboard(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
      // Submit_update.performClick();

        update_button_to_Dashboard(getCurrentFocus());
    }

    @Override
    protected void onStart() {
        upNetworkChangeListener.snackOn(getAppView_5);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(upNetworkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(upNetworkChangeListener);
        super.onStop();
    }



}