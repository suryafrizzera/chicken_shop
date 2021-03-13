package com.example.chickenshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login_activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        final Button getotp = findViewById(R.id.getotp_button);
        final EditText name = findViewById(R.id.username_edittext);
        final EditText ph_number = findViewById(R.id.ph_edittext);
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ph_number.getText().toString().trim().isEmpty()){
                    Toast.makeText(login_activity.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                }
                else {
                    getotp.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            getotp.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            getotp.setVisibility(View.VISIBLE);
                            Toast.makeText(login_activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();



                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(View.GONE);
                            getotp.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(login_activity.this,otpverification.class);
                            intent.putExtra("verificationid",verificationid);
                            intent.putExtra("mobile",ph_number.getText().toString().trim());
                            intent.putExtra("Name",name.getText().toString());
                            startActivity(intent);
                        }
                    };

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber("+91" + ph_number.getText().toString().trim())
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity( login_activity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);



                }
            }
        });
    }
}