package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {
    EditText edtEmailSignUp,edtPasswordSignUp,edtNameSignUp,edtPhoneSignUp;
    Button btnSignUp;
    ImageView imgSignUp;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    DatabaseReference mData;
    Uri imgUri;
    int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        mData= FirebaseDatabase.getInstance().getReference("User");
        mStorage= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        anhXa();
        imgSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPick();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }
    private void anhXa() {
        edtEmailSignUp=findViewById(R.id.editTextEmailSignUp);
        edtPasswordSignUp=findViewById(R.id.editTextPasswordSignUp);
        btnSignUp=findViewById(R.id.buttonSignUpSignUp);
        edtNameSignUp=findViewById(R.id.editTextNameSignUp);
        edtPhoneSignUp=findViewById(R.id.editTextPhoneSignUp);
        imgSignUp=findViewById(R.id.imageViewPhotoSignUp);
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void photoPick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
    }
    /*private void register() {
        String email = edtEmailSignUp.getText().toString().trim();
        String pass = edtPasswordSignUp.getText().toString().trim();
        //progressBar.setVisibility(View.VISIBLE);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        });
    }     */
    private void register() {
        progressDialog.show();
        if (imgUri != null) {
            final StorageReference storageReference = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));
            storageReference.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        User user = new User(edtNameSignUp.getText().toString().trim(),edtPhoneSignUp.getText().toString().trim(),
                                edtEmailSignUp.getText().toString().trim(),downloadUri.toString());
                        mData.push().setValue(user);
                        mAuth.createUserWithEmailAndPassword(edtEmailSignUp.getText().toString().trim(),edtPasswordSignUp.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }
                        });
                    }
                }
            });

        } else {
            Toast.makeText(this, " no file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
