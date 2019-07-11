package com.example.asimz.bucketlist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asimz.bucketlist.db.PointsDbHelper;
import com.example.asimz.bucketlist.db.TaskContract;
import com.example.asimz.bucketlist.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private TextView pointsDisplay;
    private ArrayAdapter<String> mAdapter;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pointsDisplay = (TextView) findViewById(R.id.points);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mHelper = new TaskDbHelper(this);
        listAdapter =new ListAdapter(getApplicationContext(), R.layout.item_todo);
        mTaskListView.setAdapter(listAdapter);
        updateUI();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent i =new Intent(MainActivity.this,addTasks.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+ TaskContract.TaskEntry.TABLE,null);
        if(cursor.moveToFirst()) {
            do{
                String title,desc,points;
                title=cursor.getString(1);
                desc = cursor.getString(2);
                points =cursor.getString(3);
                DataProvider dp = new DataProvider(title,desc,points);
                listAdapter.add(dp);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        SQLiteDatabase db1 = mHelper.getReadableDatabase();
        Cursor cursor1 = db1.query(TaskContract.PointsEntry.TABLE1,
                new String[] {TaskContract.PointsEntry.COL_POINTS_TP},
                TaskContract.PointsEntry._ID+"= ?",
                new String[] {"1"},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToFirst();
        int idx = cursor1.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

        Log.d(TAG, "Points: " + cursor1.getString(idx));

        int Points = Integer.parseInt(cursor1.getString(idx));
        pointsDisplay.setText("Total Points :"+Integer.toString(Points));
        db1.close();
    }

    public void doneTask(View view) {
        View parent = (View) view.getParent();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.PointsEntry.TABLE1,
                new String[] {TaskContract.PointsEntry.COL_POINTS_TP},
                TaskContract.PointsEntry._ID+"= ?",
                new String[] {"1"},
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();
        int idx = cursor.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

        Log.d(TAG, "Points: " + cursor.getString(idx));
        int Points = Integer.parseInt(cursor.getString(idx));

        db.close();




        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db2 = mHelper.getReadableDatabase();
        Cursor cursor1 = db2.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry.COL_TASK_POINT},
                TaskContract.TaskEntry.COL_TASK_TITLE+"= ?",
                new String[] {task},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToNext();
        int idx1 = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_POINT);
        Log.d(TAG, "Points: " + cursor.getString(idx1));
        int totalPoints = Integer.parseInt(cursor1.getString(idx)) + Points;
        db2.close();


        SQLiteDatabase db1 = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.PointsEntry.COL_POINTS_TP, Integer.toString(totalPoints));
        db1.update(TaskContract.PointsEntry.TABLE1,values,TaskContract.PointsEntry._ID +  "= ?", new String[] {"1"});
        db1.close();
        SQLiteDatabase db22 = mHelper.getWritableDatabase();
        db22.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db22.close();
        updateUI();
    }

}
