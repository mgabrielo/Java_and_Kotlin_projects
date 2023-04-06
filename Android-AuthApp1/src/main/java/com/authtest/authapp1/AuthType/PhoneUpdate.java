package com.authtest.authapp1.AuthType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.authtest.authapp1.AuthData.AuthAppDb;
import com.authtest.authapp1.AuthData.AuthAppModal;
import com.authtest.authapp1.AuthRegString.HashRegString;
import com.authtest.authapp1.LogIn;
import com.authtest.authapp1.MainActivity;
import com.authtest.authapp1.OTP_Code;
import com.authtest.authapp1.R;
import com.authtest.authapp1.Register;
import com.authtest.authapp1.Utility.ConnectReceiver;
import com.authtest.authapp1.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class PhoneUpdate extends AppCompatActivity {


    FirebaseAuth fire_m_Auth;
    FirebaseFirestore fire_m_store;
    String update_phone_id, update_phone_email, get_oldPhone, new_phone, real_phone,code_y;
    TextView previous_phone;
    EditText phoneEdt_txt;
    Button phoneUpBtn;
    CountryCodePicker countryUpPhone;
    AuthAppDb phoneUp_db;
    AuthAppModal phoneUp_modal;
    NetworkChangeListener phoneUpNetworkChangeListener= new NetworkChangeListener();
    ConstraintLayout phoneUpCon;
    View getAppView_6;
    String AutoReGenPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_update);

        fire_m_Auth =FirebaseAuth.getInstance();

        fire_m_store = FirebaseFirestore.getInstance();
        previous_phone = findViewById(R.id.old_phoneUp_display);
        phoneEdt_txt = findViewById(R.id.phoneUp_edt);
        phoneUpBtn = findViewById(R.id.submit_phoneUp);
        countryUpPhone = findViewById(R.id.phoneUp_countryCode);
        phoneUpCon = findViewById(R.id.phoneUpdateConstrain);
        getAppView_6 = phoneUpCon;

        Intent data_upPhone = getIntent();
        update_phone_id = data_upPhone.getStringExtra("phoneUp_id");
        update_phone_email = data_upPhone.getStringExtra("phoneUp_email");



        DocumentReference documentRefer = fire_m_store.collection("users").document(update_phone_id);
        documentRefer.addSnapshotListener(this, (value, error) -> {
            if (value != null ) {
               previous_phone.setText(value.getString("your_phone"));

                get_oldPhone = value.getString("your_phone");
            }

        });

        phoneUpBtn.setOnClickListener(new View.OnClickListener() {
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
                            reConNet_6();
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
                    phoneUpdateProcess();
                }


            }
        });


    }



    private void phoneUpdateProcess() {

        new_phone = phoneEdt_txt.getText().toString().trim();
        if(TextUtils.isEmpty(new_phone) ){
            phoneEdt_txt.setError("please insert phone number");
            return;
        }else if(new_phone.length() < 10){
            phoneEdt_txt.setError("Phone number incomplete");
            Toast.makeText(PhoneUpdate.this, "Phone number incomplete", Toast.LENGTH_SHORT).show();
            return;
        }else if(new_phone.length() > 11){
            phoneEdt_txt.setError("Phone number invalid");
            Toast.makeText(PhoneUpdate.this, "Phone number invalid", Toast.LENGTH_SHORT).show();
            return;
        } else if (new_phone.length() == 11) {
            String get_phone = phoneEdt_txt.getText().toString().trim();
           real_phone =  get_phone.substring(1);
        }else{
            phoneEdt_txt.setError(null);
             }

        if(!TextUtils.isEmpty(real_phone )) {
            code_y   = "+" + countryUpPhone.getSelectedCountryCode()+ real_phone;

        }else{

            code_y = "+" + countryUpPhone.getSelectedCountryCode() + new_phone;
        }

        System.out.println("real_phone : "+code_y);
        String re_gen_pass =  code_y.substring(code_y.length() - 10);
        AutoReGenPass = HashRegString.main(re_gen_pass);

        DocumentReference d_Refer = fire_m_store.collection("users").document(update_phone_id);
            Map<String, Object> map_new_phone = new HashMap<>();
        map_new_phone.put("confirm_pass",AutoReGenPass);
            map_new_phone.put("your_phone", code_y);


            d_Refer.update(map_new_phone).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    phoneUp_db = AuthAppDb.getInstance(getApplicationContext());
                    phoneUp_modal = new AuthAppModal(update_phone_email,code_y);
                    String phone_val =  phoneUp_db.authAppDAO().getUserPhone(update_phone_email);

                    if(TextUtils.isEmpty(phone_val)){
                        phoneUp_db.authAppDAO().insert(phoneUp_modal);
                    }else{
                        phoneUp_db.authAppDAO().update(code_y,update_phone_email);
                    }

                    String allow_snack=  "update success";
                    Toast.makeText(PhoneUpdate.this, "Phone Update Successful", Toast.LENGTH_SHORT).show();
                    Intent phoneUp_success = new Intent(getApplicationContext(),LogIn.class);
                    phoneUp_success.putExtra("phoneUpSuccess", allow_snack);
                    startActivity(phoneUp_success);

                   // startActivity(new Intent(getApplicationContext(), LogIn.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PhoneUpdate.this, "Phone not updated, Try again", Toast.LENGTH_SHORT).show();
                }
            });


        }




    private void reConNet_6() {
        phoneUpNetworkChangeListener.snackOn(getAppView_6);
        IntentFilter intentFilFill_6 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(phoneUpNetworkChangeListener,intentFilFill_6);
    }

    public void phoneUp_to_logIn(View view) {
        startActivity(new Intent(getApplicationContext(), LogIn.class));
    }

    @Override
    public void onBackPressed() {
        phoneUp_to_logIn(getCurrentFocus());
    }


    @Override
    protected void onStart() {
        phoneUpNetworkChangeListener.snackOn(getAppView_6);
        IntentFilter intentFilFill_6 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(phoneUpNetworkChangeListener,intentFilFill_6);
        super.onStart();
    }



    @Override
    protected void onStop() {
        unregisterReceiver(phoneUpNetworkChangeListener);
        super.onStop();
    }

    public void cancelPhoneUpdate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_really_cancel_phone_update));
        builder.setTitle(getResources().getString(R.string.cancel_phone_update));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.rejected), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.accepted), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
                Toast.makeText(PhoneUpdate.this, R.string.phone_update_cancelled,
                        Toast.LENGTH_LONG).show();
                finish();


            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}