package com.authtest.authapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.authtest.authapp1.AuthType.PassUpdate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.UserDataReader;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    TextView profile_fst_name,profile_lst_name,profile_email,profile_phone,profile_pass;
    FirebaseAuth fire_Auth;
    FirebaseFirestore fire_store;
    String userid,pass_mail;
    FirebaseUser the_fire_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_fst_name = findViewById(R.id.fst_name);
        profile_lst_name = findViewById(R.id.lst_name);
        profile_email = findViewById(R.id.your_email);
        profile_phone =findViewById(R.id.your_phone);
        profile_pass = findViewById(R.id.your_password);



        fire_Auth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();

        the_fire_user = fire_Auth.getCurrentUser();

        if(the_fire_user != null) {
            userid = fire_Auth.getCurrentUser().getUid();

            DocumentReference documentReference = fire_store.collection("users").document(userid);
            documentReference.addSnapshotListener(this, (value, error) -> {

                if (value != null) {
                    profile_fst_name.setText(value.getString("first_name"));
                }
                if (value != null) {
                    profile_lst_name.setText(value.getString("last_name"));
                }
                if (value != null) {
                    profile_email.setText(value.getString("your_email"));
                }
                if (value != null) {
                    profile_phone.setText(value.getString("your_phone"));
                }
                if (value != null) {

                    profile_pass.setText(value.getString("your_pass"));

                }
            });



        }else{
            startActivity(new Intent(getApplicationContext(),LogIn.class));
        }





    }




    public void go_to_update_password(View view) {
        Intent pass_updater = new Intent(getApplicationContext(), PassUpdate.class);
        pass_updater.putExtra("passID",  userid );
        String passChanger = profile_pass.getText().toString();
        pass_mail = profile_email.getText().toString();
        pass_updater.putExtra("passChange", passChanger);
        pass_updater.putExtra("passMailer", pass_mail);
        startActivity(pass_updater);

    }

    public void logout(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_really_want_to_signout));
        builder.setTitle(getResources().getString(R.string.sign_out));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
                Toast.makeText(MainActivity.this, R.string.log_out_success,
                        Toast.LENGTH_LONG).show();
                finish();


            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
    }

    public void deleteAccountBtn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_really_want_to_delete_account));
        builder.setTitle(getResources().getString(R.string.delete_account));
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                DocumentReference d_Refs = fire_store.collection("users").document(userid);
                Map<String, Object> map_no_account = new HashMap<>();
                String noValue = "";
                map_no_account.put("first_name",noValue);
                map_no_account.put("last_name",noValue);
                map_no_account.put("your_email",noValue);
                map_no_account.put("your_phone",noValue);
                map_no_account.put("your_pass",noValue);
                map_no_account.put("confirm_pass",noValue);

                d_Refs.update(map_no_account).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LogIn.class));
                        Toast.makeText(MainActivity.this, R.string.account_delete_success,
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Account could not be deleted", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}