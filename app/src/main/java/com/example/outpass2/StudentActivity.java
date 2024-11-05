package com.example.outpass2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    TextView tv1;
    Button bt1;
    FirebaseFirestore db;
    String doc;
    ArrayList<String> dates;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studentmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.studentLogout)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if(id == R.id.studentProfile)
        {
            startActivity(new Intent(this, StudentProfilePage.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);

        dates=new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        tv1=findViewById(R.id.tv1);
        bt1=findViewById(R.id.bt1);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String sapid = sharedPreferences.getString("sapId", "");
        String role = sharedPreferences.getString("role", "");

        Intent intent1=getIntent();
        tv1.setText(sapid+" "+role);

        bt1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {


                                       db.collection("users").document(sapid).get().addOnCompleteListener(task -> {
                                           if (task.isSuccessful()) {
                                               DocumentSnapshot document = task.getResult();
                                               Toast.makeText(StudentActivity.this, "123", Toast.LENGTH_SHORT).show();

                                               if (document.exists()) {
                                                   doc = document.getString("year") + " " + document.getString("branch");


                                                   db.collection("timeTable").document(doc).get().addOnCompleteListener(task2 -> {
                                                       if (task2.isSuccessful()) {
                                                           DocumentSnapshot document2 = task2.getResult();

                                                           if (document2.exists()) {
                                                               dates = (ArrayList<String>) document2.get("date");


                                                               if (dates != null) {
                                                                   String[] dates1 = dates.toArray(new String[0]);


                                                                   AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
                                                                   builder.setTitle("Outing Dates");
                                                                   StringBuilder message = new StringBuilder();
                                                                   for (String date : dates) {
                                                                       message.append(date).append("\n");
                                                                   }
                                                                   builder.setMessage(message.toString());
                                                                   builder.setPositiveButton("OK", (dialog, which) -> {
                                                                       dialog.dismiss();
                                                                   });
                                                                   AlertDialog alertDialog = builder.create();
                                                                   alertDialog.show();
                                                               }
                                                           } else {
                                                               Toast.makeText(StudentActivity.this, "Invalid doc", Toast.LENGTH_SHORT).show();
                                                           }
                                                       } else {
                                                           Toast.makeText(StudentActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                               } else {
                                                   Toast.makeText(StudentActivity.this, "Invalid Sap ID", Toast.LENGTH_SHORT).show();
                                               }
                                           } else {
                                               Toast.makeText(StudentActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}