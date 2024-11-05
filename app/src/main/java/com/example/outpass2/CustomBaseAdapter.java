package com.example.outpass2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> sapids,names;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();;



    public CustomBaseAdapter(Activity applicationContext, ArrayList<String> sapids, ArrayList<String> names) {
        this.sapids = sapids;
        this.context = applicationContext;
        this.inflater = LayoutInflater.from(context);
        this.names = names;

    }

    @Override
    public int getCount() {
        return sapids.size();
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
        view = inflater.inflate(R.layout.ui_view, null);
        TextView lv_sname = view.findViewById(R.id.lv_sname);
        TextView lv_sap = view.findViewById(R.id.lv_sap);
        Button bt_ban = view.findViewById(R.id.bt_ban);

        lv_sap.setText(sapids.get(i));
        lv_sname.setText(names.get(i));

        bt_ban.setOnClickListener(v -> {
            showBanDialog(i);  // Pass the position to handle the specific item if needed
        });

        return view;
    }

    private void showBanDialog(int position) {

        if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
            return;
        }

        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        RadioButton option15Days = new RadioButton(context);
        option15Days.setText("15 Days");
        radioGroup.addView(option15Days);

        RadioButton option30Days = new RadioButton(context);
        option30Days.setText("30 Days");
        radioGroup.addView(option30Days);

        RadioButton optionPermanent = new RadioButton(context);
        optionPermanent.setText("Permanent");
        radioGroup.addView(optionPermanent);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        layout.addView(radioGroup);

        new AlertDialog.Builder(context)
                .setTitle("Give Ban to "+names.get(position))
                .setView(layout)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Get selected ban duration
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    String selectedBanDuration = "";
                    if (selectedId == option15Days.getId()) {
                        selectedBanDuration = "15";
                    } else if (selectedId == option30Days.getId()) {
                        selectedBanDuration = "30";
                    } else if (selectedId == optionPermanent.getId()) {
                        selectedBanDuration = "Permanent";
                    }

                    Map<String,String> map = new HashMap<>();
                    if(selectedBanDuration=="Permanent")
                    {
                        map.put("banEndDate","Permanent");
                    }
                    else
                    {
                        LocalDate currentDate = LocalDate.now();
                        LocalDate newDate = currentDate.plusDays(Long.parseLong(selectedBanDuration));
                        map.put("banEndDate",newDate.toString());
                    }
                    final String x = selectedBanDuration;
                    db.collection("users").document(sapids.get(position)).set(map, SetOptions.merge()).addOnSuccessListener(t->{
                        Toast.makeText(context, ""+names.get(position)+" is banned for "+x+" days", Toast.LENGTH_SHORT).show();
                    });





                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


}
