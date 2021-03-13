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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class otpverification extends AppCompatActivity {
    String verificationid;
    String code;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Users = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        verificationid = getIntent().getStringExtra("verificationid");

        final ProgressBar progressBar = findViewById(R.id.otp_progressbar);
        final EditText otp_edittext = findViewById(R.id.otp_edittext);
        final Button verify_button = findViewById(R.id.verify_button);

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_edittext.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(otpverification.this,"Enter a valid code",Toast.LENGTH_SHORT).show();
                }
                code = otp_edittext.getText().toString().trim();
                if(verificationid != null){
                    verify_button.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    final PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationid,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            verify_button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            if(task.isSuccessful()){

                                UserProfileChangeRequest setuser = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(getIntent().getStringExtra("Name")).build();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                user.updateProfile(setuser);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"The code you entered was invalid",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}