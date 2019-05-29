package com.rakesh.shop99.shop99admin.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rakesh.shop99.shop99admin.ImageUploadInfo;
import com.rakesh.shop99.shop99admin.MainActivity;
import com.rakesh.shop99.shop99admin.fragment.ProductFragment;
import com.rakesh.shop99.shop99admin.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout add_cateogry_view,add_subcateogry_view,add_product_view;
    Spinner categoryForSubcategory,categoryForProducat;
    boolean isFABOpen=false;
    FloatingActionButton fab1,fab2,fab3;
    Button chooseCategory, uploadCategory,resetCategory,submitCategory;
    EditText categoryName;
    ImageView categoryImage;
    int Image_Request_Code = 7;
    Uri FilePathUri;
    ProgressDialog progressDialog ;
    String Storage_Path_Category = "Category/";
    String lastUploadedImagePath="";
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        add_cateogry_view=(RelativeLayout)findViewById(R.id.add_category_view);
        add_subcateogry_view=(RelativeLayout)findViewById(R.id.add_subcategory_view);
        add_product_view=(RelativeLayout)findViewById(R.id.add_product_view);
        categoryForSubcategory=(Spinner)findViewById(R.id.spinner);
        categoryForProducat=(Spinner)findViewById(R.id.spinner1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        chooseCategory = (Button)findViewById(R.id.button);
        uploadCategory = (Button)findViewById(R.id.button2);
        submitCategory=(Button)findViewById(R.id.button3);
        resetCategory=(Button)findViewById(R.id.button4);
        categoryName=(EditText)findViewById(R.id.editText);
        categoryImage=(ImageView)findViewById(R.id.imageView2);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(NavigationActivity.this);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_cateogry_view.setVisibility(View.VISIBLE);
                add_cateogry_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_cateogry_view.setVisibility(View.GONE);
                    }
                });
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory(categoryForSubcategory);
                add_subcateogry_view.setVisibility(View.VISIBLE);
                add_subcateogry_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_subcateogry_view.setVisibility(View.GONE);
                    }
                });
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory(categoryForProducat);
                add_product_view.setVisibility(View.VISIBLE);
                add_product_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_product_view.setVisibility(View.GONE);
                    }
                });
            }
        });
        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });
        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageFileToFirebaseStorage();

            }
        });
        submitCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryStr=categoryName.getText().toString();
                if(categoryStr.equals("")) {
                    Toast.makeText(getApplicationContext(), "fill the category name.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(lastUploadedImagePath.equals("")){
                    Toast.makeText(getApplicationContext(), "upload a image.", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    progressDialog.setTitle("Category");
                    progressDialog.show();
                    progressDialog.setMessage("Uploading data....");

                    Map<String, Object> category = new HashMap<>();
                    category.put("name", categoryStr);
                    category.put("image", lastUploadedImagePath);

                    db.collection("category").document()
                            .set(category)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    categoryName.setText("");
                                    categoryImage.setImageResource(R.drawable.product1);
                                    lastUploadedImagePath="";
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"new category added",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Error adding document",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        resetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryName.setText("");
                categoryImage.setImageResource(R.drawable.product1);
                lastUploadedImagePath="";
            }
        });
        categoryForProducat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),position+" ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                categoryImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                chooseCategory.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_products) {
            Fragment fragment =(Fragment) ProductFragment.newInstance();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content,fragment).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void UploadImageFileToFirebaseStorage() {

        if (FilePathUri != null) {
            try {
                progressDialog.setTitle("Image is Uploading...");
                progressDialog.show();
                final StorageReference storageReference2nd = storageReference.child(Storage_Path_Category + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                storageReference2nd.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        lastUploadedImagePath=uri.toString();
                                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(NavigationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        // On progress change upload time.
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");

                            }
                        });
            }catch (Exception ex){
                Toast.makeText(NavigationActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }
            }

        else{

                Toast.makeText(NavigationActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

            }

    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public boolean getCategory(Spinner spinner){
        final List<String> categoryList=new ArrayList<String>();
        final ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_textview,categoryList);
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                categoryList.add(document.getData().get("name").toString());
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                });

        categoryAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spinner.setAdapter(categoryAdapter);

        return false;
    }
}
