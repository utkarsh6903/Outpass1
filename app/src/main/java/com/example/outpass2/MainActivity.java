package com.example.outpass2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    EditText sap, pass;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
        bt1 = findViewById(R.id.bt1);
        sap = findViewById(R.id.sap);
        pass = findViewById(R.id.pass);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        String sapid = sap.getText().toString();
        String password = pass.getText().toString();

        db.collection("users").document(sapid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    String pass1 = document.getString("password");
                    String role1= document.getString("role");
                    if (pass1.equals(password)) {
                        Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        if(role1.equals("warden")){
                            Intent intent1=new Intent(MainActivity.this,WardenActivity.class);
                            startActivity(intent1);
                        }
                        else if(role1.equals("student")){
                            Intent intent2=new Intent(MainActivity.this,StudentActivity.class);
                            startActivity(intent2);
                        }
                        else{
                            Toast.makeText(this, "Invalid Role", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Invalid Password or Userid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}