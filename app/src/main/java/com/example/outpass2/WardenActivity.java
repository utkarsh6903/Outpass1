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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WardenActivity extends AppCompatActivity {

    TextView tv1;
    Button btstudents,dates;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wardenmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.wardenLogout)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if(id == R.id.wardenProfile)
        {
            startActivity(new Intent(this, WardenProfilePage.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warden);

        tv1=findViewById(R.id.tv1);
        btstudents = findViewById(R.id.btstudents);

        btstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudentsList.class);
                startActivity(intent);
            }
        });

        dates=findViewById(R.id.dates);


        Intent intent1=getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String sapid = sharedPreferences.getString("sapId", "");
        String role = sharedPreferences.getString("role", "");
        tv1.setText(sapid+" "+role);
        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outing=new Intent(getApplicationContext(),TimeTable.class);
                startActivity(outing);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}