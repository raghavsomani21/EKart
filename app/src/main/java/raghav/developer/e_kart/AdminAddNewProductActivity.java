package raghav.developer.e_kart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    public String categoryName,desc,price,prodName,saveCurrentDate,saveCurrentTime,prodKey,downloadImageUrl;
    private Button addProductButton;
    private ImageView inputProdImage;
    private EditText inputProdName,inputProdDesc,inputProdPrice;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private StorageReference prodImagesRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().getString("category");
        //Toast.makeText(AdminAddNewProductActivity.this,"Product to be added "+categoryName,Toast.LENGTH_LONG).show();
        addProductButton = findViewById(R.id.add_product_button);
        inputProdImage = findViewById(R.id.select_product_image);
        inputProdName = findViewById(R.id.product_name);
        inputProdDesc = findViewById(R.id.product_description);
        inputProdPrice = findViewById(R.id.product_price);

        prodImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loadingBar = new ProgressDialog(this);

        inputProdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateProductData();

            }
        });
    }
    //Adding photo

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){

            imageUri = data.getData();
            Log.i("Image Uri = ",imageUri.getPath());
            Log.i("Image URI last path segment = ",imageUri.getLastPathSegment());
            inputProdImage.setImageURI(imageUri);

        }

    }
    //-----------------------------------------------------------------------//

    //validating

    private void validateProductData() {

        desc = inputProdDesc.getText().toString();
        price = inputProdPrice.getText().toString();
        prodName = inputProdName.getText().toString();

        if(imageUri==null)
            Toast.makeText(AdminAddNewProductActivity.this,"Product Image is mandatory",Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(desc))
            Toast.makeText(AdminAddNewProductActivity.this,"Product Description is mandatory",Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(price))
            Toast.makeText(AdminAddNewProductActivity.this,"Product price is mandatory",Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(prodName))
            Toast.makeText(AdminAddNewProductActivity.this,"Product name is mandatory",Toast.LENGTH_LONG).show();
        else{
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Add new product");
        loadingBar.setMessage("Please wait while we add a new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        prodKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = prodImagesRef.child(imageUri.getLastPathSegment() + prodKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMsg = e.toString();
                Log.i("E3-",errorMsg);
                Toast.makeText(AdminAddNewProductActivity.this,"Error-"+errorMsg,Toast.LENGTH_LONG).show();
            }
        }
        ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Product Image uploaded successfully",Toast.LENGTH_LONG).show();
                Task <Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        /*if(!task.isSuccessful()){
                            Log.i("E1-",task.getException().toString());
                            throw task.getException();
                        }*/
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "product image URL successfully received", Toast.LENGTH_LONG).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {

        HashMap <String,Object> productMap = new HashMap<>();
        productMap.put("pid",prodKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",desc);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("name",prodName);

        productRef.child(prodKey).updateChildren(productMap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this,"Product added successfully",Toast.LENGTH_LONG).show();
                }
                else{
                    String mssg = task.getException().toString();
                    Log.i("Error2-",mssg);
                    Toast.makeText(AdminAddNewProductActivity.this,"Error-"+mssg,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
