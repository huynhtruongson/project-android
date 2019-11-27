package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NhaTroPost extends AppCompatActivity {
    DatabaseReference mData;
    StorageReference mStorage;
    FirebaseAuth mAuth;
    EditText edtTieuDe,edtTen,edtDiaChi,edtQuan,edtDienTich,edtGiaTien,edtSdt,edtMoTa;
    ImageView imgHinh;
    Button btnPost;
    Uri imgUri;
    int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nha_tro_post);
        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        mStorage= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        anhXa();
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPick();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

    }
    private void anhXa() {
        edtTieuDe=findViewById(R.id.editTextTieuDePost);
        edtTen=findViewById(R.id.editTextTenPost);
        edtDiaChi=findViewById(R.id.editTextDiaChiPost);
        edtQuan=findViewById(R.id.editTextQuanPost);
        edtDienTich=findViewById(R.id.editTextDienTichPost);
        edtGiaTien=findViewById(R.id.editTextGiaTienPost);
        edtSdt=findViewById(R.id.editTextSdtPost);
        edtMoTa=findViewById(R.id.editTextMoTaPost);
        imgHinh=findViewById(R.id.imageViewHinhPost);
        btnPost=findViewById(R.id.buttonPost);
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
    private void addData() {
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
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        NhaTro nhaTro = new NhaTro(edtGiaTien.getText().toString().trim(),edtTieuDe.getText().toString().trim(),edtDiaChi.getText().toString().trim(),
                        edtQuan.getText().toString().trim(),edtSdt.getText().toString().trim(),edtDienTich.getText().toString().trim(),edtMoTa.getText().toString().trim(),edtTen.getText().toString().trim()
                        ,mAuth.getCurrentUser().getUid(),downloadUri.toString(),"0","0");
                        mData.push().setValue(nhaTro);
                        //list.add(sinhVien);
                        //adapter.notifyDataSetChanged();
                        Toast.makeText(NhaTroPost.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
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
