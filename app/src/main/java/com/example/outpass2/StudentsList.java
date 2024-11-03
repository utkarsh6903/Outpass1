package com.example.outpass2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentsList extends AppCompatActivity {

    ListView studentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_students_list);

        studentsListView = findViewById(R.id.studentsListView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> sapids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        db.collection("users").whereEqualTo("role","student").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    names.add(document.getString("name"));
                    sapids.add(documentId);
                }
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(),sapids,names);
                studentsListView.setAdapter(customBaseAdapter);
            }
            else {
                Toast.makeText(this, "unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}