package com.example.asimz.bucketlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter {
    List list =new ArrayList();
    MainActivity mainActivity = new MainActivity();

    public ListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void add(Object object)
    {
        super.add(object);
        list.add(object);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row =convertView;
        LayoutHandler layoutHandler;
        if (row==null)
        {
            LayoutInflater inflater =(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.item_todo,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.title=(TextView)row.findViewById(R.id.task_title);
            layoutHandler.desc=(TextView)row.findViewById(R.id.task_desc);
            layoutHandler.points=(TextView)row.findViewById(R.id.task_points);
            row.setTag(layoutHandler);

        }
        else
            {
                layoutHandler= (LayoutHandler)row.getTag();

            }
//        Button done = (Button) row.findViewById(R.id.task_delete);
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainActivity.doneTask(v);
//            }
//        });
            DataProvider dp =(DataProvider)this.getItem(position);
            layoutHandler.title.setText(dp.getTitle());
            layoutHandler.desc.setText(dp.getDesc());
            layoutHandler.points.setText(dp.getPoints());
        return row;
    }

    static class LayoutHandler {
        TextView title,desc,points;
    }
}
