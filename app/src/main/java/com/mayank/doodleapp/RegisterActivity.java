package com.mayank.doodleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    ProgressDialog nDialog;

    String uEmail;
    String uPassword;
    EditText edtEmail;
    EditText edtName;
    EditText edtPass;
    TextView txtLogin;
    TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPass = findViewById(R.id.edtPass);
        txtLogin = findViewById(R.id.txtLogin);
        btnRegister = findViewById(R.id.textView5);

        firebaseAuth = FirebaseAuth.getInstance();

        nDialog = new ProgressDialog(RegisterActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Getting Data");
        nDialog.setIcon(R.drawable.swirl);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);


        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            finish();
        }



        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#3486eb"));
        }






        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uEmail = edtEmail.getText().toString();
                uPassword = edtPass.getText().toString();


                if(uEmail!=null && uPassword!=null){

                    nDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                nDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {

                                nDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG ).show();
                            }

                        }
                    });


                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Fill credentials properly", Toast.LENGTH_SHORT).show();


                }

            }
        });


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

    }
}