package com.example.asimz.bucketlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asimz.bucketlist.db.TaskContract;
import com.example.asimz.bucketlist.db.TaskDbHelper;

public class addTasks extends AppCompatActivity {

    public EditText titles,desc,points;
    String txt_title,txt_desc,txt_points;
    TaskDbHelper mHelper;
    Button addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        titles =(EditText) findViewById(R.id.titleEditText);
        desc =(EditText) findViewById(R.id.descEditText);
        points =(EditText) findViewById(R.id.points);
        mHelper = new TaskDbHelper(this);
        addTask =(Button) findViewById(R.id.addTask);



        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_title = titles.getText().toString();
                txt_desc = desc.getText().toString();
                txt_points = points.getText().toString();
                addTask();
            }
        });





    }

    void addTask(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, txt_title);
        values.put(TaskContract.TaskEntry.COL_TASK_DESC, txt_desc);
        values.put(TaskContract.TaskEntry.COL_TASK_POINT, txt_points);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        Intent i =new Intent(addTasks.this,MainActivity.class);
        startActivity(i);
    }
}
