package com.example.outpass2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WardenProfilePage extends AppCompatActivity {

    TextView info;
    EditText pass;
    FirebaseFirestore db;
    Button passChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warden_profile_page);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String sapid = sharedPreferences.getString("sapId", "");

        db = FirebaseFirestore.getInstance();
        info = findViewById(R.id.winfo);
        pass = findViewById(R.id.wpass);
        passChange = findViewById(R.id.wpassChange);

        db.collection("users").document(sapid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String fname = document.getString("name");
                    String fpass = document.getString("password");
                    info.setText("NAME: "+fname.toUpperCase());
                    pass.setText(fpass);
                    passChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("users").document(sapid).update("password",pass.getText().toString()).addOnSuccessListener(aVoid->{
                                Toast.makeText(WardenProfilePage.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}