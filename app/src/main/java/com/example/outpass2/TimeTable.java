package com.example.outpass2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


public class TimeTable extends AppCompatActivity {

    Spinner year,branch;
    Button datepicker;
    ArrayList<String> dates;
    String date1;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_time_table);

        db=FirebaseFirestore.getInstance();
        datepicker=findViewById(R.id.datepicker);
        year=findViewById(R.id.year);
        branch=findViewById(R.id.branch);


        dates=new ArrayList<>();



        Intent intent=getIntent();

//        datepicker.setOnClickListener(view -> {
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(TimeTable.this,
//                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
//                        selectedMonth++;
//
//                        date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
//                    }, year, month, day);
//            dates.add(date);
//            datePickerDialog.show();
//
//        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void inputDate(View view) {
        String year1=year.getSelectedItem().toString();
        String branch1=branch.getSelectedItem().toString();

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TimeTable.this,
                (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                    selectedMonth++;

                    date1 = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                }, year, month, day);
        datePickerDialog.show();

        dates.add(date1);


        String doc=year1+" "+branch1;
        Map<String,Object> map = new HashMap<>();
        map.put("date", FieldValue.arrayUnion(date1));

        db.collection("timeTable").document(doc).set(map, SetOptions.merge())
        .addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Date Added Successfully", Toast.LENGTH_SHORT).show();
    })
    .addOnFailureListener(e -> {
        Toast.makeText(this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
    });
    }
}