package com.example.outpass2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterForStudentOutings extends BaseAdapter {

    Context context;
    ArrayList<String> arrayOfDates;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    String sapid;

    public AdapterForStudentOutings(Activity studentActivity, ArrayList<String> arrayOfDates, String sapid) {
        this.arrayOfDates = arrayOfDates;
        this.context = studentActivity;
        this.inflater = LayoutInflater.from(context);
        this.sapid = sapid;
    }

    @Override
    public int getCount() {
        return arrayOfDates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.uifor_studentsoutings, null);
        TextView s_outings = view.findViewById(R.id.s_outings);
        Button apply_button = view.findViewById(R.id.apply_button);

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = arrayOfDates.get(i);
                String[] parts = date.split("/");
                String result = parts[0] + parts[1];
                LocalDate currentDate = LocalDate.now();
                String currDate = String.valueOf(currentDate.getDayOfMonth()) + String.valueOf(currentDate.getMonthValue());

                LocalTime currentTime = LocalTime.now();
                int currentHour = currentTime.getHour();



                if(!currDate.equals(result))
                {
                    Toast.makeText(context, "Invalid Outing", Toast.LENGTH_SHORT).show();
                }
                else if(currentHour<17 && currDate.equals(result))
                {
                    Toast.makeText(context, "You can apply after 5pm", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String documentId = currDate;
                    Map<String,Object> data = new HashMap<>();
                    data.put("students", FieldValue.arrayUnion(sapid));
                    db.collection("outings").document(documentId).set(data, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Applied", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(aVoid -> {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            });
                }

            }
        });

        s_outings.setText("Outing Date:- "+arrayOfDates.get(i));

        return view;
    }
}
