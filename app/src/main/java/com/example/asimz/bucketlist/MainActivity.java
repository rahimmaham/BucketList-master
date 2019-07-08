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
    private PointsDbHelper pHelper;
    private ListView mTaskListView;
    private TextView pointsDisplay;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsDisplay = (TextView) findViewById(R.id.points);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mHelper = new TaskDbHelper(this);
        pHelper = new PointsDbHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
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
//                final EditText taskEditText = new EditText(this);
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("Add a new task")
//                        .setMessage("What do you want to do next?")
//                        .setView(taskEditText)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String task = String.valueOf(taskEditText.getText());
//                                Log.d(TAG, "Task to add: " + task);
//                                SQLiteDatabase db = mHelper.getWritableDatabase();
//                                ContentValues values = new ContentValues();
//                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
//
//                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
//                                        null,
//                                        values,
//                                        SQLiteDatabase.CONFLICT_REPLACE);
//
//                                db.close();
//
//
//                                updateUI();
//                                dialog.dismiss();
//                                }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
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



        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db2 = mHelper.getReadableDatabase();
        Cursor cursor1 = db.query(TaskContract.TaskEntry.TABLE,
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
        updateUI();
    }

}
