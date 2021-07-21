package com.example.firebaseyo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class LoginActivity extends AppCompatActivity {
    Button ch, up,location;
    ImageView img;
    StorageReference mStorageRef;
    public Uri imguri;
    private StorageTask uploadTask;
    Bitmap FixBitmap;
    EditText name,data;
    Button save;
    DatabaseReference reRef ;
    Member member;

    // acoount
    TextView userEmail;
    Button userLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 取得點選項目的id
        int id = item.getItemId();
        if (id == R.id.setting) {
            new AlertDialog.Builder(this)
                    .setTitle("設定")
                    .setMessage("成功")
                    .setPositiveButton("ok",null)
                    .show();
            Toast.makeText(getApplicationContext(),"yo",Toast.LENGTH_LONG).show();
        }
        else if (id==R.id.help)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //防止登出按返回仍然有資料的bug
            startActivity(intent);
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    } //覆寫meunu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
        //覆寫menu
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");//取得 Firebase 的連結
        reRef= FirebaseDatabase.getInstance().getReference().child("備註資訊");
        member=new Member();
userEmail=(TextView)findViewById(R.id.tvuseremail);
userLogout=(Button)findViewById(R.id.btnlogout);
        save =(Button)findViewById(R.id.btnsend);
        name=(EditText)findViewById(R.id.editText);
        data=(EditText)findViewById(R.id.editText2);
        ch = (Button) findViewById(R.id.btnchoose);
        up = (Button) findViewById(R.id.btnupload);
        img = (ImageView) findViewById(R.id.imgview);
        location=(Button)findViewById(R.id.loc);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userEmail.setText(firebaseUser.getEmail());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(LoginActivity.this,MapsActivity.class));
            }
        });
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
                //Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null &&uploadTask.isInProgress()){
                    Toast.makeText(LoginActivity.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }else {
                    Fileuploader();
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setName(name.getText().toString().trim());
                member.setData(data.getText().toString().trim());

                reRef.push().setValue(member);
                Toast.makeText(LoginActivity.this,"data is inserted",Toast.LENGTH_LONG).show();
            } //上傳資料
        });
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //防止登出按返回仍然有資料的bug
                startActivity(intent);
                // startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Filechooser();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private  String getExtension(Uri uri){
        ContentResolver cr =getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private  void  Fileuploader()
    {
        StorageReference Ref=mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));//指向 imguri 這個參照路徑

        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        uploadTask= Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(LoginActivity.this,"Image upload successFully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        // Handle unsuccessful uploads
                        // ...
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded"+(int)progress+"%"); //顯示進度條
                    }
                });



    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent, 2);//2為相機


    }

    private void Filechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {

            if (requestCode == 1  && data != null && data.getData() != null) {
                imguri = data.getData();
                img.setImageURI(imguri);
            } else if (requestCode == 2 )
            {

                FixBitmap = (Bitmap) data.getExtras().get("data");// 獲取相機返回的資料,並轉換為Bitmap圖片格式
                img.setImageBitmap(FixBitmap);
            }
        }
    }

}
