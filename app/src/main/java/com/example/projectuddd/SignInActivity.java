package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText edtEmailSignIn,edtPasswordSignIn;
    Button btnSignIn,btnSignUp;
    TextView txtRecoverPassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        anhXa();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging...");
        mAuth=FirebaseAuth.getInstance();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        txtRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });

    }
    private void anhXa() {
        edtEmailSignIn=findViewById(R.id.editTextEmailSignIn);
        edtPasswordSignIn=findViewById(R.id.editTextPasswordSignIn);
        btnSignIn=findViewById(R.id.buttonSignIn);
        btnSignUp=findViewById(R.id.buttonSignUp);
        txtRecoverPassword=findViewById(R.id.textViewRecoverPassword);
    }
    private void signIn() {
        String email = edtEmailSignIn.getText().toString().trim();
        String pass = edtPasswordSignIn.getText().toString().trim();
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void signUp() {
        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
    private void recoverPassword() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Recover password !");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailRecover = new EditText(this);
        emailRecover.setHint("Email");
        emailRecover.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailRecover);
        dialogBuilder.setView(linearLayout);
        dialogBuilder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailRcv = emailRecover.getText().toString().trim();
                beginRecoverPassword(emailRcv);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.show();
    }
    private void beginRecoverPassword(String email) {
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                    Toast.makeText(SignInActivity.this, "Email sent !", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SignInActivity.this, "Fail !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            finish();
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
        }
    }
}
