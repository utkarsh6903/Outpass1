package com.example.outpass2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> sapids,names;
    LayoutInflater inflater;


    public CustomBaseAdapter(Context applicationContext, ArrayList<String> sapids, ArrayList<String> names) {
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
        view = inflater.inflate(R.layout.ui_view,null);
        TextView lv_sname = view.findViewById(R.id.lv_sname);
        TextView lv_sap = view.findViewById(R.id.lv_sap);
        lv_sap.setText(sapids.get(i));
        lv_sname.setText(names.get(i));
        return view;
    }
}
