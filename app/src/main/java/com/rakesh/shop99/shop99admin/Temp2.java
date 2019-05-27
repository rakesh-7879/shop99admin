package com.rakesh.shop99.shop99admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Temp2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp2);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document("0KNBezA3LZoH7YylDdeD")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                                Toast.makeText(getApplicationContext(),document.getId()+"=>"+document.getData(),Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(),"Error getting documents.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
